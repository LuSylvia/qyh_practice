package com.qyh_practice.module_recommend.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.module_common.BaseViewModel
import com.example.module_common.entity.LoadState
import com.example.module_common.retrofit.RetrofitManager
import com.example.module_common.retrofit.launch
import com.qyh_practice.module_recommend.api.RecommendService
import com.qyh_practice.module_recommend.entity.RecommendUserInfo
import kotlinx.coroutines.async

class RecommendViewModel:BaseViewModel() {
    val loadState = MutableLiveData<LoadState>()

    val recommendPeopleLiveData=MutableLiveData<ArrayList<RecommendUserInfo>>()

    fun getRecommendList(sids:String){
        launch({
            loadState.value=LoadState.LOADING
            val recommendService=RetrofitManager.getService(RecommendService::class.java)
            val response=async { recommendService.getRecommendList(sids) }
            val errorMsg:String=response.await().errorMessage
            if(("").equals(errorMsg)){
                //获取列表成功
                //判断列表数据是否为空
                if(response.await().data.list.isEmpty()){
                    loadState.value=LoadState.EMPTY
                }else{
                    loadState.value=LoadState.SUCCESS
                    //给LiveData填充数据
                    recommendPeopleLiveData.value= response.await().data.list as ArrayList<RecommendUserInfo>?
                }

            }else{
                //获取失败
                loadState.value=LoadState.FAIL
                Log.d("RecommendViewModel","获取列表失败，提示是${errorMsg}")
            }


        },{
            loadState.value=LoadState.FAIL
        })
    }


    fun getRecommendSids(workcity:Int){
        launch({
            val recommendService=RetrofitManager.getService(RecommendService::class.java)
            val response=async { recommendService.getRecommendSids(workcity) }
            val errorMsg=response.await().errorMessage
            if(errorMsg.equals("")){
                //获取sids成功
                //直接调用getList函数，获取具体的推荐用户信息
                val sids=response.await().data.list
                for(sid in sids){
                    getRecommendList(sid)
                }
            //getRecommendList(sids)

            }


        },{
            //异常被捕获，打印信息
            Log.d("RecommendViewModel","获取推荐用户失败，提示是${it.message}")
        })


    }

}