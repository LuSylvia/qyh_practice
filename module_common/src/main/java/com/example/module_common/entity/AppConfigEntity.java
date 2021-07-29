package com.example.module_common.entity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AppConfigEntity extends ResponseEntity.Data {
    /**
     * 审核通过的头像
     */
    public String avatar;

    public ArrayList<ConfigFlagEntity>flagList;
    /**
     * 性别
     */
    public int gender;
    /**
     * 0，无头像
     * 1，有头像
     */
    public int haveAvatar;
    /**
     * 年龄
     */
    public int age;
    /**
     * 省份
     */
    public String workcityStr;
    /**
     * 省份code
     */
    public int workcity;

    public String nickName;

    public int userId;
    /**
     * 万象优图访问域名
     */
    public int cosimageDomain;
    /**
     * 加群所需要的玫瑰数
     */
    public int joinGroupCostRoseNum;
    /**
     * 进入相亲所需玫瑰花数量
     */
    public int applyMeetCostRoseNum;
    /**
     * 好友申请所需玫瑰数
     */
    public int applyFriendCostRoseNum;
    /**
     * 直播和相亲记录带进去的，加好友消耗比心数量
     */
    public int applyFriendCostBiXinNum;
    /**
     * 是否培训中
     */
    public boolean matchmakerTraining;
    /**
     * 消息列表页推荐阈值
     */
    public Integer messageLiveRecommendThreshold;
    /**
     * 1.头像 2.手机 3.微信,4.直播推荐
     */
    public int[] interceptList;
    /**
     * 是否白名单
     */
    public boolean isWhiteUser;
    /**
     * AppConfig的开关
     */
    public BussinessNeedSwitches businessNeedSwitches;


    private static class BussinessNeedSwitches extends BaseEntity{
        /**
         * 推荐页2.0
         */
        public boolean recommendOpt;
        /**
         * 是否打开主体更换提示
         */
        public boolean popServiceChangeWin;

        @NotNull
        @Override
        public String[] uniqueKey() {
            return new String[0];
        }
    }


}
