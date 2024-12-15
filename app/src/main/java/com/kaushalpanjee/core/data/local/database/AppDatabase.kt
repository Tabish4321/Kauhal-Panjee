package com.kaushalpanjee.core.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kaushalpanjee.core.data.local.dao.UserDao
import com.kaushalpanjee.core.data.local.entity.UserEntity

@Database(entities = [UserEntity::class],
    version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun getUserDao() : UserDao
}