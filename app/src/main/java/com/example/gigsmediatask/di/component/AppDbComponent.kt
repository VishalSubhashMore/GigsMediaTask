package com.example.gigsmediatask.di.component

import com.example.gigsmediatask.di.module.AppDbModule
import com.example.gigsmediatask.viewmodel.UserViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppDbModule::class])
interface AppDbComponent {
    fun inject(userViewModel: UserViewModel)
}