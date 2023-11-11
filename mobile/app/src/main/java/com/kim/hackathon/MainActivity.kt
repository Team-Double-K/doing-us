package com.kim.hackathon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.kakao.util.maps.helper.Utility
import com.kim.hackathon.databinding.ActivityLoginBinding
import com.kim.hackathon.databinding.ActivityMainBinding
import com.kim.hackathon.databinding.ActivityMapBinding
//import com.kim.hackathon.databinding.ActivityRegisterBinding
import com.kim.hackathon.utils.ApiService
import com.kim.hackathon.vo.UserVO
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var apiService:ApiService
    lateinit var login_binding:ActivityLoginBinding
    lateinit var main_binding:ActivityMainBinding
//    lateinit var register_binding:ActivityRegisterBinding
    lateinit var map_binding:ActivityMapBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        //
        Log.d("a", "keyhash : ${Utility.getKeyHash(this)}")

        //
        login_binding = ActivityLoginBinding.inflate(layoutInflater)
        main_binding = ActivityMainBinding.inflate(layoutInflater)
//        register_binding = ActivityRegisterBinding.inflate(layoutInflater)
        map_binding = ActivityMapBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(main_binding.root)
        apiService = com.kim.hackathon.config.init()

        login_binding.btnLogin.setOnClickListener {
            val input_userId:String? = login_binding.editTextId.text.toString()
            val input_userPw:String? = login_binding.editTextPw.text.toString()

            val userRequestVO:UserVO? =  UserVO(input_userId, input_userPw)
            login(userRequestVO)
        }
        login_binding.btnRegister.setOnClickListener {
            val register_intent = Intent(this, RegisterUserActivity::class.java)
            startActivity(register_intent)
        }

    }

    private fun login(userRequestVO:UserVO?){
        val call = apiService.login("url",userRequestVO)
        call.enqueue(object: Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                //토큰날라오겠지 시발
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                //좇됨 ㅅtry
            }
        })
    }


}