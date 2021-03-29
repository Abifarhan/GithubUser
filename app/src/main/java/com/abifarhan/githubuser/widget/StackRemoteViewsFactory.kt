package com.abifarhan.githubuser.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.os.Binder
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.abifarhan.githubuser.R
import com.abifarhan.githubuser.model.data.UserFavourite
import com.abifarhan.githubuser.model.db.FavoriteDatabaseContract.FavoritColumns.Companion.CONTENT_URI
import com.abifarhan.githubuser.model.helper.MappingFavoritHelper
import com.bumptech.glide.Glide
import java.lang.Exception

internal class StackRemoteViewsFactory(private var mContext: Context) :
    RemoteViewsService.RemoteViewsFactory {
    private lateinit var cursor : Cursor
    private val mWidgetItems = ArrayList<Bitmap>()
    @SuppressLint("Recycle")
    override fun onCreate() {
        cursor = mContext.contentResolver.query(CONTENT_URI, null, null,null,null)!!
        cursor.close()
        val identityToken = Binder.clearCallingIdentity()
        cursor = mContext.contentResolver.query(CONTENT_URI, null, null,null,null)!!
        val favorit: ArrayList<UserFavourite> = MappingFavoritHelper.mapCursorToArrayList(cursor)
        for (i in favorit) {
            try {
                val bitmap: Bitmap = Glide.with(mContext)
                    .asBitmap()
                    .load(i.avatar)
                    .submit(250, 250)
                    .get()
                mWidgetItems.add(bitmap)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        Binder.restoreCallingIdentity(identityToken)
    }

    @SuppressLint("Recycle")
    override fun onDataSetChanged() {
        cursor.close()
        val identityToken = Binder.clearCallingIdentity()
        cursor = mContext.contentResolver.query(CONTENT_URI, null, null,null,null)!!
        val favorit = MappingFavoritHelper.mapCursorToArrayList(cursor)
        for (i in favorit) {
            try {
                val bitmap: Bitmap = Glide.with(mContext)
                    .asBitmap()
                    .load(i.avatar)
                    .submit(250, 250)
                    .get()
                mWidgetItems.add(bitmap)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        Binder.restoreCallingIdentity(identityToken)
    }

    override fun onDestroy() {}

    override fun getCount(): Int = cursor.count

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)
        rv.setImageViewBitmap(R.id.imageView_widget, mWidgetItems[position])
        val extras = bundleOf(
            FavoritAppWidget.EXTRA_ITEM to position
        )
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)
        rv.setOnClickFillInIntent(R.id.imageView_widget, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = 0

    override fun hasStableIds(): Boolean = false
}