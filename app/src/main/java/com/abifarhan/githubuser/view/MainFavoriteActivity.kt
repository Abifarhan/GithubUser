package com.abifarhan.githubuser.view

import android.content.Intent
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.abifarhan.githubuser.databinding.ActivityMainFavoriteBinding
import com.abifarhan.githubuser.model.data.UserFavourite
import com.abifarhan.githubuser.model.adapter.FavoAdapter
import com.abifarhan.githubuser.model.db.FavoriteDatabaseContract.FavoritColumns.Companion.CONTENT_URI
import com.abifarhan.githubuser.model.helper.MappingFavoritHelper
import com.abifarhan.githubuser.viewmodel.FavoritViewModel
import com.abifarhan.githubuser.viewmodel.ViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainFavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainFavoriteBinding
    private lateinit var favoritViewModel: FavoritViewModel
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
        val favoritFactory = ViewModelFactory(applicationContext)
        favoritViewModel =
            ViewModelProvider(this, favoritFactory)
                .get(FavoritViewModel::class.java)

        adapter.setOnDeleteListener { favorite, position ->
            favoritViewModel.deleteFavoritGithub(favorite.id.toString())
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

        adapter.setOnItemClickCallback(object : FavoAdapter.OnItemClickCallback {
            override fun onItemClicked(userFavourite: UserFavourite) {
                Toast.makeText(
                    baseContext,
                    "anda memilih ${userFavourite.username}",
                    Toast.LENGTH_SHORT
                ).show()
                val detailFavoritIntent =
                    Intent(this@MainFavoriteActivity, DetailFavoritActivity::class.java)
                detailFavoritIntent.putExtra(
                    DetailFavoritActivity.EXTRA_FAVORIT_DETAIL,
                    userFavourite
                )
                startActivity(detailFavoritIntent)
            }
        })
    }

    private fun loadUserAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            favoritViewModel.showRecyclerFavorit()
            val deferredFav = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingFavoritHelper.mapCursorToArrayList(cursor)
            }
            favoritViewModel.closeTheDatabase()
            val favoritDef = deferredFav.await()
            if (favoritDef.size > 0) {
                adapter.listFavorit = favoritDef
            } else {
                adapter.listFavorit = ArrayList()
                showSnackbarMessage("Tidak ada data saat ini")
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listFavorit)
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(binding.rvListUserFavourite, message, Snackbar.LENGTH_SHORT).show()
    }

}