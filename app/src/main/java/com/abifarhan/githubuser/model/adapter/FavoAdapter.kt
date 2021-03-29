package com.abifarhan.githubuser.model.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abifarhan.githubuser.R
import com.abifarhan.githubuser.databinding.ItemFavouriteBinding
import com.abifarhan.githubuser.model.data.UserFavourite
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class FavoAdapter(private val activity: Activity) : RecyclerView.Adapter<FavoAdapter.ViewHolder>() {
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    var listFavorit = ArrayList<UserFavourite>()
        set(listFavorit) {
            if (listFavorit.size > 0) {
                this.listFavorit.clear()
            }
            this.listFavorit.addAll(listFavorit)
            notifyDataSetChanged()

        }

    private lateinit var deleteListener: ((UserFavourite, Int) -> Unit)

    fun setOnDeleteListener(listener: (UserFavourite, Int) -> Unit) {
        deleteListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_favourite, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listFavorit[position], deleteListener, position)

        Log.e("Adapter", "$listFavorit")
    }

    override fun getItemCount(): Int {
        return this.listFavorit.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = ItemFavouriteBinding.bind(itemView)
        fun bind(
            favorit: UserFavourite,
            deleteListener: (UserFavourite, Int) -> Unit,
            position: Int
        ) {
            binding.textViewName.text = favorit.username
            Glide.with(itemView.context)
                .load(favorit.avatar)
                .apply(RequestOptions().override(200, 200))
                .into(binding.imgPhotoFavourite)
            binding.buttonDeleteFav.setOnClickListener { deleteListener(favorit, position) }
            itemView.setOnClickListener {
                onItemClickCallback?.onItemClicked(favorit)
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(userFavourite: UserFavourite)
    }
}