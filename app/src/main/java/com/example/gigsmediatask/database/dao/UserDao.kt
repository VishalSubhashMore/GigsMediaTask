package com.example.gigsmediatask.database.dao

import androidx.room.*
import com.example.gigsmediatask.database.entity.UserEntity

@Dao
interface UserDao {

    @Query("SELECT * FROM User ORDER BY id DESC")
    fun getAllUsers(): List<UserEntity>?

    @Insert
    fun insertUser(userEntity: UserEntity)

    @Update
    fun updateUser(userEntity: UserEntity)

    @Delete
    fun deleteUser(userEntity: UserEntity)

}