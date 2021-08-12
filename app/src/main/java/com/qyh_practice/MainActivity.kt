package com.qyh_practice

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.module_common.activity.ActivityManager
import com.example.module_common.activity.BaseActivity
import com.example.module_common.constants.RouterManager
import com.example.module_common.permission.PermissionX
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.qyh_practice.adapter.ViewPagerAdapter
import com.qyh_practice.databinding.ActivityMainBinding
import com.qyh_practice.live.LiveFragment
import com.qyh_practice.message.messageFragment
import com.qyh_practice.mine.myFragment
import com.qyh_practice.module_login.SplashActivity
import com.qyh_practice.module_recommend.Recommend2Fragment
import com.qyh_practice.moment.trendFragment

@Route(path = RouterManager.ACTIVITY_MAIN)
class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewPager: ViewPager
    private lateinit var navView: BottomNavigationView
    private var menuItem: MenuItem? = null
    private val fragmentList: ArrayList<Fragment> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        viewPager = binding.viewPager
        navView = binding.navView
        //子Activity中必须显示调用此函数来获取title控件
        findView()
        if (tv_main_title != null) {
            tv_main_title.setText("仿趣约会")
        }
        binding.baseToolbar.visibility = View.GONE
        //toolbar.setLogo(R.drawable.ic_dashboard_black_24dp)
        //tv_sub_title.setText("more")
        initViewPager()
        setListener()

        //释放SplashActivity所占用的资源
        ActivityManager.getInstance().removeActivity(SplashActivity::class.java)
        //getPermissions()


    }


    //请求权限
    //注意，这个方法每次启动都会请求权限，应尝试修改使用
    private fun getPermissions() {
        PermissionX.request(
            this,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA
        ) { allGranted, deniedList ->
            if (allGranted) {
                //Toast.makeText(this, "All permissions are granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "You denied $deniedList", Toast.LENGTH_SHORT).show()
            }

        }
    }

    //ViewPager初始化
    private fun initViewPager() {
        val recommendationFragment = Recommend2Fragment()
        val blindDateFragment = LiveFragment()
        val trendFragment = trendFragment()
        val messageFragment = messageFragment()
        val myFragment = myFragment()

        fragmentList.add(recommendationFragment)
        fragmentList.add(blindDateFragment)
        fragmentList.add(trendFragment)
        fragmentList.add(messageFragment)
        fragmentList.add(myFragment)

    }

    //设置viewPager的切换动画和bottomNavigationView的点击事件
    private fun setListener() {
        //BottomNavigationView的点击事件
        //为了让viewPager知道自己要移动到哪个页面
        navView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.item_recommendation -> {
                    viewPager.currentItem = 0
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.item_blindDate -> {
                    viewPager.currentItem = 1
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.item_trend -> {
                    viewPager.currentItem = 2
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.item_message -> {
                    viewPager.currentItem = 3
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.item_mine -> {
                    viewPager.currentItem = 4
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }

        //给viewPager设置addpter
        val normalAdapter = ViewPagerAdapter(supportFragmentManager, fragmentList)
        viewPager.adapter = normalAdapter
        //预加载页数
        viewPager.offscreenPageLimit = fragmentList.size
        //页面切换时，设置不同页面
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                if (menuItem != null) {
                    //如果有已经选中的item，取消它的选中状态
                    //Log.d("ERR_MENUITEM", "null还走这分支")
                    menuItem!!.isChecked = false
                } else {
                    //如果没有，则取消默认的选中状态(第一个item)
                    navView.menu.getItem(0).isChecked = false
                }
                //获取当前item，设置为选中状态
                menuItem = navView.menu.getItem(position)
                menuItem!!.isChecked = true

            }
        })


    }


}