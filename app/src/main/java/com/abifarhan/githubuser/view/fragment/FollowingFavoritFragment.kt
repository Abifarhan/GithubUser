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
import com.abifarhan.githubuser.model.adapter.ListGithubFollowingFavoritAdapter
import com.abifarhan.githubuser.viewmodel.FollowingFavoritViewModel


class FollowingFavoritFragment : Fragment() {
    private lateinit var adapter: ListGithubFollowingFavoritAdapter
    private lateinit var followingFavoritViewModel: FollowingFavoritViewModel
    private lateinit var recyclerView: RecyclerView

    companion object{
        private const val ARG_USERNAME = "username"
        fun newInstance(username: String): FollowingFavoritFragment {
            val fragment = FollowingFavoritFragment()
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
        return inflater.inflate(R.layout.fragment_following_favorit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.rv_following_favorit)
        recyclerView.setHasFixedSize(true)
        adapter = ListGithubFollowingFavoritAdapter()
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        followingFavoritViewModel = ViewModelProvider(this.requireActivity(), ViewModelProvider.NewInstanceFactory()).get(
            FollowingFavoritViewModel::class.java
        )
        val username = arguments?.getString(ARG_USERNAME)
        followingFavoritViewModel.setFollowingGithub(username!!)
        showLoading(true, view)
        adapter.notifyDataSetChanged()
        recyclerView.adapter = adapter
        followingFavoritViewModel.getFollowingGithub().observe(requireActivity(), {
            if (it != null) {
                adapter.setData(it)
                showLoading(false,view)
            }
        })

    }

    private fun showLoading(state: Boolean, view: View) {
        val progressBar: ProgressBar = view.findViewById(R.id.progressBar_following_favorit)
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.INVISIBLE
        }
    }
}