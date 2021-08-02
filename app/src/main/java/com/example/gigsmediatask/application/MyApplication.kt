package com.example.gigsmediatask.application

import android.app.Application
import com.example.gigsmediatask.di.component.AppDbComponent
import com.example.gigsmediatask.di.component.DaggerAppDbComponent
import com.example.gigsmediatask.di.component.DaggerRetroComponent
import com.example.gigsmediatask.di.component.RetroComponent
import com.example.gigsmediatask.di.module.AppDbModule
import com.example.gigsmediatask.di.module.RetroModule

class MyApplication : Application() {

    private lateinit var githubRetroComponent: RetroComponent
    private lateinit var downloadRetroComponent: RetroComponent
    private lateinit var appDbComponent: AppDbComponent

    val githubBaseUrl = "https://api.github.com/search/"
    val downloadBaseUrl = "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/"

    override fun onCreate() {
        super.onCreate()
        githubRetroComponent =
            DaggerRetroComponent.builder().retroModule(RetroModule(githubBaseUrl)).build()
        downloadRetroComponent =
            DaggerRetroComponent.builder().retroModule(RetroModule(downloadBaseUrl)).build()
        appDbComponent = DaggerAppDbComponent.builder().appDbModule(AppDbModule(this)).build()
    }

    fun getGithubRetroComponent(): RetroComponent {
        return githubRetroComponent
    }

    fun getDownloadRetroComponent(): RetroComponent {
        return downloadRetroComponent
    }

    fun getAppDbComponent(): AppDbComponent {
        return appDbComponent
    }
}