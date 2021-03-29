package com.abifarhan.githubuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.abifarhan.githubuser.BuildConfig
import com.abifarhan.githubuser.model.data.GithubFollower
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import org.json.JSONArray

class FollowerViewModel : ViewModel() {
    private val listFollowerGithub = MutableLiveData<ArrayList<GithubFollower>>()

    fun setFollowerGithub(username: String) {
        val listFollowerItems = ArrayList<GithubFollower>()
        val url = "https://api.github.com/users/$username/followers"
        AndroidNetworking.get(url)
            .addPathParameter("username", username)
            .addHeaders("Authorization", "token ${BuildConfig.GITHUB_TOKEN}")
            .build()
            .getAsJSONArray(object : JSONArrayRequestListener {
                override fun onResponse(response: JSONArray?) {
                    for (i in 0 until response?.length()!!) {
                        val jsonObject = response.getJSONObject(i)
                        val username = jsonObject.getString("login")
                        val photo = jsonObject.getString("avatar_url")
                        val follower = GithubFollower(
                            username = username,
                            avatar = photo
                        )
                        listFollowerItems.add(follower)
                    }
                    listFollowerGithub.postValue(listFollowerItems)
                }

                override fun onError(anError: ANError?) {
                    Log.d("onFailure", anError?.message.toString())
                }

            })
    }

    fun getFollowerGithub(): LiveData<ArrayList<GithubFollower>> {
        return listFollowerGithub
    }
}