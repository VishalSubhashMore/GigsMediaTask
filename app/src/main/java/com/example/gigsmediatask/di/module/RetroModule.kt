package com.example.gigsmediatask.di.module

import com.example.gigsmediatask.api.RetroServiceInterface
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class RetroModule(val baseUrl: String) {

    //        val baseUrl = "https://api.github.com/search/"
//    val baseUrl = "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/"
    @Singleton
    @Provides
    fun getRetroServiceInterface(retrofit: Retrofit): RetroServiceInterface {
        return retrofit.create(RetroServiceInterface::class.java)
    }

    @Singleton
    @Provides
    fun getRetrofitInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}