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
import com.qyh_practice.module_recommend.databinding.FragmentRecommend2Binding
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
    private var height: Float = 0.0F
    private var mBeforeStare = AppBarState.IDLE;


    private var workcity: Int = 0

    //paging3使用的adapter
    private lateinit var pagingAdapter: RecommendUserAdapter


    enum class AppBarState {
        EXPANDED,//展开
        COLLAPSED,//折叠
        IDLE//展开与折叠的中间状态
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
    fun onReceiveMsg(msg: EventBusMessage) {
        Log.d("Syliva-receive", "fragment,eventbus收到的消息是${msg.workcity}")
        //接收workcity，记录
        workcity = msg.workcity
    }


    @SuppressLint("ResourceAsColor")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        pagingAdapter = context?.let { RecommendUserAdapter(it) }!!
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)


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
                    Log.d("Sylvia-loading", "fragment,推荐页加载中")
                }
                is androidx.paging.LoadState.NotLoading -> {
                    Log.d("Sylvia-Success", "fragment,推荐页加载完成")

                }
                is androidx.paging.LoadState.Error -> {
                    Log.d("Sylvia-Error", "fragment,推荐页网络异常")
                    binding.recyclerView.visibility = View.GONE

                }
            }
        }



    }

    private fun changeTitleBarBg(verticalOffset: Int, height: Float) {

    }
}