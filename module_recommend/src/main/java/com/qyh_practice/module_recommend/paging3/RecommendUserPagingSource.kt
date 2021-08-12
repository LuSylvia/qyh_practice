package com.qyh_practice.module_recommend.paging3


import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.module_common.utils.LogUtil
import com.qyh_practice.module_recommend.api.RecommendService
import com.qyh_practice.module_recommend.entity.RecommendUserInfo
import kotlinx.coroutines.delay
import kotlin.math.min

class RecommendUserPagingSource(
    private val service: RecommendService,
    private val workcity: Int
) : PagingSource<String, RecommendUserInfo>() {
    companion object {
        /**
         * key的列表，用于获取数据
         */
        val keys: ArrayList<String> = ArrayList()

        val mIds = arrayListOf<String>()
        val mLastIds = arrayListOf<String>()

        const val PAGE_SIZE = 10

        //代表要读key里的哪个数据
        var position: Int = 0
    }


    override fun getRefreshKey(state: PagingState<String, RecommendUserInfo>): String? = null

    //用Load请求数据
    override suspend fun load(params: LoadParams<String>): LoadResult<String, RecommendUserInfo> {

        return try {


                getIds(workcity)
                Log.d("Sylvia-success", "id是${mIds.joinToString(separator = ",")}")

            //从网络读取数据
            delay(3000)
            val userInfos = service.getRecommendList(sids = getRequestIds()).data.list
            delay(3000)
            LoadResult.Page(
                data = userInfos,
                //只能向前
                prevKey = null,
                nextKey = service.getRecommendSids(workcity).data.list.joinToString(separator = ",")
            )

        } catch (e: Exception) {
            //获取数据失败，抛出异常
            LogUtil.e("PagingSource", "分页加载推荐用户信息异常，原因是：${e.message}")
            LoadResult.Error(e)
        }

    }

    suspend fun getIds(workcity: Int) {
        val ids = service.getRecommendSids(workcity).data.list
        val list = mIds.filterNot { it in ids }
        mIds.addAll(list)

    }


    fun getRequestIds(): String {
        if (mIds.isEmpty()) {
            return ""
        }
        mLastIds.clear()
        mLastIds.addAll(mIds.subList(0, min(mIds.size, PAGE_SIZE)))
        return mLastIds.joinToString(separator = ",")
    }


}