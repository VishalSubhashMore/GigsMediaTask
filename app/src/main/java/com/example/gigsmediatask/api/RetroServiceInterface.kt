package com.example.gigsmediatask.api

import com.example.gigsmediatask.model.GithubDataLList
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RetroServiceInterface {

    @GET("repositories")
    fun getDataFromAPI(@Query("q") query: String): Call<GithubDataLList>?

//    @POST("https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf")
    @POST("pdf/dummy.pdf")
    fun downloadPDF(): Call<ResponseBody>
}