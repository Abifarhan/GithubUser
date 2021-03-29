package com.abifarhan.githubuser.model.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.abifarhan.githubuser.model.db.FavoriteDatabaseContract.FavoritColumns.Companion.TABLE_NAME
import com.abifarhan.githubuser.model.db.FavoriteDatabaseContract.FavoritColumns.Companion.ID
import java.sql.SQLException

class FavoritHelper(context: Context) {
    private lateinit var database: SQLiteDatabase

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private var INSTANCE: FavoritHelper? = null

        private lateinit var dataBaseHelper: FavoritDatabaseHelper

        fun getInstance(context: Context?): FavoritHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: FavoritHelper(context!!)
            }
    }

    init {
        dataBaseHelper = FavoritDatabaseHelper(context)
    }


    @Throws(SQLException::class)
    fun open() {
        database = dataBaseHelper.writableDatabase
    }

    fun close() {
        dataBaseHelper.close()
        if (database.isOpen)
            database.close()
    }

    fun queryAll(): Cursor {
        if (!database.isOpen) open()
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$ID ASC"
        )
    }

    fun queryById(id: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "$ID = ?",
            arrayOf(id),
            null,
            null,
            null,
            null
        )
    }

    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun update(id: String, values: ContentValues?): Int {
        return database.update(DATABASE_TABLE, values, "$ID = ?", arrayOf(id))
    }

    fun deleteById(id: String): Int {
        Log.i("IS_OPEN", database.isOpen.toString())
        return database.delete(DATABASE_TABLE, "$ID = '$id'", null)
    }
}