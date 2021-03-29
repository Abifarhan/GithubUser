package com.abifarhan.githubuser.model.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.abifarhan.githubuser.model.db.FavoriteDatabaseContract.FavoritColumns.Companion.TABLE_NAME

class FavoritDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {

        private const val DATABASE_NAME = "dbgithubapp"

        private const val DATABASE_VERSION = 1

        private const val SQL_CREATE_TABLE_FAVORIT = "CREATE TABLE $TABLE_NAME" +
                " (${FavoriteDatabaseContract.FavoritColumns.ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                " ${FavoriteDatabaseContract.FavoritColumns.id_unique} INTEGER UNIQUE NOT NULL," +
                " ${FavoriteDatabaseContract.FavoritColumns.USERNAME} TEXT UNIQUE NOT NULL," +
                " ${FavoriteDatabaseContract.FavoritColumns.AVATAR} TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE_FAVORIT)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}