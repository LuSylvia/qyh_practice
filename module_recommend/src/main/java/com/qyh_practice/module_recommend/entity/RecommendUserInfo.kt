package com.qyh_practice.module_recommend.entity

import com.example.module_common.entity.BaseEntity


open class NearByEntity : BaseEntity() {
    override fun uniqueKey(): Array<String>? {
        return arrayOf()
    }

}

/**
 * 推荐基类
 */
open class RecommentBaseEntity : NearByEntity()


data class RecommendUserInfo(
    val age: Int,
    val avatar: String,
    /**
     * 性别
     */
    val gender: Int,
    val desc: String,
    /**
     * 0:无，1相亲中，2专属相亲
     */
    val liveType: Int,
    /**
     * 是否是VIP
     */
    val isVIP: Boolean,
    /**
     * 是否实名
     */
    val isRealName: Boolean,
    val nickName: String,
    /**
     * 是否有关系
     */
    val hazRelation: Boolean,
    val userId: Int,
    val workCityStr: String,
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
    val anchorId: Int,
    val defaultAvatar: Boolean = false,
    /**
     * 是否是新用户
     */
    val isNewUser: Boolean = false,
    val mediaList: List<MediaList>? = null

) : RecommentBaseEntity()

data class MediaList(
    /**
     * 媒体类型。0相册图片，1动态图片，2动态视频
     */
    val mediaType: Int,
    /**
     * 媒体路径
     */
    val mediaURL: String?
) : RecommentBaseEntity()
