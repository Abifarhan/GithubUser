package com.abifarhan.githubuser.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abifarhan.githubuser.R
import com.abifarhan.githubuser.model.adapter.ListGithubFollowerFavoritAdapter
import com.abifarhan.githubuser.viewmodel.FollowerFavoritViewModel


class FollowerFavoritFragment : Fragment() {
    private lateinit var adapter: ListGithubFollowerFavoritAdapter
    private lateinit var followerFavoritViewModel: FollowerFavoritViewModel
    private lateinit var recyclerView: RecyclerView

    companion object {
        private const val ARG_USERNAME = "username"
        fun newInstance(username: String): FollowerFavoritFragment {
            val fragment = FollowerFavoritFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_follower_favorit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.rv_follower_favorit)
        recyclerView.setHasFixedSize(true)
        adapter = ListGithubFollowerFavoritAdapter()
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        followerFavoritViewModel =
            ViewModelProvider(this.requireActivity(), ViewModelProvider.NewInstanceFactory()).get(
                FollowerFavoritViewModel::class.java
            )

        val username = arguments?.getString(ARG_USERNAME)
        followerFavoritViewModel.setFollowerGithub(username!!)
        showLoading(true, view)
        adapter.notifyDataSetChanged()
        recyclerView.adapter = adapter
        followerFavoritViewModel.getFollowerGithub().observe(requireActivity(), {
            if (it != null) {
                adapter.setData(it)
                showLoading(false, view)
            }
        })
    }

    private fun showLoading(state: Boolean, view: View) {
        val progressBar: ProgressBar = view.findViewById(R.id.progressBar_followers_favorit)
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.INVISIBLE
        }
    }
}