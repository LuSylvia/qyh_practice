package com.qyh_practice.module_recommend.entity;

import java.util.List;

public class RecommendUserInfo {

    public int age;

    public String avatar;
    /**
     * 性别
     */
    public int gender;

    public String desc;
    /**
     * 0:无，1相亲中，2专属相亲
     */
    public int liveType;
    /**
     * 是否是VIP
     */
    public boolean isVIP;
    /**
     * 是否实名
     */
    public boolean isRealName;

    public String nickName;
    /**
     * 是否有关系
     */
    public boolean hasRelation;

    public int userId;

    public String workCityStr;
    /**
     * 0不在线，1 1小时内，2当天活跃
     */
    public int onlineType;
    /**
     * 是否是红娘
     */
    public boolean isMatchMaker;
    /**
     * 所在房间的主播ID
     */
    public int anchorid;

    public boolean defaultAvatar;
    /**
     * 是否是新用户
     */
    public boolean isNewUser;

    public List<MediaList> mediaList;

    /**
     * 3张媒体资源
     */
    private static class MediaList{
        /**
         * 媒体类型。0相册图片，1动态图片，2动态视频
         */
        public String mediaType;
        /**
         * 媒体路径
         */
        public String mediaURL;
    }
}
