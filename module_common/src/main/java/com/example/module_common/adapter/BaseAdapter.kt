package com.example.module_common.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.module_common.R

abstract class BaseAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val VIEW_TYPE_NETWORK_ERROR = -2
        const val VIEW_TYPE_EMPTY = -1
        const val VIEW_TYPE_ITEM = 0
    }

    val mData: ArrayList<T> = ArrayList()
    var empty: Boolean = false
    var networkError: Boolean = false

    var mOnNetworkErrorClickListener: View.OnClickListener? = null

    open fun onRefreshSuccess(list: List<T>?) {
        mData.clear()
        if (list != null) {
            mData.addAll(list)
        }
        empty = mData.isEmpty()
        networkError = false
    }

    fun onRefreshFailed() {
        networkError = mData.isEmpty()
        empty = mData.isEmpty()
    }

    fun onLoadMoreSuccess(list: List<T>?) {
        if (list != null) {
            mData.addAll(list)
        }
    }

    override fun getItemCount(): Int {
        val itemCount = mData.size
        if (itemCount == 0 && (empty || networkError)) {
            return 1
        }
        return itemCount
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0 && mData.size == 0) {
            if (networkError) {
                return VIEW_TYPE_NETWORK_ERROR
            }
            if (empty) {
                return VIEW_TYPE_EMPTY
            }
        }
        return VIEW_TYPE_ITEM
    }

    fun bindSimpleViewHolder(
        holder: SimpleViewHolder,
        imageRes: Int = R.drawable.icon_common_net_error,
        stringRes: Int = R.string.string_common_net_error
    ) {
        holder.itemView.run {
            //findViewById<ImageView>()
        }
    }


}