package com.qyh_practice.recommendation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.qyh_practice.databinding.FragmentRecommendationBinding
import com.qyh_practice.module_recommend.entity.RecommendUserInfo
import com.qyh_practice.module_recommend.viewModel.RecommendViewModel


class RecommendationFragment : Fragment() {
    private lateinit var binding: FragmentRecommendationBinding
    val viewModel: RecommendViewModel by activityViewModels()
    private val userInfos = ArrayList<RecommendUserInfo>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRecommendationBinding.inflate(inflater, container, false)
        val view = binding.root
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //TODO:将appBarConfig里的workcity传过来
        val workcity: Int = 0
        viewModel.getRecommendSids(workcity)
        //initUserinfos()

//        val layoutManager=LinearLayoutManager(activity)
//        binding.recyclerView.layoutManager=layoutManager
//        val adapter=RecommendUserAdapter2(userInfos)
//        binding.recyclerView.adapter=adapter

    }

    private fun initUserinfos() {
        val mediaList = RecommendUserInfo().mediaList

        //mediaList.add()
        repeat(2) {
            userInfos.add(
                RecommendUserInfo(
                    18, "123", 1, "123", 1, false, true, "just", false, 123, "ShenZhen", 0,
                    false, 1, false, true, mediaList
                )
            )
        }
    }


}