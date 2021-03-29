package com.abifarhan.consumerapp.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserFavourite(
    var id: Int? = 0,
    var id_unique: String? = null,
    var username: String? = null,
    var avatar: String? = null
) : Parcelable