package com.example.gigsmediatask.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.gigsmediatask.application.MyApplication
import com.example.gigsmediatask.api.RetroServiceInterface
import com.example.gigsmediatask.model.GithubDataLList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Task3ViewModel(application: Application) : AndroidViewModel(application) {

    var retroService: RetroServiceInterface = (application as MyApplication).getGithubRetroComponent().getRetroServiceInterface()

    private var githubLiveData: MutableLiveData<GithubDataLList> = MutableLiveData()

    fun getGithubLiveData(): LiveData<GithubDataLList> {
        return githubLiveData
    }

    fun callGithubAPI() {
        val call: Call<GithubDataLList>? = retroService.getDataFromAPI("india")
        call?.enqueue(object : Callback<GithubDataLList> {
            override fun onResponse(
                call: Call<GithubDataLList>,
                response: Response<GithubDataLList>
            ) {
                if (response.isSuccessful) {
                    githubLiveData.postValue(response.body())
                } else {
                    githubLiveData.postValue(null)
                }
            }

            override fun onFailure(call: Call<GithubDataLList>, t: Throwable) {
                githubLiveData.postValue(null)
            }

        })
    }

}