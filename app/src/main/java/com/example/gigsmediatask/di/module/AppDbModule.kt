package com.example.gigsmediatask.di.module

import android.content.Context
import com.example.gigsmediatask.application.MyApplication
import com.example.gigsmediatask.database.AppDatabase
import com.example.gigsmediatask.database.dao.UserDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppDbModule(val application: MyApplication) {

    @Singleton
    @Provides
    fun getUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.getUserDao()
    }

    @Singleton
    @Provides
    fun getRoomDbInstance(): AppDatabase {
        return AppDatabase.getAppDatabaseInstance(getApplicationContext())
    }

    @Singleton
    @Provides
    fun getApplicationContext(): Context {
        return application.applicationContext
    }

}