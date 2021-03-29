package com.abifarhan.githubuser.model.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Github(
        var username: String?,
        var id: String?,
        var avatar: String?,
        var follower: String?,
        var following: String?,
        var repository: String?
): Parcelable