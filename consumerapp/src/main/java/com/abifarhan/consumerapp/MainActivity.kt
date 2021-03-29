package com.abifarhan.consumerapp

import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.abifarhan.consumerapp.adapter.FavoAdapter
import com.abifarhan.consumerapp.databinding.ActivityMainFavoriteBinding
import com.abifarhan.consumerapp.db.FavoriteDatabaseContract.FavoritColumns.Companion.CONTENT_URI
import com.abifarhan.consumerapp.entity.UserFavourite
import com.abifarhan.consumerapp.helper.MappingFavoritHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainFavoriteBinding
    private lateinit var adapter: FavoAdapter
    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvListUserFavourite.layoutManager = LinearLayoutManager(this)
        binding.rvListUserFavourite.setHasFixedSize(true)
        adapter = FavoAdapter(this)
        adapter.setOnDeleteListener { favorite, position ->
            Toast.makeText(this,"Anda klik hapus, Anda tidak bisa melakukan penghapusan disini. Anda bisa menghapus data di app provider", Toast.LENGTH_SHORT).show()
        }
        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(handler) {
            override fun onChange(selfChange: Boolean) {
                loadUserAsync()
            }
        }
        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)
        binding.rvListUserFavourite.adapter = adapter
        if (savedInstanceState == null) {
            loadUserAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<UserFavourite>(EXTRA_STATE)
            if (list != null) {
                adapter.listFavorit = list
            }
        }
    }

    private fun loadUserAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            val deferredFav = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI,null,null,null,null)
                MappingFavoritHelper.mapCursorToArrayList(cursor)
            }
            val favorit = deferredFav.await()
            if (favorit.size > 0) {
                adapter.listFavorit = favorit
            } else {
                adapter.listFavorit = ArrayList()
                showSnackbarMessage("Tidak ada data saat ini")
            }
        }
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(binding.rvListUserFavourite, message, Snackbar.LENGTH_SHORT).show()
    }
}