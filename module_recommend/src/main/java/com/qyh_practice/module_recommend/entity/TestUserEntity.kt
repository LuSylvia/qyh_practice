package com.qyh_practice.module_recommend.entity

import com.example.module_common.entity.ResponseEntity
import java.io.Serializable

data class TestUserEntity(
    val avatar_url:String,
    val nickname:String,
    val age:Int,
    val workcity_str:String
) :ResponseEntity.Data(),Serializable{

}