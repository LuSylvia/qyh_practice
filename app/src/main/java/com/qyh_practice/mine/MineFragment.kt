package com.qyh_practice.mine

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.module_common.notification.NotificationUtil
import com.example.module_common.utils.CookieHelper
import com.qyh_practice.databinding.FragmentMyBinding


class myFragment : Fragment() {
    private lateinit var binding: FragmentMyBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyBinding.inflate(inflater, container, false)
        val view = binding.root
        return view

        //return inflater.inflate(R.layout.fragment_my, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setListener()
    }


    fun setListener() {
        binding.btnQuit.setOnClickListener {
            //强制下线
            //需要先清除本地缓存


            val intent = Intent("com.qyh_practice.broadcast.FORCE_OFFLINE")
            activity?.sendBroadcast(intent)
        }

        binding.btnSendNotification.setOnClickListener {
            //TODO:发送通知
            context?.let { it1 ->
                NotificationUtil.sendNotification(it1, "测试内容", null)
            }
        }
    }

}