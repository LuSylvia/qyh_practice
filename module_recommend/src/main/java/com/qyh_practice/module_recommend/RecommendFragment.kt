package com.qyh_practice.module_recommend

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.module_common.constants.RouterManager
import com.example.module_common.eventbus.EventBusMessage
import com.qyh_practice.module_recommend.adapter.TestUserAdapter
import com.qyh_practice.module_recommend.databinding.FragmentRecommendBinding
import com.qyh_practice.module_recommend.entity.TestUserEntity
import com.qyh_practice.module_recommend.viewModel.RecommendViewModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@Route(path = RouterManager.FRAGMENT_RECOMMEND)
class RecommendFragment : Fragment() {
    private lateinit var binding:FragmentRecommendBinding
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

    /**
     *
     */
    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }


    /**
     * 注意，要用eventbus接受消息的话，该方法必须是用public修饰
     * 并且收到消息的前提是，必须先调用EventBus.getDefault().register方法
     * 否则，应采用粘性事件，发送方选择postSticky方法，接受方设置sticky=true
     */
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    fun onReceiveMsg(msg: EventBusMessage){
        Log.d("Syliva-receive","收到的消息是${msg.userID}")
        //TODO:接受workcity，然后开始检索
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



        //折叠栏属性设置
        binding.recommendCollapsingToolbarLayout.title = "仿趣约会"
        with(binding.recommendCollapsingToolbarLayout) {
            setExpandedTitleColor(R.color.black)
            setCollapsedTitleTextColor(R.color.white)
        }


        //TODO:获取workcity，从SharedPrefences拿
        val workcity: Int = 10101000
    }
}