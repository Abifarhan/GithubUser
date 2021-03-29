package com.abifarhan.githubuser.model.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abifarhan.githubuser.R
import com.abifarhan.githubuser.databinding.ItemListFollowerFavoritBinding
import com.abifarhan.githubuser.model.data.GithubFollowerFavorit
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class ListGithubFollowerFavoritAdapter :
    RecyclerView.Adapter<ListGithubFollowerFavoritAdapter.ViewHolder>() {
    private val mData = ArrayList<GithubFollowerFavorit>()

    fun setData(items: ArrayList<GithubFollowerFavorit>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListGithubFollowerFavoritAdapter.ViewHolder {
        val mView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_follower_favorit, parent, false)
        return ViewHolder(mView)
    }

    override fun onBindViewHolder(
        holder: ListGithubFollowerFavoritAdapter.ViewHolder,
        position: Int
    ) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int = mData.size

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val binding = ItemListFollowerFavoritBinding.bind(itemView)
        fun bind(followerFavorit: GithubFollowerFavorit) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(followerFavorit.avatar)
                    .apply(RequestOptions().override(150, 150))
                    .into(binding.imgPhotoFollowerFavorit)

                binding.textViewUsernameFollowerFavorit.text = followerFavorit.username
            }
        }
    }
}