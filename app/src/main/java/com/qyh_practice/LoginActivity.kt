package com.qyh_practice

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.module_common.BaseActivity
import com.example.module_common.entity.LoadState
import com.example.module_common.utils.LogUtil
import com.qyh_practice.databinding.ActivityLoginBinding
import com.qyh_practice.module_login.viewmodel.LoginViewModel
import java.util.regex.Pattern

class LoginActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()
    private var waitCountDown:Boolean=false //用于标识获取验证码按钮是否正在等待倒计时

    //实现倒计时功能（默认30s）
    private val timer: CountDownTimer = object : CountDownTimer(30000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            binding.btnGetDentifyingcode.setText("重新发送(${millisUntilFinished / 1000})")

        }

        override fun onFinish() {
            binding.btnGetDentifyingcode.setText("获取验证码")
            waitCountDown=false
            binding.btnGetDentifyingcode.isEnabled = true

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        init()
        loginViewModel.loadState.observe(this, Observer {
            when(it){
                LoadState.LOADING->{
                    //啥都不做
                    LogUtil.d("LoginActivity", "在loading")
                }
                LoadState.SUCCESS->{
                    LogUtil.d("LoginActivity", "登录成功")
                    //跳转主界面
                    val intent = Intent(this,MainActivity::class.java)
                    startActivity(intent)
                }
                LoadState.FAIL->{
                    //打印错误信息
                    LogUtil.d("LoginActivity", "登录失败")
                }
                else -> {
                    LogUtil.d("LoginActivity", "未知的错误（虽然也不太可能）")
                }
            }

        }

        )

    }

    //初始化操作
    fun init() {
        findView()
        listener()
        tv_main_title.setText("手机号登录")
    }

    //处理点击事件
    private fun listener() {
        //1.监听手机号的输入情况,设置获取验证码的按钮是否可点击
        binding.edPhone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                s?.let { edPhoneRelatedChange(it.length) }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //binding.tvPhoneError.visibility = View.INVISIBLE
                //只要用户在修改手机号，获取验证码的按钮就应该设为不可点击
                edPhoneRelatedChange(0)
            }

            override fun afterTextChanged(s: Editable?) {
                if(waitCountDown==false){
                    s?.let { edPhoneRelatedChange(it.length) }
                }

            }
        })
        //监听验证码的输入情况
        binding.edDentifyingcode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                s?.let { edDentifyingCodeRelatedChange(it.length) }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                s?.let { edDentifyingCodeRelatedChange(it.length) }
            }

        })


        //2.获取验证码按钮点击后，触发事件，向用户填写的手机号发送验证码
        //type的含义——1:登录，2：注册，3：绑定
        binding.btnGetDentifyingcode.setOnClickListener {
            run {
                val phone: String = binding.edPhone.text.toString()
                if (phone.length == 11) {
                    //调viewModel，发验证码
                    try {
                        timer.start()
                        waitCountDown=true
                        binding.btnGetDentifyingcode.isEnabled = false
                        binding.btnGetDentifyingcode.setBackgroundColor(resources.getColor(R.color.grey))
                    } catch (e: java.lang.Exception) {

                    }

                }


            }
        }

        //3.真正的登录逻辑的实现
        //采用的接口是需要微信stageToken的接口
        //传入手机号phone，验证码code,stageToken(该属性传"")
        binding.btnLogin.setOnClickListener {
            run {
                //(1)从edPhone获取手机号和验证码
                val phone: String = binding.edPhone.text.toString()
                val code: String = binding.edDentifyingcode.text.toString()

                if (phone.length != 11 || code.length != 4) {
                    return@setOnClickListener
                }
                //TODO:自定义广播测试
                val intent = Intent("com.example.broadcasttest.MyBroadcast")
                intent.setPackage(packageName)
                //sendBroadcast(intent)
                sendOrderedBroadcast(intent, null)

                //调用登录函数，获取错误信息
                //成功后，它会自动调用getConfig函数
                loginViewModel.login(phone, code, "")



            }
        }

    }

    //与验证码相关的修改
    private fun edDentifyingCodeRelatedChange(codeLen: Int) {
        if (codeLen < 4) {
            binding.tvDentifyingcodeError.visibility = View.VISIBLE
            binding.btnLogin.isEnabled = false

        } else {
            binding.tvDentifyingcodeError.visibility = View.INVISIBLE
            binding.btnLogin.isEnabled = true
        }
    }


    //与edPhone相关的修改
    @SuppressLint("ResourceAsColor")
    private fun edPhoneRelatedChange(numLen: Int) {
        if (numLen < 11) {
            //弹出警告框
            binding.tvPhoneError.visibility = View.VISIBLE
            //获取验证码按钮不可点击
            binding.btnGetDentifyingcode.isEnabled = false
            //修改获取验证码按钮的背景颜色
            binding.btnGetDentifyingcode.setBackgroundColor(resources.getColor(R.color.grey))

        } else {
            //已输入11位手机号
            binding.tvPhoneError.visibility = View.INVISIBLE
            binding.btnGetDentifyingcode.isEnabled = true
            binding.btnGetDentifyingcode.setBackgroundColor(resources.getColor(R.color.colorPrimary))

        }
    }

    //验证手机号格式
    private fun checkMobiles(mobiles: String): Boolean {
        return try {
            val p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$")
            val m = p.matcher(mobiles)
            m.matches()
        } catch (e: Exception) {
            false
        }
    }


}