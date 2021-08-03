package com.qyh_practice.module_recommend.paging


import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.module_common.utils.LogUtil
import com.qyh_practice.module_recommend.api.RecommendService
import com.qyh_practice.module_recommend.entity.RecommendUserInfo
import java.lang.IllegalArgumentException

//TODO:继承PagingSource<K,V>
class RecommendUserPagingSource(
    private val service: RecommendService,
    private val sids:String
):PagingSource<String,RecommendUserInfo>() {
    companion object{
        /**
         * key的列表，用于获取数据
         */
        val keys:ArrayList<String> =ArrayList()
        //代表要读key里的哪个数据
        var position:Int=0
    }


    override fun getRefreshKey(state: PagingState<String, RecommendUserInfo>): String? =null

    //用Load请求数据
    override suspend fun load(params: LoadParams<String>): LoadResult<String, RecommendUserInfo> {
        //TODO("Not yet implemented")
        return try{
            //只要成功拿到了用户id集合，就修改keys线性表和当前页数position
            if(!sids.equals("")){
                keys.add(sids)
                position++
            }else{
                throw IllegalArgumentException("用户id集合异常！")
            }
            //从网络读取数据
            val userInfos=service.getRecommendList(sids = keys[position]).data.userInfoList

            LoadResult.Page(
                data =userInfos,
                prevKey = if(position>0) keys[--position] else null,
                nextKey = if(position!=keys.size-1) keys[++position] else null
            )

        }catch (e:Exception){
            //获取数据失败，抛出异常
            LogUtil.e("PagingSource","分页加载推荐用户信息异常，原因是：${e.message}")
            LoadResult.Error(e)
        }

    }



}