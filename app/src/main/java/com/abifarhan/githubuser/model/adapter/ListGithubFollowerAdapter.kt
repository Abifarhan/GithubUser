package com.abifarhan.githubuser.model.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abifarhan.githubuser.R
import com.abifarhan.githubuser.databinding.ItemListFollowerBinding
import com.abifarhan.githubuser.model.data.GithubFollower
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class ListGithubFollowerAdapter : RecyclerView.Adapter<ListGithubFollowerAdapter.ListViewHolder>() {
    private val mData = ArrayList<GithubFollower>()

    fun setData(items: ArrayList<GithubFollower>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val mView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_follower, parent, false)
        return ListViewHolder(mView)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int = mData.size

    inner class ListViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val binding = ItemListFollowerBinding.bind(itemView)
        fun bind(follower: GithubFollower) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(follower.avatar)
                    .apply(RequestOptions().override(150, 150))
                    .into(binding.imgPhotoFollower)

                binding.textViewUsernameFollower.text = follower.username
            }
        }
    }
}