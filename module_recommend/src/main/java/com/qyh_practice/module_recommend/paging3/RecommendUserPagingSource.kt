package com.qyh_practice.module_recommend.paging3


import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.module_common.utils.LogUtil
import com.qyh_practice.module_recommend.api.RecommendService
import com.qyh_practice.module_recommend.entity.RecommendUserInfo
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
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
    override suspend fun load(params: LoadParams<String>): LoadResult<String, RecommendUserInfo> {

        return try {

            val userInfos = service.getRecommendList(sids = getRequestIds()).data.list
            Log.d("Sylvia-success", "头像是${userInfos[0].avatar}")
            LoadResult.Page(
                data = userInfos,
                //只能向前
                prevKey = null,
                nextKey = runBlocking { async { getRequestIds() }.await() }
            )

        } catch (e: Exception) {
            //获取数据失败，抛出异常
            LogUtil.e("Sylvia-Exception", "分页加载推荐用户信息异常，原因是：${e.message}")
            LoadResult.Error(e)
        }

    }

    suspend fun getIds(workcity: Int) {
        val ids = service.getRecommendSids(workcity).data.list
        val list = mIds.filterNot { it in ids }
        mIds.addAll(list)

    }


    suspend fun getRequestIds(): String {
        runBlocking {
            getIds(workcity)
        }
        mLastIds.clear()
        mLastIds.addAll(mIds.subList(0, min(mIds.size, PAGE_SIZE)))
        return mLastIds.joinToString(separator = ",")
    }


}