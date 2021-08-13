package com.qyh_practice.module_recommend.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.module_common.entity.BaseRepository
import com.example.module_common.retrofit.RetrofitManager
import com.qyh_practice.module_recommend.api.RecommendService
import com.qyh_practice.module_recommend.entity.RecommendUserInfo
import com.qyh_practice.module_recommend.paging3.RecommendUserPagingSource
import kotlinx.coroutines.flow.Flow

class RecommendUserRepository private constructor() : BaseRepository() {
    companion object {
        /**
         * 单例
         */
        val instance = Singleton.singleton

        /**
         * 一页只有10条用户数据，用完就该重新获取了
         */
        private const val PAGE_SIZE = 28

        /**
         * 预刷新的距离，距离最后一个item多远时加载数据
         */
        private const val PREFETCHDISTANCE = 2


        private val recommendService = RetrofitManager.getService<RecommendService>()
    }

    private object Singleton {
        val singleton = RecommendUserRepository()
    }


    fun getPagingData(workcity: Int): Flow<PagingData<RecommendUserInfo>> {
        //TODO:用类似async、await的技巧，让getIds方法先运行完
        //TODO:必须修改完用户retSids后，再调用return Pager（）.flow
        //注意：runBlocking函数不仅会阻塞当前协程，还会阻塞线程
        //如果在主线程程执行，有可能导致OOM异常
        //为了安全起见，应该使用coroutineScope函数（该函数只会阻塞当前协程）
        return Pager(
            config = PagingConfig(PAGE_SIZE, PREFETCHDISTANCE),
            pagingSourceFactory = {
                RecommendUserPagingSource(
                    service = recommendService,
                    workcity = workcity
                )
            }
        ).flow
    }


}