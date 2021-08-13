package com.qyh_practice.module_recommend.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.qyh_practice.module_recommend.R
import com.qyh_practice.module_recommend.entity.RecommendUserInfo


class RecommendUserAdapter(val context: Context) :
    PagingDataAdapter<RecommendUserInfo, RecommendUserAdapter.RecommendUserViewHolder>(COMPARATOR) {
    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<RecommendUserInfo>() {
            override fun areItemsTheSame(
                oldItem: RecommendUserInfo,
                newItem: RecommendUserInfo
            ): Boolean {
                return oldItem.userId == newItem.userId
            }

            /**
             * 比较2个item的内容是否相同
             */
            override fun areContentsTheSame(
                oldItem: RecommendUserInfo,
                newItem: RecommendUserInfo
            ): Boolean {
                return oldItem == newItem
            }
        }
    }


    class RecommendUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iv_avatar: ImageView = itemView.findViewById(R.id.iv_avatar)
        val tv_workcity_str: TextView = itemView.findViewById(R.id.tv_workcitystr)
        val tv_nickName: TextView = itemView.findViewById(R.id.tv_nickname)
        val tv_age: TextView = itemView.findViewById(R.id.tv_age)
    }

    override fun onBindViewHolder(holder: RecommendUserViewHolder, position: Int) {
        //paging内置了getItem函数
        val recommendUserInfo = getItem(position)
        recommendUserInfo?.let {
            holder.tv_age.text = recommendUserInfo.age.toString()
            holder.tv_workcity_str.text = recommendUserInfo.workCityStr
            holder.tv_nickName.text = recommendUserInfo.nickName

            //用Glide加载头像
            Glide.with(context).
            load(recommendUserInfo.avatar).
            circleCrop().
            into(holder.iv_avatar);
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendUserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recommend_userinfo, parent, false)
        return RecommendUserViewHolder(view)

    }


}