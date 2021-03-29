package com.abifarhan.githubuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.abifarhan.githubuser.BuildConfig
import com.abifarhan.githubuser.model.data.GithubFollowingFavorit
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import org.json.JSONArray

class FollowingFavoritViewModel : ViewModel() {
    private val listFollowingFavoritGithub = MutableLiveData<ArrayList<GithubFollowingFavorit>>()

    fun setFollowingGithub(username: String) {
        val listFollowingItems = ArrayList<GithubFollowingFavorit>()
        val url = "https://api.github.com/users/$username/following"
        AndroidNetworking.get(url)
            .addPathParameter("username", username)
            .addHeaders(
                "Authorization", "token  ${
                    BuildConfig.GITHUB_TOKEN
                }"
            )
            .build()
            .getAsJSONArray(object : JSONArrayRequestListener {
                override fun onResponse(response: JSONArray?) {
                    for (i in 0 until response?.length()!!) {
                        val jsonObject = response.getJSONObject(i)
                        val username = jsonObject.getString("login")
                        val photo = jsonObject.getString("avatar_url")
                        val following = GithubFollowingFavorit(
                            username = username,
                            avatar = photo
                        )
                        listFollowingItems.add(following)
                    }
                    listFollowingFavoritGithub.postValue(listFollowingItems)
                }

                override fun onError(anError: ANError?) {
                    Log.d("onFailure", anError?.message.toString())
                }
            })
    }

    fun getFollowingGithub(): LiveData<ArrayList<GithubFollowingFavorit>> {
        return listFollowingFavoritGithub
    }
}
