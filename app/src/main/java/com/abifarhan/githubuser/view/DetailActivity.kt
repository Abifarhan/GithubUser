package com.abifarhan.githubuser.view

import android.content.ContentValues
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.abifarhan.githubuser.R
import com.abifarhan.githubuser.databinding.ActivityDetailBinding
import com.abifarhan.githubuser.model.data.UserFavourite
import com.abifarhan.githubuser.model.data.Github
import com.abifarhan.githubuser.model.adapter.SectionsPagerAdapter
import com.abifarhan.githubuser.model.db.FavoriteDatabaseContract.FavoritColumns.Companion.CONTENT_URI
import com.abifarhan.githubuser.model.helper.MappingFavoritHelper
import com.abifarhan.githubuser.view.fragment.FollowersFragment
import com.abifarhan.githubuser.view.fragment.FollowingFragment
import com.abifarhan.githubuser.viewmodel.DetailViewModel
import com.abifarhan.githubuser.viewmodel.FavoritViewModel
import com.abifarhan.githubuser.viewmodel.ViewModelFactory
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var favoritViewModel: FavoritViewModel
    private var fav: UserFavourite? = null
    private lateinit var uriWithId: Uri

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
        const val EXTRA_GITHUB = "extra_person"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val github = intent.getParcelableExtra<Github>(EXTRA_GITHUB) as Github
        Glide.with(this)
            .load(github.avatar)
            .into(binding.imageViewUserDetail)
        binding.textViewNameDetail.text = github.username
        binding.textViewRepositoryDetail.text = github.repository
        val idUnique = github.id
        Toast.makeText(this, "unik id kamu adalah $idUnique", Toast.LENGTH_SHORT).show()
        val bundle = Bundle()
        bundle.putString("username", github.username)
        val followingFragment = FollowingFragment()
        val followersFragment = FollowersFragment()
        followersFragment.arguments = bundle
        followingFragment.arguments = bundle
        val sectionPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionPagerAdapter
        sectionPagerAdapter.username = github.username
        Log.d("context", " usernamenya adalah :${sectionPagerAdapter.username}")
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f

        uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + fav?.id)
        val cursor = contentResolver.query(uriWithId, null, null, null, null)
        if (cursor != null) {
            fav = MappingFavoritHelper.mapCursorToObject(cursor)
            cursor.close()
        }

        val favoritFactory = ViewModelFactory(applicationContext)
        favoritViewModel =
            ViewModelProvider(this, favoritFactory)
                .get(FavoritViewModel::class.java)

        binding.apply {
            fbFavourite.setOnClickListener {
                val username = github.username
                val avatar = github.avatar
                val idUnique = github.id

                favoritViewModel.setFavoritGithub(username!!, avatar!!, idUnique!!)
                Toast.makeText(this@DetailActivity,"Anda sudah berhasil menambah data username :$username, dengan foto : $avatar dan id : $idUnique.", Toast.LENGTH_SHORT).show()
                Toast.makeText(this@DetailActivity,"Jika data sudah ada sebelumnya maka data terbaru akan menghapus data yang lama", Toast.LENGTH_SHORT).show()
                binding.fbFavourite.visibility = View.GONE
            }
        }

        detailViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(DetailViewModel::class.java)
        detailViewModel.setDetailUser(github.username!!)
        detailViewModel.detailUser.observe(this, {
            binding.textViewFollowersDetail.text = it.follower
            binding.textViewFollowingDetail.text = it.following
            binding.textViewRepositoryDetail.text = it.repository
            if (it.company == "null") {
                binding.textViewCompanyDetail.text =
                    resources.getString(R.string.nothing)
            } else {
                binding.textViewCompanyDetail.text = it.company
            }
            binding.progressBarDetail.visibility = View.INVISIBLE
        })
    }
}


