package com.abifarhan.githubuser.viewmodel

import android.content.ContentValues
import android.content.Context
import androidx.lifecycle.ViewModel
import com.abifarhan.githubuser.model.data.UserFavourite
import com.abifarhan.githubuser.model.db.FavoritHelper
import com.abifarhan.githubuser.model.db.FavoriteDatabaseContract

class FavoritViewModel(private val context: Context) : ViewModel() {
    private var fav: UserFavourite? = null
    private lateinit var favHelper: FavoritHelper

    fun setFavoritGithub(username: String, avatar: String, idUnique: String) {
        favHelper = FavoritHelper.getInstance(context)
        favHelper.open()
        val values = ContentValues()
        fav
        values.put(FavoriteDatabaseContract.FavoritColumns.USERNAME, username)
        values.put(FavoriteDatabaseContract.FavoritColumns.AVATAR, avatar)
        values.put(FavoriteDatabaseContract.FavoritColumns.id_unique, idUnique)
        val result = favHelper.insert(values)
        fav?.id = result.toInt()
    }

    fun deleteFavoritGithub(id: String) {
        favHelper = FavoritHelper.getInstance(context)
        favHelper.open()
        favHelper.deleteById(id)
    }

    fun showRecyclerFavorit() {
        favHelper = FavoritHelper.getInstance(context)
        favHelper.open()
    }

    fun closeTheDatabase() {
        favHelper = FavoritHelper.getInstance(context)
        favHelper.open()
        favHelper.close()
    }
}