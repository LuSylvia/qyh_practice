package com.qyh_practice.module_recommend.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.qyh_practice.module_recommend.R
import com.qyh_practice.module_recommend.entity.RecommendListEntity
import com.qyh_practice.module_recommend.entity.TestUserEntity

class TestUserAdapter(val context: Context, val testUserList: List<TestUserEntity>) :
    RecyclerView.Adapter<TestUserAdapter.TestUserViewHolder>() {
    class TestUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iv_avatar: ImageView = itemView.findViewById(R.id.iv_avatar)
        val tv_workcity_str: TextView = itemView.findViewById(R.id.tv_workcitystr)
        val tv_nickName: TextView = itemView.findViewById(R.id.tv_nickname)
        val tv_age: TextView = itemView.findViewById(R.id.tv_age)

    }

    var recommendListEntity:RecommendListEntity?=null


    fun setRecommendList(recommendListEntity: RecommendListEntity){
        this.recommendListEntity=recommendListEntity;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestUserViewHolder {
        val viewHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recommend_userinfo, parent, false)
        return TestUserViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: TestUserViewHolder, position: Int) {
        //set value for each viewHolder
        if(recommendListEntity==null){
            val testUserInfo = testUserList[position]

            holder.tv_age.setText(testUserInfo.age.toString())
            holder.tv_nickName.setText(testUserInfo.nickname)
            holder.tv_workcity_str.setText(testUserInfo.workcity_str)
            //头像读取用Glide
            Glide.with(context)
                .load(R.drawable.girl)
                .override(100, 100)
                .circleCrop()
                .into(holder.iv_avatar);
        }else{
            val userInfo= recommendListEntity!!.list[position]

            holder.tv_age.setText(userInfo.age.toString())
            holder.tv_nickName.setText(userInfo.nickName)
            holder.tv_workcity_str.setText(userInfo.workCityStr)
            //头像读取用Glide
            Glide.with(context)
                .load(userInfo.avatar)
                .override(100, 100)
                .circleCrop()
                .into(holder.iv_avatar);


        }



    }

    override fun getItemCount(): Int {
        if (testUserList.isEmpty()) {
            return 0
        }
        return testUserList.size
    }


}