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
import com.abifarhan.githubuser.model.adapter.ListGithubFollowingAdapter
import com.abifarhan.githubuser.viewmodel.FollowingViewModel


class FollowingFragment : Fragment() {
    private lateinit var adapter: ListGithubFollowingAdapter
    private lateinit var followingViewModel: FollowingViewModel
    private lateinit var recyclerView: RecyclerView

    companion object {
        private const val ARG_USERNAME = "username"
        fun newInstance(username: String): FollowingFragment {
            val fragment = FollowingFragment()
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
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.rv_following)
        recyclerView.setHasFixedSize(true)
        adapter = ListGithubFollowingAdapter()
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        followingViewModel = ViewModelProvider(this.requireActivity(), ViewModelProvider.NewInstanceFactory()).get(
            FollowingViewModel::class.java)
        val username = arguments?.getString(ARG_USERNAME)
        followingViewModel.setFollowingGithub(username!!)
        showLoading(true, view)
        adapter.notifyDataSetChanged()
        recyclerView.adapter = adapter
        followingViewModel.getFollowingGithub().observe(requireActivity(), { followingItems ->
            if (followingItems != null) {
                adapter.setData(followingItems)
                showLoading(false,view)
            }
        })
    }

    private fun showLoading(state: Boolean, view: View) {
        val progressBar: ProgressBar = view.findViewById(R.id.progressBar_following)
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.INVISIBLE
        }
    }
}