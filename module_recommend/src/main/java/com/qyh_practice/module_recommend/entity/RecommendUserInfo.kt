package com.qyh_practice.module_recommend.entity

import com.example.module_common.entity.BaseEntity
import java.io.Serializable

data class RecommendUserInfo(
    val age: Int,
    val avatar: String? = null,
    /**
     * 性别
     */
    val gender: Int,
    val desc: String? = null,
    /**
     * 0:无，1相亲中，2专属相亲
     */
    val liveType: Int = 0,
    /**
     * 是否是VIP
     */
    val isVIP: Boolean = false,
    /**
     * 是否实名
     */
    val isRealName: Boolean = false,
    val nickName: String? = null,
    /**
     * 是否有关系
     */
    val hasRelation: Boolean = false,
    val userId: Int = 0,
    val workCityStr: String? = null,
    /**
     * 0不在线，1 1小时内，2当天活跃
     */
    val onlineType: Int = 0,
    /**
     * 是否是红娘
     */
    val isMatchMaker: Boolean = false,
    /**
     * 所在房间的主播ID
     */
    val anchorid: Int,
    val defaultAvatar: Boolean = false,
    /**
     * 是否是新用户
     */
    val isNewUser: Boolean = false,
    val mediaList: List<MediaList>? = null

):Serializable

data class MediaList(
    /**
     * 媒体类型。0相册图片，1动态图片，2动态视频
     */
    val mediaType: Int,
    /**
     * 媒体路径
     */
    val mediaURL: String?
):BaseEntity(){
    override fun uniqueKey(): Array<String>?=null
}
