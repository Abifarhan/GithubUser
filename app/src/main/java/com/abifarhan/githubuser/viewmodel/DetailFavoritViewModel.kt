package com.abifarhan.githubuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.abifarhan.githubuser.BuildConfig
import com.abifarhan.githubuser.model.data.GithubDetail
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import org.json.JSONObject

class DetailFavoritViewModel : ViewModel() {
    private val oneItem = MutableLiveData<GithubDetail>()

    fun setDetailUser(username: String) {
        AndroidNetworking.get("https://api.github.com/users/$username")
            .addPathParameter("username", username)
            .addHeaders(
                "Authorization", "token ${
                    BuildConfig.GITHUB_TOKEN
                }"
            )
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(responseObject: JSONObject?) {
                    val followers = responseObject?.getString("followers")
                    val following = responseObject?.getString("following")
                    val company = responseObject?.getString("company")
                    val repository = responseObject?.getString("public_repos")
                    val githubDetail = GithubDetail(
                        followers.toString(),
                        following.toString(),
                        company.toString(),
                        repository.toString()
                    )
                    oneItem.postValue(githubDetail)
                }
                override fun onError(anError: ANError?) {
                    Log.d("onFailure", anError?.message.toString())
                }
            })
    }

    val detailUser: LiveData<GithubDetail>
        get() = oneItem
}