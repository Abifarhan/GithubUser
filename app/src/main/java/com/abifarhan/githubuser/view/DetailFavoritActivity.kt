package com.abifarhan.githubuser.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.abifarhan.githubuser.R
import com.abifarhan.githubuser.databinding.ActivityDetailFavoritBinding
import com.abifarhan.githubuser.model.adapter.SectionFavoritPagerAdapter
import com.abifarhan.githubuser.model.data.UserFavourite
import com.abifarhan.githubuser.view.fragment.FollowerFavoritFragment
import com.abifarhan.githubuser.view.fragment.FollowingFavoritFragment
import com.abifarhan.githubuser.viewmodel.DetailFavoritViewModel
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailFavoritActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailFavoritBinding
    private lateinit var detailViewModel: DetailFavoritViewModel
    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
        const val EXTRA_FAVORIT_DETAIL = "EXTRA_FAVORIT_DETAIL"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailFavoritBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val githubFavorit =
            intent.getParcelableExtra<UserFavourite>(EXTRA_FAVORIT_DETAIL) as UserFavourite
        Glide.with(this)
            .load(githubFavorit.avatar)
            .into(binding.imageViewUserDetailFavorit)
        binding.textViewNameDetailFavorit.text = githubFavorit.username
        binding.fbFavouriteShare.setOnClickListener {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT,"Anda mendapatkan data user Favorit yaitu : ${githubFavorit.username} dan ini fotonya : ${githubFavorit.avatar} dengan id : ${githubFavorit.id_unique} dan id tabel database : ${githubFavorit.id}")
                type = "text/plain"
            }
            startActivity(shareIntent)
        }
        val bundle = Bundle()
        bundle.putString("username", githubFavorit.username)
        val followingFavoritFragment = FollowingFavoritFragment()
        val followerFavoritFragment = FollowerFavoritFragment()
        followerFavoritFragment.arguments = bundle
        followingFavoritFragment.arguments = bundle
        val sectionPagerAdapter = SectionFavoritPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager_favorit)
        viewPager.adapter = sectionPagerAdapter
        sectionPagerAdapter.username = githubFavorit.username
        val tabs: TabLayout = findViewById(R.id.tabs_favorit)
        TabLayoutMediator(tabs, viewPager){ tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailFavoritViewModel::class.java)
        detailViewModel.setDetailUser(githubFavorit.username!!)
        detailViewModel.detailUser.observe(this,{
            binding.textViewFollowersDetailFavorit.text = it.follower
            binding.textViewFollowingDetailFavorit.text = it.following
            binding.textViewRepositoryDetailFavorit.text = it.repository
            if (it.company == "null") {
                binding.textViewCompanyDetailFavorit.text =
                    resources.getString(R.string.nothing)
            } else {
                binding.textViewCompanyDetailFavorit.text = it.company
            }
            binding.progressBarDetail.visibility = View.INVISIBLE
        })
    }


}