package com.qyh_practice.module_recommend.paging


import com.example.module_common.retrofit.RetrofitManager
import com.qyh_practice.module_recommend.api.RecommendService
import com.qyh_practice.module_recommend.entity.RecommendUserInfo
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

//TODO:继承PagingSource<K,V>
class RecommendUserPagingSource {


//    override fun getRefreshKey(state: PagingState<Int, RecommendUserInfo>): Int? =null
//
//     override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RecommendUserInfo> =null

//         try {
//            runBlocking {
//                val page = params.key ?: 1 // set page 1 as default
//                //val pageSize = params.loadSize
//                val service=RetrofitManager.getService(RecommendService::class.java)
//                val response_ids=async {  service.getRecommendSids(page)}
//                val sids=response_ids.await().data.list
//                if(sids!=null){
//                    //val repoResponse = gitHubService.searchRepos(page, pageSize)
//                    //val repoItems = repoResponse.items
//                    val response_infos=async {  service.getRecommendList(sids .get(0))}
//                    val recommendUserInfo=response_infos.await().data.list
//
//                    val prevKey = if (page > 1) page - 1 else null
//                    val nextKey = if (recommendUserInfo.isNotEmpty()) page + 1 else null
//                    LoadResult.Page(recommendUserInfo, prevKey, nextKey)
//                }
//
//
//            }
//
//        }catch (e:Exception){
//            LoadResult.Error(e)
//        }

        //return null


}