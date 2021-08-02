package com.example.gigsmediatask.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gigsmediatask.database.dao.UserDao
import com.example.gigsmediatask.database.entity.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getUserDao(): UserDao

    companion object {
        private var dbInstance: AppDatabase? = null
        fun getAppDatabaseInstance(context: Context): AppDatabase {
            if (dbInstance == null) {
                dbInstance =
                    Room.databaseBuilder<AppDatabase>(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "Database"
                    )
                        .allowMainThreadQueries()
                        .build()
            }
            return dbInstance!!
        }
    }

}