package com.example.gigsmediatask.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.gigsmediatask.application.MyApplication
import com.example.gigsmediatask.database.dao.UserDao
import com.example.gigsmediatask.database.entity.UserEntity
import javax.inject.Inject

class UserViewModel(application: Application) : AndroidViewModel(application) {

    @Inject
    lateinit var userDao: UserDao

    lateinit var userList: MutableLiveData<List<UserEntity>>

    init {
        (application as MyApplication).getAppDbComponent().inject(this)
        userList = MutableLiveData()
    }

    fun getUsers(): LiveData<List<UserEntity>> {
        return userList
    }

    fun getAllUserList() {
        val list = userDao.getAllUsers()
        userList.postValue(list)
    }

    fun insertUser(userEntity: UserEntity) {
        userDao.insertUser(userEntity)
//        getAllUserList()
    }

    fun updateUser(userEntity: UserEntity) {
        userDao.updateUser(userEntity)
        getAllUserList()
    }

    fun deleteUser(userEntity: UserEntity) {
        userDao.deleteUser(userEntity)
        getAllUserList()
    }

}