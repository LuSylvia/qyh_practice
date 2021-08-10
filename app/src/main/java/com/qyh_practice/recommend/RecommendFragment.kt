package com.qyh_practice.recommend

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.qyh_practice.R
import com.qyh_practice.databinding.FragmentRecommendBinding
import com.qyh_practice.module_recommend.adapter.TestUserAdapter
import com.qyh_practice.module_recommend.entity.TestUserEntity
import com.qyh_practice.module_recommend.viewModel.RecommendViewModel


class RecommendFragment : Fragment() {
    private lateinit var binding: FragmentRecommendBinding
    private val viewModel: RecommendViewModel by activityViewModels()
    private lateinit var adapter: TestUserAdapter
    private lateinit var testUserInfos: MutableList<TestUserEntity>

    enum class AppBarState {
        EXPANDED,
        COLLAPSED,
        IDLE
    }


    fun initList() {
        testUserInfos = ArrayList()

        val testUserEntity1 = TestUserEntity("", "龙曦", 23, "北京")
        val testUserEntity2 = TestUserEntity("", "Sylvia", 22, "重庆")
        val testUserEntity3 = TestUserEntity("", "Lu123F", 21, "上海")
        val testUserEntity4 = TestUserEntity("", "好！很有精神！", 20, "天津")
        val testUserEntity5 = TestUserEntity("", "合", 20, "天津")
        val testUserEntity6 = TestUserEntity("", "逆转", 20, "天津")
        val testUserEntity7 = TestUserEntity("", "探寻", 20, "天津")
        val testUserEntity8 = TestUserEntity("", "事件分发", 20, "天津")

        testUserInfos.add(testUserEntity1)
        testUserInfos.add(testUserEntity2)
        testUserInfos.add(testUserEntity3)
        testUserInfos.add(testUserEntity4)
        testUserInfos.add(testUserEntity5)
        testUserInfos.add(testUserEntity6)
        testUserInfos.add(testUserEntity7)
        testUserInfos.add(testUserEntity8)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRecommendBinding.inflate(inflater, container, false)
        val view = binding.root
        return view

    }


    @SuppressLint("ResourceAsColor")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initList()
        adapter = context?.let { TestUserAdapter(it, testUserInfos) }!!
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.adapter = adapter


        //添加分隔线
        val itemDecoration = DividerItemDecoration(
            binding.recyclerView.getContext(),
            (binding.recyclerView.layoutManager as LinearLayoutManager).getOrientation()
        )
        binding.recyclerView.addItemDecoration(itemDecoration)

//        binding.recommendAppbarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
//            {
//                if (verticalOffset == 0) {
//
//                }
//            }
//        }

        //折叠栏属性设置
        binding.recommendCollapsingToolbarLayout.title = "仿趣约会"
        with(binding.recommendCollapsingToolbarLayout) {
            setExpandedTitleColor(R.color.black)
            setCollapsedTitleTextColor(R.color.white)
        }


        //TODO:获取到tv_main_title，隐藏标题栏


        //TODO:获取workcity，从SharedPrefences拿
        val workcity: Int = 10101000


    }


}