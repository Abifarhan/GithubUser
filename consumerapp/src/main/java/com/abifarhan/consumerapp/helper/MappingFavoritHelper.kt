package com.abifarhan.consumerapp.helper

import android.database.Cursor
import com.abifarhan.consumerapp.db.FavoriteDatabaseContract
import com.abifarhan.consumerapp.entity.UserFavourite

object MappingFavoritHelper {
    fun mapCursorToArrayList(favoritCursor: Cursor?) : ArrayList<UserFavourite>{
        val favlist = ArrayList<UserFavourite>()
        favoritCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(FavoriteDatabaseContract.FavoritColumns.ID))
                val idUnique = getInt(getColumnIndexOrThrow(FavoriteDatabaseContract.FavoritColumns.id_unique))
                val username = getString(getColumnIndexOrThrow(FavoriteDatabaseContract.FavoritColumns.USERNAME))
                val avatar = getString(getColumnIndexOrThrow(FavoriteDatabaseContract.FavoritColumns.AVATAR))
                favlist.add(UserFavourite(id,idUnique.toString(),username,avatar))
            }
        }
        return favlist
    }
}