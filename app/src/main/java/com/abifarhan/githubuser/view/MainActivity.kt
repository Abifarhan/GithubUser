package com.abifarhan.githubuser.view

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.abifarhan.githubuser.R
import com.abifarhan.githubuser.databinding.ActivityMainBinding
import com.abifarhan.githubuser.model.data.Github
import com.abifarhan.githubuser.model.adapter.ListGithubAdapter
import com.abifarhan.githubuser.view.alarm.SettingsActivity
import com.abifarhan.githubuser.viewmodel.MainActivityViewModel
import com.androidnetworking.AndroidNetworking

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: ListGithubAdapter
    private lateinit var mainActivityViewModel: MainActivityViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvListUser.setHasFixedSize(true)
        binding.rvListUser.layoutManager = LinearLayoutManager(this)
        mainActivityViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MainActivityViewModel::class.java
        )
        AndroidNetworking.initialize(applicationContext)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.search)?.actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                proceedTheSearch(query!!)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                proceedTheSearch(newText!!)
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.change_language -> {
                val changeIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(changeIntent)
            }
            R.id.action_see_list_favourite -> {
                startActivity(Intent(this, MainFavoriteActivity::class.java))
            }
            R.id.action_set_alarm -> {
                startActivity(Intent(this, SettingsActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun proceedTheSearch(username: String) {
        mainActivityViewModel.setUserGithub(username)
        showLoading(true)
        adapter = ListGithubAdapter()
        adapter.notifyDataSetChanged()
        binding.rvListUser.adapter = adapter
        mainActivityViewModel.getUserGithub().observe(this@MainActivity, { userItems ->
            if (userItems != null) {
                adapter.setData(userItems)
                Log.d(this.toString(), " ini adalah user$userItems")
                showLoading(false)
            }
            adapter.setOnItemClickCallback(object : ListGithubAdapter.OnItemClickCallback {
                override fun onItemClicked(data: Github) {
                    val detailIntent = Intent(this@MainActivity, DetailActivity::class.java)
                    detailIntent.putExtra(DetailActivity.EXTRA_GITHUB, data)
                    startActivity(detailIntent)
                }
            })
        })
    }
}