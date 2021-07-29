package com.qyh_practice.module_recommend.api

import com.example.module_common.constants.RecommendUrl
import com.example.module_common.entity.ResponseEntity
import com.qyh_practice.module_recommend.entity.RecommendListEntity
import com.qyh_practice.module_recommend.entity.RecommendSidsEntity
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RecommendService {

    @FormUrlEncoded
    @POST(RecommendUrl.GET_RECOMMEND_ID)
    suspend fun getRecommendSids(@Field("workcity")workcity:Int):ResponseEntity<RecommendSidsEntity>


    @FormUrlEncoded
    @POST(RecommendUrl.GET_RECOMMEND_LIST)
    suspend fun getRecommendList(@Field("sids")sids:String):ResponseEntity<RecommendListEntity>
}