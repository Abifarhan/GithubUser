package com.abifarhan.consumerapp.db

import android.net.Uri
import android.provider.BaseColumns

object FavoriteDatabaseContract {
    const val AUTHORITY = "com.abifarhan.githubuser"
    const val SCHEME = "content"

    internal class FavoritColumns: BaseColumns {
        companion object{
            private const val TABLE_NAME = "user"
            const val ID = "_id"
            const val id_unique = "id_unique"
            const val USERNAME = "username"
            const val AVATAR = "avatar"
            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}