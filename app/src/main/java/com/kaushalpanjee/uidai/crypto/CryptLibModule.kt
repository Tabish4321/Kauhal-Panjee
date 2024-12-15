package com.kaushalpanjee.uidai.crypto

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class) // Use appropriate component scope
class CryptLibModule {
    @Provides
    fun provideCryptLib(): CryptLib {
        return CryptLib()
    }
}