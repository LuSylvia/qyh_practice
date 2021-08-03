package com.qyh_practice.module_recommend.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.module_common.entity.BaseRepository
import com.example.module_common.retrofit.RetrofitManager
import com.example.module_common.utils.LogUtil
import com.qyh_practice.module_recommend.api.RecommendService
import com.qyh_practice.module_recommend.entity.RecommendUserInfo
import com.qyh_practice.module_recommend.paging.RecommendUserPagingSource
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking

class RecommendUserRepository private constructor() : BaseRepository() {
    companion object {
        /**
         * 单例
         */
        val instance = Singleton.singleton

        /**
         * 一页只有8条用户数据，用完8条后就该重新获取了
         */
        private const val PAGE_SIZE = 8

        private var retSids: String = ""

        private val recommendService = RetrofitManager.getService<RecommendService>()
    }

    private object Singleton {
        val singleton = RecommendUserRepository()
    }


    suspend fun getIds(workcity: Int) {
        coroutineScope {
            val response = recommendService.getRecommendSids(workcity = workcity)
            if (!response.isError) {
                val sids: Array<out String>? = response.data.list
                //取出一次网络请求获取到的所有sid，拼接到一个字符串里
                if (sids != null) {

                    //初始化
                    retSids = ""
                    println("测试执行开始，retSids=${retSids}")
                    for (sid in sids) {
                        retSids += sid + ","
                    }
                    println("测试执行完成,retSids=${retSids}")
                }


            }
        }

    }

    suspend fun getPagingData(workcity: Int): Flow<PagingData<RecommendUserInfo>> {
        //TODO:用类似async、await的技巧，让getIds方法先运行完
        //TODO:修改完用户retSids后，再调用return Pager（）.flow
        //注意：runBlocking函数不仅会阻塞当前协程，还会阻塞线程
        //如果在主线程程执行，有可能导致OOM异常
        //为了安全起见，应该使用coroutineScope函数（该函数只会阻塞当前协程）
        coroutineScope {
            async {  getIds(workcity)}.await()
            LogUtil.d("RecommendUserRepository","消息是+${retSids}")
        }
        return Pager(
            config = PagingConfig(PAGE_SIZE),
            pagingSourceFactory = {
                RecommendUserPagingSource(
                    service = recommendService,
                    sids = retSids
                )
            }
        ).flow
    }


}