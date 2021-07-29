package com.qyh_practice.module_recommend.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qyh_practice.module_recommend.R;
import com.qyh_practice.module_recommend.entity.RecommendUserInfo;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RecommendUserAdapter2  extends RecyclerView.Adapter<RecommendUserAdapter2.RecommendUserViewHolder> {
    private List<RecommendUserInfo> userInfos=new ArrayList<>();

    @NonNull
    @NotNull
    @Override
    public RecommendUserViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        RecommendUserViewHolder holder= new RecommendUserViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_recommend_userinfo, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecommendUserViewHolder holder, int position) {
        holder.tv_workcitystr.setText(userInfos.get(position).workCityStr);
        holder.tv_nickname.setText(userInfos.get(position).nickName);
        holder.tv_age.setText(userInfos.get(position).age);
        //TODO:头像的加载用Glide
    }

    @Override
    public int getItemCount() {
        return userInfos.size();
    }


    static class RecommendUserViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_avatar;
        TextView tv_age,tv_workcitystr,tv_nickname;

        public RecommendUserViewHolder(View itemView){
            super(itemView);
            iv_avatar=itemView.findViewById(R.id.iv_avatar);
            tv_age=itemView.findViewById(R.id.tv_age);
            tv_nickname=itemView.findViewById(R.id.tv_nickname);
            tv_workcitystr=itemView.findViewById(R.id.tv_workcitystr);

        }



    }
}
