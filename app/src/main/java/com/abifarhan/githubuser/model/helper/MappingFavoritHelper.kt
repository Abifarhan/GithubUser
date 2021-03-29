package com.abifarhan.githubuser.model.helper

import android.database.Cursor
import com.abifarhan.githubuser.model.data.UserFavourite
import com.abifarhan.githubuser.model.db.FavoriteDatabaseContract

object MappingFavoritHelper {
    fun mapCursorToArrayList(favoritCursor: Cursor?): ArrayList<UserFavourite> {
        val favlist = ArrayList<UserFavourite>()

        favoritCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(FavoriteDatabaseContract.FavoritColumns.ID))
                val idUnique =
                    getInt(getColumnIndexOrThrow(FavoriteDatabaseContract.FavoritColumns.id_unique))
                val username =
                    getString(getColumnIndexOrThrow(FavoriteDatabaseContract.FavoritColumns.USERNAME))
                val avatar =
                    getString(getColumnIndexOrThrow(FavoriteDatabaseContract.FavoritColumns.AVATAR))
                favlist.add(UserFavourite(id, idUnique.toString(), username, avatar))
            }
        }
        return favlist
    }

    fun mapCursorToObject(favoritCursor: Cursor?): UserFavourite {
        var userFavorit = UserFavourite()
        favoritCursor?.apply {
            moveToFirst()
            val id = getInt(getColumnIndexOrThrow(FavoriteDatabaseContract.FavoritColumns.ID))
            val idUnique =
                getInt(getColumnIndexOrThrow(FavoriteDatabaseContract.FavoritColumns.id_unique))
            val username =
                getString(getColumnIndexOrThrow(FavoriteDatabaseContract.FavoritColumns.USERNAME))
            val avatar =
                getString(getColumnIndexOrThrow(FavoriteDatabaseContract.FavoritColumns.AVATAR))
            userFavorit = UserFavourite(id, idUnique.toString(), username, avatar)
        }
        return userFavorit
    }
}