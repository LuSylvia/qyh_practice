package com.qyh_practice.module_recommend.entity;

import com.example.module_common.entity.ResponseEntity;

//推荐
public class RecommendSidsEntity extends ResponseEntity.Data {

    public boolean hasNext;
    /**
     * sid列表
     */
    public String[] list;
    /**
     * 刷新时间间隔，毫秒
     */
    public int refreshInterval;

    public String scoreDetail;

}
