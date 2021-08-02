package com.example.gigsmediatask.di.component

import com.example.gigsmediatask.di.module.RetroModule
import com.example.gigsmediatask.api.RetroServiceInterface
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RetroModule::class])
interface RetroComponent {
    fun getRetroServiceInterface(): RetroServiceInterface
}