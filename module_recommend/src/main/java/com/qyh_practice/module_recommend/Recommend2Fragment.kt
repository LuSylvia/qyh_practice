package com.qyh_practice.module_recommend

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.module_common.constants.RouterManager
import com.example.module_common.eventbus.EventBusMessage
import com.qyh_practice.module_recommend.adapter.RecommendUserAdapter
import com.qyh_practice.module_recommend.adapter.TestUserAdapter
import com.qyh_practice.module_recommend.databinding.FragmentRecommend2Binding
import com.qyh_practice.module_recommend.entity.TestUserEntity
import com.qyh_practice.module_recommend.viewModel.RecommendViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@Route(path = RouterManager.FRAGMENT_RECOMMEND)
class Recommend2Fragment : Fragment() {
    private lateinit var binding: FragmentRecommend2Binding
    private val viewModel: RecommendViewModel by activityViewModels()
    private lateinit var adapter: TestUserAdapter
    private lateinit var testUserInfos: MutableList<TestUserEntity>
    private var height: Float = 0.0F
    private var mBeforeStare = AppBarState.IDLE;

    //paging3使用的adapter
    private lateinit var pagingAdapter: RecommendUserAdapter


    enum class AppBarState {
        EXPANDED,//展开
        COLLAPSED,//折叠
        IDLE//展开与折叠的中间状态
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
        binding = FragmentRecommend2Binding.inflate(inflater, container, false)
        val view = binding.root
        return view

    }


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
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onReceiveMsg(msg: EventBusMessage): Int {
        Log.d("Syliva-receive", "收到的消息是${msg.workcity}")
        //TODO:接收workcity，然后开始检索
        return msg.workcity
    }


    @SuppressLint("ResourceAsColor")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //initList()
        //adapter = context?.let { TestUserAdapter(it, testUserInfos) }!!
        pagingAdapter = context?.let { RecommendUserAdapter(it) }!!
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        //binding.recyclerView.adapter = adapter

        binding.recyclerView.adapter = pagingAdapter

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

        lifecycleScope.launch {
            viewModel.getPagingData(workcity).collect { pagingData ->
                //核心方法submitData
                pagingAdapter.submitData(pagingData)
            }
        }
        pagingAdapter.addLoadStateListener {
            when (it.refresh) {
                is androidx.paging.LoadState.Loading -> {
                    //加载中
                    Log.d("Sylvia-loading", "推荐页加载中")
                }
                is androidx.paging.LoadState.NotLoading -> {
                    Log.d("Sylvia-Success", "推荐页加载完成")

                }
                is androidx.paging.LoadState.Error -> {
                    Log.d("Sylvia-Error", "推荐页网络异常")
                    binding.recyclerView.visibility = View.GONE

                }
            }
        }

//        viewModel.getRecommendSids(workcity)
//        viewModel.loadState.observe(viewLifecycleOwner, Observer {
//            when (it) {
//                LoadState.SUCCESS -> {
//                    Log.d("Sylvia-Success", "推荐页成功了")
//                    viewModel.recommendPeopleLiveData.observe(viewLifecycleOwner, Observer {
//                        viewModel.recommendPeopleLiveData.value?.let { it1 ->
//                            adapter.setRecommendList(
//                                it1
//                            )
//                            adapter.notifyDataSetChanged()
//                        }
//                        Log.d("Sylvia-Success", "头像是" + it.list.get(0).avatar)
//                    })
//                    return@Observer
//                }
//                LoadState.EMPTY -> {
//                    Log.d("Sylvia-Empty", "推荐页为空")
//                    return@Observer
//                }
//                LoadState.FAIL -> {
//                    Log.d("Sylvia-Fail", "推荐页加载失败")
//                    return@Observer
//                }
//
//            }
//        })


        /**
         * Add a listener that will be called when the offset of this {@link AppBarLayout} changes.
         *
         * @param listener The listener that will be called when the offset changes.]
         * @see #removeOnOffsetChangedListener(OnOffsetChangedListener)
         */
//        binding.appBarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset:Int ->
//            {
//                if(verticalOffset==0){
//                    //进入展开状态
//                    if(mBeforeStare==AppBarState.IDLE){
//                        //TODO:退出
//                    }else{
//                        mBeforeStare=AppBarState.IDLE
//                        binding.recommendHeaderView.alpha=1.0f
//                        binding.ivTopBg.alpha=1.0f
//                        binding.recommendCollapsingToolbarLayout.setBackgroundResource(R.drawable.ic_funnydate_white)
//
//                    }
//                }else if(Math.abs(verticalOffset)>height){
//                    //进入折叠状态
//                    if(mBeforeStare==AppBarState.COLLAPSED){
//                        //TODO:退出
//                    }else{
//                        mBeforeStare=AppBarState.COLLAPSED
//                        binding.recommendHeaderView.alpha=1.0f
//                        binding.ivTopBg.alpha=1.0f
//                        binding.recommendCollapsingToolbarLayout.setBackgroundResource(R.drawable.ic_funnydate_color)
//                    }
//
//
//                }else{
//                    //进入中间状态
//                    if(mBeforeStare==AppBarState.IDLE){
//
//                    }else{
//                        binding.frameLayoutTitle.setBackgroundColor(R.color.white)
//                    }
//
//                    mBeforeStare=AppBarState.EXPANDED
//                    changeTitleBarBg(verticalOffset, height)
//                }
//            }
//        }


    }
}