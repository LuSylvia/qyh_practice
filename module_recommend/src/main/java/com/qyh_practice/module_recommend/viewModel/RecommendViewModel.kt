package com.qyh_practice.module_recommend.viewModel

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.module_common.BaseViewModel
import com.qyh_practice.module_recommend.entity.RecommendUserInfo
import com.qyh_practice.module_recommend.repository.RecommendUserRepository
import kotlinx.coroutines.flow.Flow

class RecommendViewModel : BaseViewModel() {
    /**
     * 获取推荐用户数据 2.0版
     * 使用pagingData获取数据
     */
    fun getPagingData(workcity: Int): Flow<PagingData<RecommendUserInfo>> {
        return RecommendUserRepository.instance.getPagingData(workcity = workcity)
            .cachedIn(viewModelScope)
    }


}