package com.abifarhan.githubuser.model.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abifarhan.githubuser.R
import com.abifarhan.githubuser.databinding.ItemListFollowingBinding
import com.abifarhan.githubuser.model.data.GithubFollowing
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class ListGithubFollowingAdapter :
    RecyclerView.Adapter<ListGithubFollowingAdapter.ListViewHolder>() {
    private val mData = ArrayList<GithubFollowing>()

    fun setData(items: ArrayList<GithubFollowing>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val mView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_following, parent, false)
        return ListViewHolder(mView)
    }

    override fun onBindViewHolder(
        holder: ListViewHolder,
        position: Int
    ) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int = mData.size

    inner class ListViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val binding = ItemListFollowingBinding.bind(itemView)
        fun bind(following: GithubFollowing) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(following.avatar)
                    .apply(RequestOptions().override(150, 150))
                    .into(binding.imgPhotoFollowing)

                binding.textViewUsernameFollowing.text = following.username
            }
        }
    }
}