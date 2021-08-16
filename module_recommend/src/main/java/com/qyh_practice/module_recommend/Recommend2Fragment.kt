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
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.BaseOnOffsetChangedListener
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
    private var height: Float = 272f
    private var mBeforeStare = AppBarState.IDLE;


    private var workcity: Int = 0

    //paging3使用的adapter
    private lateinit var pagingAdapter: RecommendUserAdapter


    enum class AppBarState {
        EXPANDED,//toolbar没完全收起，这时应该是被用户用手指按着的状态
        COLLAPSED,//折叠完成，代表用户已经滑到下面去了，这个toolbar该收起来了
        IDLE//空闲，最初啥都没动就是该状态，toolbar自然展开
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
        //动态修改appBarLayout透明度和颜色
        binding.appBarLayout.addOnOffsetChangedListener(object :AppBarLayout.OnOffsetChangedListener{
            /**
             * Called when the {@link AppBarLayout}'s layout offset has been changed. This allows child
             * views to implement custom behavior based on the offset (for instance pinning a view at a
             * certain y value).
             *
             * @param appBarLayout the {@link AppBarLayout} which offset has changed
             * @param verticalOffset the vertical offset for the parent {@link AppBarLayout}, in px
             */
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                if(verticalOffset==0){
                    Log.d("Sylvia-listener","起始状态！")
                    //此时是啥都没动，最原始的展开状态
                    if(mBeforeStare==AppBarState.IDLE){
                        return
                    }
                    mBeforeStare=AppBarState.IDLE
                    binding.tvTitle.alpha=1f
                    binding.ivTopBg.alpha=1f
                    binding.recommendHeaderView.alpha=1f
                    binding.tvTitle.setBackgroundResource(R.drawable.ic_funnydate_white)
                    binding.frameLayoutTitle.setBackgroundColor(R.color.transparent)
                    binding.vTitleUnderline.visibility=View.INVISIBLE

                }else if(Math.abs(verticalOffset)>height){
                    //此时已经动完了，该折叠起来了
                    if(mBeforeStare==AppBarState.COLLAPSED){
                        return
                    }
                    mBeforeStare=AppBarState.COLLAPSED

                    binding.tvTitle.alpha=1f
                    binding.ivTopBg.alpha=1f
                    binding.recommendHeaderView.alpha=1f
                    binding.tvTitle.setBackgroundResource(R.drawable.ic_funnydate_color)
                    binding.frameLayoutTitle.setBackgroundColor(R.color.white)

                    binding.recommendHeaderView.visibility=View.INVISIBLE

                    binding.vTitleUnderline.visibility=View.VISIBLE

                    Log.d("Sylvia-listener","折叠状态！")
                }else{
                    //此时中间状态，既没有完全折叠，也没有完全展开

                    binding.recommendHeaderView.visibility=View.VISIBLE

                    if(mBeforeStare==AppBarState.IDLE){
                        binding.vTitleUnderline.visibility=View.INVISIBLE
                    }
                    if(mBeforeStare==AppBarState.IDLE||mBeforeStare==AppBarState.COLLAPSED){
                        binding.frameLayoutTitle.setBackgroundColor(R.color.transparent)
                        binding.vTitleUnderline.visibility=View.VISIBLE
                    }
                    mBeforeStare=AppBarState.EXPANDED


                    changeTitleBarBg(verticalOffset, height)
                    Log.d("Sylvia-listener","中间状态！")
                }


            }

        })



    }

    private fun changeTitleBarBg(verticalOffset: Int, height: Float) {
        var currFraction:Float=Math.abs(verticalOffset*1.0f/height)

        if(currFraction>1) currFraction=1f
        else if(currFraction<0) currFraction=0f

        val halfAlpha=1-currFraction
        //设置透明度
        binding.ivTopBg.alpha=halfAlpha
        binding.recommendHeaderView.alpha=halfAlpha
        binding.tvTitle.alpha=1-halfAlpha
        Log.d("Sylvia-alpha","changeTitleBarBg,正在修改透明度，alpha=$halfAlpha")



    }
}