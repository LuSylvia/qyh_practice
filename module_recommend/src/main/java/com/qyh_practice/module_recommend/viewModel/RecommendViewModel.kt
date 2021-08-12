package com.qyh_practice.module_recommend.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import com.example.module_common.BaseViewModel
import com.example.module_common.entity.LoadState
import com.example.module_common.retrofit.RetrofitManager
import com.example.module_common.retrofit.launch
import com.qyh_practice.module_recommend.api.RecommendService
import com.qyh_practice.module_recommend.entity.RecommendListEntity
import com.qyh_practice.module_recommend.entity.RecommendUserInfo
import com.qyh_practice.module_recommend.repository.RecommendUserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow

class RecommendViewModel : BaseViewModel() {

    val loadState = MutableLiveData<LoadState>()

    val recommendPeopleLiveData = MutableLiveData<RecommendListEntity>()


    /**
     * 获取推荐用户数据 1.0版
     */
    fun getRecommendList(sids: String) {
        launch({
            loadState.value = LoadState.LOADING
            val recommendService = RetrofitManager.getService(RecommendService::class.java)
            val response = async { recommendService.getRecommendList(sids) }

            if (!response.await().isError) {
                //获取列表成功
                //判断列表数据是否为空
                if (response.await().data.list?.isEmpty() == true) {
                    loadState.value = LoadState.EMPTY
                    Log.d(
                        "Sylvia-ViewModel",
                        "推荐页成！头像是${response.await().data.list[0].avatar}"
                    )
                } else {
                    loadState.value = LoadState.SUCCESS
                    //给LiveData填充数据
                    recommendPeopleLiveData.value =
                        response.await().data
                    Log.d(
                        "Sylvia-ViewModel",
                        "推荐页成！头像是${response.await().data.list[0].avatar}"
                    )
                }

            } else {
                //获取失败
                loadState.value = LoadState.FAIL
                Log.d("Sylvia-ViewModel", "推荐页 获取列表失败，提示是${response.await().errorMessage}")
            }


        }, {
            loadState.value = LoadState.FAIL
        })
    }


    fun getRecommendSids(workcity: Int) {
        launch({
            val recommendService = RetrofitManager.getService(RecommendService::class.java)
            val response = async { recommendService.getRecommendSids(workcity) }

            if (!response.await().isError && !response.await().data.list.isEmpty()) {
                //获取sids成功
                //直接调用getList函数，获取具体的推荐用户信息
                val sids = response.await().data.list.joinToString(separator = ",")
                getRecommendList(sids)
                //getRecommendList(sids)
            }
        }, {
            //异常被捕获，打印信息
            Log.d("RecommendViewModel", "获取推荐用户失败，提示是${it.message}")
        })


    }

    /**
     * 获取推荐用户数据 2.0版
     * 使用pagingData获取数据
     */
    fun getPagingData(workcity: Int): Flow<PagingData<RecommendUserInfo>> {
        Log.d("Sylvia-loading", "workcity是${workcity}")
        return RecommendUserRepository.instance.getPagingData(workcity = workcity)
    }


}