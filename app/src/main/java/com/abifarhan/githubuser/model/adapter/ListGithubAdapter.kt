package com.abifarhan.githubuser.model.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abifarhan.githubuser.R
import com.abifarhan.githubuser.databinding.ItemListBinding
import com.abifarhan.githubuser.model.data.Github
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class ListGithubAdapter : RecyclerView.Adapter<ListGithubAdapter.ListViewHolder>() {
    private val mData = ArrayList<Github>()

    fun setData(items: ArrayList<Github>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return ListViewHolder(mView)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int = mData.size

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemListBinding.bind(itemView)
        fun bind(github: Github) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(github.avatar)
                    .apply(RequestOptions().override(200, 200))
                    .into(binding.imgPhoto)
                binding.textViewName.text = github.username

                itemView.setOnClickListener {
                    onItemClickCallback?.onItemClicked(github)
                }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Github)
    }
}