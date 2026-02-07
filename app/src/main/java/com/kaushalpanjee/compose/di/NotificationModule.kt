package com.kaushalpanjee.compose.di



import com.kaushalpanjee.compose.data.repository.ChangePassWordRepoImpl
import com.kaushalpanjee.compose.data.repository.NotificationRepositoryImpl
import com.kaushalpanjee.compose.domain.repository.ChangePasswordRepository
import com.kaushalpanjee.compose.domain.repository.NotificationRepository
import com.kaushalpanjee.compose.domain.usecase.GetChangePasswordUseCases
import com.kaushalpanjee.compose.domain.usecase.GetNotificationsUseCase
import com.kaushalpanjee.compose.domain.usecase.UpdateNotificationStatusUseCase
import com.kaushalpanjee.core.data.remote.AppLevelApi
import com.kaushalpanjee.core.di.AppModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Retrofit
import javax.inject.Singleton
/**
 * Created by Rishi Porwal
 */


@Module
@InstallIn(ViewModelComponent::class)
object NotificationModule {


    @Provides
    @ViewModelScoped
    fun provideNotificationRepository(
        @AppModule.PostLoginAppLevelApi apiService: AppLevelApi
    ): NotificationRepository {
        return NotificationRepositoryImpl(apiService)
    }

    @Provides
    @ViewModelScoped
    fun provideGetNotificationsUseCase(
        repository: NotificationRepository
    ): GetNotificationsUseCase {
        return GetNotificationsUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideUpdateNotificationStatusUseCase(
        repository: NotificationRepository
    ): UpdateNotificationStatusUseCase {
        return UpdateNotificationStatusUseCase(repository)
    }

}