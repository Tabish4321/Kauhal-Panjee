package com.kaushalpanjee.compose.di

import LoggingInterceptor
import com.kaushalpanjee.BuildConfig
import com.kaushalpanjee.compose.data.repository.ChangePassWordRepoImpl
import com.kaushalpanjee.compose.domain.repository.ChangePasswordRepository
import com.kaushalpanjee.compose.domain.usecase.GetChangePasswordUseCases
import com.kaushalpanjee.core.data.remote.AppLevelApi
import com.kaushalpanjee.core.di.AppModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@Module
@InstallIn(ViewModelComponent::class)
object ChangePasswordModule {

    @Provides
    @ViewModelScoped
    fun provideChangePasswordRepository(
        @AppModule.PostLoginAppLevelApi apiService: AppLevelApi
    ): ChangePasswordRepository {
        return ChangePassWordRepoImpl(apiService)
    }


    @Provides
    @ViewModelScoped
    fun provideGetChangePassUseCase(
        repository: ChangePasswordRepository
    ): GetChangePasswordUseCases {
        return GetChangePasswordUseCases(repository)
    }

}