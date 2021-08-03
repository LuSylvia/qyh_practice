package com.qyh_practice.recommendation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.qyh_practice.databinding.FragmentRecommendationBinding
import com.qyh_practice.module_recommend.adapter.RecommendUserAdapter
import com.qyh_practice.module_recommend.entity.RecommendUserInfo
import com.qyh_practice.module_recommend.viewModel.RecommendViewModel
import kotlinx.coroutines.flow.collect


class RecommendationFragment : Fragment() {
    private lateinit var binding: FragmentRecommendationBinding
    private val viewModel: RecommendViewModel by activityViewModels()
    private lateinit var adapter:RecommendUserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRecommendationBinding.inflate(inflater, container, false)
        val view = binding.root
        return view

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter= context?.let { RecommendUserAdapter(it) }!!
        binding.recyclerView.layoutManager=LinearLayoutManager(activity)
        binding.recyclerView.adapter=adapter

        //TODO:获取workcity，从SharedPrefences拿
        val workcity:Int=10101000


        lifecycleScope.launchWhenCreated {
            viewModel.getPagingData(workcity).collect { pagingData->
                adapter.submitData(pagingData)
            }
        }
        adapter.addLoadStateListener {
            when(it.refresh){
                is LoadState.NotLoading->{

                }
                is LoadState.Loading->{

                }
                is LoadState.Error->{

                }
            }
        }


    }


}