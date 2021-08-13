package com.qyh_practice.module_recommend.paging3


import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.module_common.utils.LogUtil
import com.qyh_practice.module_recommend.api.RecommendService
import com.qyh_practice.module_recommend.entity.RecommendUserInfo
import kotlinx.coroutines.*
import kotlin.math.min

class RecommendUserPagingSource(
    private val service: RecommendService,
    private val workcity: Int
) : PagingSource<String, RecommendUserInfo>() {
    companion object {
        val mIds = arrayListOf<String>()
        val mLastIds = arrayListOf<String>()
        const val PAGE_SIZE = 10
    }


    override fun getRefreshKey(state: PagingState<String, RecommendUserInfo>): String? = null

    //用Load请求数据
    //LoadResult<K,V>
    //其中，K为获取页面item时的数据类型，此处为拼接后的String sidsStr
    //V是每一项数据对应的对象类型，此处为RecommendUserInfo
    override suspend fun load(params: LoadParams<String>): LoadResult<String, RecommendUserInfo> {
//        val coroutineScope= CoroutineScope(Dispatchers.IO)
//        coroutineScope.launch {
//
//        }

        return try {
            val sidList = service.getRecommendSids(workcity).data.list
            val sidsStr = sidList.joinToString(separator = ",")
            val userInfos = service.getRecommendList(sidsStr).data.list
            Log.d("Sylvia-success", "pagingsource，头像是${userInfos[0].avatar}")



            LoadResult.Page(
                data = userInfos,
                //只能向前
                prevKey = null,
                nextKey = sidsStr  //设为null会在获取28条数据后，无法获取新数据
            )

        } catch (e: Exception) {
            //获取数据失败，抛出异常
            LogUtil.e("Sylvia-Exception", "pagingsource,分页加载推荐用户信息异常，原因是：${e.message}")
            LoadResult.Error(e)
        }

    }




}