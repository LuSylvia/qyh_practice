package com.qyh_practice

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import com.example.module_common.BaseActivity
import com.example.module_common.DentifyingCodeView
import com.qyh_practice.databinding.ActivityLoginBinding
import com.qyh_practice.module_login.viewmodel.LoginViewModel
import java.util.regex.Pattern

class LoginActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()

    private lateinit var edPhone: EditText
    private lateinit var btnGetDentifyingcode: Button
    private lateinit var btnLogin: Button
    private lateinit var edDentifyingcode: DentifyingCodeView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //setContentView(R.layout.activity_login)
        //bindView()

        listener()
    }

    fun bindView() {
        edPhone = findViewById(R.id.ed_phone)
        edDentifyingcode = findViewById(R.id.ed_dentifyingcode)

        btnGetDentifyingcode = findViewById(R.id.btn_get_dentifyingcode)
        btnLogin = findViewById(R.id.btn_login)
    }


    //处理点击事件
    fun listener() {
        //1.监听手机号的输入情况,设置获取验证码的按钮是否可点击
        binding.edPhone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                s?.let { edPhoneRelatedChange(it.length) }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //binding.tvPhoneError.visibility=View.INVISIBLE

            }

            override fun afterTextChanged(s: Editable?) {
                s?.let { edPhoneRelatedChange(it.length) }
            }
        })


        //2.获取验证码按钮点击后，触发事件，向用户填写的手机号发送验证码
        //type的含义——1:登录，2：注册，3：绑定
        binding.btnGetDentifyingcode.setOnClickListener { v ->
            run {
                val phone: String = binding.edPhone.text.toString()
                if (phone.length == 11) {
                    //调viewModel，发验证码
                    try {
                        loginViewModel.getSmsCode(phone, 1)
                    } catch (e: java.lang.Exception) {

                    }

                }
            }
        }

        //3.真正的登录逻辑的实现
        //采用的接口是不需要微信stageToken的接口
        //传入手机号phone，验证码code
        binding.btnLogin.setOnClickListener {
            run {
                //(1)从edPhone获取手机号
                val phone: String = binding.edPhone.text.toString()
                if (phone.length != 11) {
                    Log.d("LoginActivity", "手机号异常+${phone}+${phone.length}")
                    return@setOnClickListener
                }
                Log.d("LoginActivity", "手机号是${phone}")
                //（2）从edDentifyingcode获取验证码
                val code: String = binding.edDentifyingcode.text.toString()
                if (code.length != 4) {
                    Log.d("LoginActivity", "验证码异常+${code}+${code.length}")
                    return@setOnClickListener
                }
                Log.d("LoginActivity", "验证码是${code}")
                val errorMsg: String? = loginViewModel.login(phone, code)
                if (errorMsg == null) {
                    //代表成功
                    //跳转到MainActivity
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    //代表失败
                    //啥都不动，还得弹错误信息
                    Log.d("LoginActivity", errorMsg)
                }

            }
        }

    }


    //与edPhone相关的修改
    private fun edPhoneRelatedChange(numLen: Int) {
//        if(numLen<10){
//            //弹出警告框
//            binding.tvPhoneError.visibility= View.VISIBLE
//            //获取验证码按钮不可点击
//            binding.btnGetDentifyingcode.isClickable=false
//        }else{
//            binding.tvPhoneError.visibility= View.INVISIBLE
//            binding.btnGetDentifyingcode.isClickable=true
//
//        }
    }


    //验证手机号格式
    private fun checkMobiles(mobiles: String): Boolean {
        var flag = false
        flag = try {
            val p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$")
            val m = p.matcher(mobiles)
            m.matches()
        } catch (e: Exception) {
            false
        }
        return flag
    }


}