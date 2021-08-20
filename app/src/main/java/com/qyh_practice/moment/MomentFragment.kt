package com.qyh_practice.moment


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.module_common.utils.SecondsCountDownTimer
import com.qyh_practice.R
import com.qyh_practice.databinding.FragmentMomentBinding


class MomentFragment : Fragment() {
    private lateinit var binding: FragmentMomentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMomentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        onGetAsyncInfoSuccess()
        binding.btnStopCountDown.setOnClickListener {
            run {
                if (countDownTimer != null) {
                    countDownTimer?.cancel()
                    angelThankCountDown()
                }else{
                    binding.tvCountdown.text="The World!"
                }
            }
        }


    }


    override fun onDestroy() {
        super.onDestroy()
        //回收计时器所占资源
        if (countDownTimer != null) {
            countDownTimer!!.cancel()
        }

    }


    //感谢环节的倒计时，由后台传过来
    private var thankCountDown = 180L

    //实现房间本身倒计时
    private var countDownTimer: SecondsCountDownTimer? = null


    private fun onGetAsyncInfoSuccess() {
        val roomCountDown = 20L

        if (roomCountDown > 0L) {
            binding.tvCountdown.visibility = View.VISIBLE
            //获取房间本身倒计时和感谢环节倒计时
            countDownTimer = object : SecondsCountDownTimer(roomCountDown) {
                override fun onSeconds(seconds: Long) {
                    var timeStr: String = String.format(
                        "%d:%02d",
                        seconds / 60L,
                        seconds % 60L
                    )
                    if (seconds < 60L) {
                        //说明房间剩余时间小于1分钟，该进入偷塔环节了
                        binding.tvCountdown.setTextColor(Color.parseColor("#fd506d"))
                        timeStr = getString(R.string.steal_tower_link) + timeStr
                    }

                    binding.tvCountdown.text = timeStr


                }

                override fun onFinish() {
                    super.onFinish()
                    angelThankCountDown()
                }
            }
            countDownTimer?.start()
        } else {
            binding.tvCountdown.visibility = View.GONE
        }
    }

    /**
     * 实现天使场的感谢环节倒计时
     */
    fun angelThankCountDown() {
        if (thankCountDown > 0L) {
            //数据请求成功
            binding.tvCountdown.visibility = View.VISIBLE
            //回收房间倒计时的计时器
            if (countDownTimer != null) {
                countDownTimer?.cancel()
            }
            //创建感谢环节的计时器
            countDownTimer = object : SecondsCountDownTimer(thankCountDown) {
                override fun onSeconds(seconds: Long) {
                    binding.tvCountdown.text = getString(
                        R.string.thank_link,
                        String.format("%d:%02d", seconds / 60, seconds % 60)
                    )

                    binding.tvCountdown.setTextColor(resources.getColor(R.color.color_fd506d))

                }
            }
            countDownTimer?.start()
        } else {
            //数据请求失败，隐藏倒计时框
            binding.tvCountdown.visibility = View.GONE
        }
    }


}