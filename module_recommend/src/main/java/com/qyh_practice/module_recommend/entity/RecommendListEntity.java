package com.qyh_practice.module_recommend.entity;

import com.example.module_common.entity.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

public class RecommendListEntity extends ResponseEntity.Data {
    public boolean hazNext;
    public List<RecommendUserInfo> list;
}
