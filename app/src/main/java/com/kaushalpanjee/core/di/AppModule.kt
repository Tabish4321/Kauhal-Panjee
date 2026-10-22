package com.kaushalpanjee.core.di

import LoggingInterceptor
import android.content.Context
import androidx.room.Room
import com.kaushalpanjee.core.data.local.database.AppDatabase
import com.kaushalpanjee.core.data.remote.AppLevelApi
import com.kaushalpanjee.core.util.ApiConstant
import com.kaushalpanjee.core.util.AppConstant
import com.kaushalpanjee.core.util.CustomInterceptor
import com.kaushalpanjee.core.util.UserPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class PreLoginOkHttpClient

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class PostLoginOkHttpClient

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class PreLoginAppLevelApi

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class PostLoginAppLevelApi

    @Provides
    @Singleton
    fun providesRoomDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "Kaushal_panjee_db"
        )
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    // PRE-LOGIN RETROFIT
    @Provides
    @Singleton
    @PreLoginOkHttpClient
    fun providesRetrofitForPreLogin(
        userPreferences: UserPreferences,
        @ApplicationContext context: Context
    ): Retrofit {

        val client = getRetrofitClient(
            userPreferences = userPreferences,
            isPostLogin = false,
            isAuthenticationRequired = false,
            skipCustomInterceptor = true,
            context = context
        )

        return Retrofit.Builder()
            .baseUrl(AppConstant.StaticURL.baseUrl)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    // POST-LOGIN RETROFIT
    @Provides
    @Singleton
    @PostLoginOkHttpClient
    fun providesRetrofitForPostLogin(
        userPreferences: UserPreferences,
        @ApplicationContext context: Context
    ): Retrofit {

        val client = getRetrofitClient(
            userPreferences = userPreferences,
            isPostLogin = true,
            isAuthenticationRequired = true,
            skipCustomInterceptor = false,
            context = context
        )

        return Retrofit.Builder()
            .baseUrl(AppConstant.StaticURL.baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun getRetrofitClient(
        authenticator: Authenticator? = null,
        userPreferences: UserPreferences,
        isPostLogin: Boolean,
        isAuthenticationRequired: Boolean,
        skipCustomInterceptor: Boolean,
        context: Context
    ): OkHttpClient {

        val builder = OkHttpClient.Builder()
            .connectTimeout(
                ApiConstant.CONNECT_TIMEOUT,
                TimeUnit.SECONDS
            )
            .writeTimeout(
                ApiConstant.WRITE_TIMEOUT,
                TimeUnit.SECONDS
            )
            .readTimeout(
                ApiConstant.READ_TIMEOUT,
                TimeUnit.SECONDS
            )
            .retryOnConnectionFailure(true)

        // Add Accept header to every request,
        // including requests using the PreLogin client.
        builder.addInterceptor { chain ->
            val request: Request = chain.request()
                .newBuilder()
                .header("Accept", "application/json")
                .build()

            chain.proceed(request)
        }

        // Add custom headers only when required.
        if (!skipCustomInterceptor) {
            builder.addInterceptor(
                CustomInterceptor(
                    isPostLogin,
                    userPreferences,
                    isAuthenticationRequired,
                    context
                )
            )
        }

        // Keep logging last to inspect the request after
        // the preceding interceptors have modified it.
        builder.addInterceptor(LoggingInterceptor())

        authenticator?.let {
            builder.authenticator(it)
        }

        return builder.build()
    }

    @Provides
    @Singleton
    @PreLoginAppLevelApi
    fun providePreLoginAppLevelApi(
        @PreLoginOkHttpClient retrofit: Retrofit
    ): AppLevelApi {
        return retrofit.create(AppLevelApi::class.java)
    }

    @Provides
    @Singleton
    @PostLoginAppLevelApi
    fun providePostLoginAppLevelApi(
        @PostLoginOkHttpClient retrofit: Retrofit
    ): AppLevelApi {
        return retrofit.create(AppLevelApi::class.java)
    }
}