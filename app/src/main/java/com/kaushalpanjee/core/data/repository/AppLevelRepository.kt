package com.kaushalpanjee.core.data.repository

import com.kaushalpanjee.core.data.local.database.AppDatabase
import com.kaushalpanjee.core.data.remote.AppLevelApi
import com.kaushalpanjee.core.di.AppModule
import javax.inject.Inject

class AppLevelRepository @Inject constructor(
    @AppModule.PreLoginAppLevelApi private val appLevelApiPreLogin: AppLevelApi,
    @AppModule.PostLoginAppLevelApi private val appLevelApiPostLogin: AppLevelApi,
    appDatabase: AppDatabase
) {



}