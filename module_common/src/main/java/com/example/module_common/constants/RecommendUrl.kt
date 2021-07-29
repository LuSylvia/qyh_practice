package com.example.module_common.constants

class RecommendUrl :BaseUrl(){
    companion object{
        /**
         * 获取推荐列表
         */
        const val  GET_RECOMMEND_LIST="/recommend/recommendListPlus.do";
        /**
         * 获取推荐ID
         */
        const val  GET_RECOMMEND_ID="/recommend/recommendSids.do";
    }

}