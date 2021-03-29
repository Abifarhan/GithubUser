package com.abifarhan.githubuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.abifarhan.githubuser.BuildConfig
import com.abifarhan.githubuser.model.data.Github
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import org.json.JSONObject

class MainActivityViewModel : ViewModel() {
    private val listUserGithub = MutableLiveData<ArrayList<Github>>()

    fun setUserGithub(username: String) {
        val listUserItems = ArrayList<Github>()

        val apiKey = BuildConfig.GITHUB_TOKEN
        val url = "https://api.github.com/search/users?q=$username"
        AndroidNetworking.get(url)
            .addPathParameter("items", "items")
            .addHeaders("Authorization", "token $apiKey")
            .setPriority(Priority.LOW)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    val items = response?.getJSONArray("items")
                    for (i in 0 until items?.length()!!) {
                        val item = items.getJSONObject(i)
                        val username = item.getString("login")
                        val idUnique = item.getString("id")
                        val avatar = item.getString("avatar_url")
                        val follower = item.getString("followers_url")
                        val following = item.getString("following_url")
                        val repository = item.getString("repos_url")
                        val github = Github(
                            username,
                            idUnique,
                            avatar,
                            follower,
                            following,
                            repository
                        )
                        listUserItems.add(github)
                    }
                    listUserGithub.postValue(listUserItems)
                }

                override fun onError(anError: ANError?) {
                    Log.d("onFailure", anError?.message.toString())
                }
            })
    }

    fun getUserGithub(): LiveData<ArrayList<Github>> {
        return listUserGithub
    }
}