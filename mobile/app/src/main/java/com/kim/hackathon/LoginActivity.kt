package com.kim.hackathon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.kakao.util.maps.helper.Utility
import com.kim.hackathon.databinding.ActivityLoginBinding
import com.kim.hackathon.databinding.ActivityMainBinding
import com.kim.hackathon.databinding.ActivityMapBinding
//import com.kim.hackathon.databinding.ActivityRegisterBinding
import com.kim.hackathon.utils.ApiService
import com.kim.hackathon.vo.UserVO
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    lateinit var apiService:ApiService
    lateinit var login_binding:ActivityLoginBinding
    lateinit var main_binding:ActivityMainBinding
    lateinit var map_binding:ActivityMapBinding
    lateinit var main_intent: Intent  // main_intent 선언

    override fun onCreate(savedInstanceState: Bundle?) {

        val sharedPreferences = getSharedPreferences("user",0)
        val accessToken:String? = sharedPreferences.getString("accessToken", null)
        main_intent = Intent(this, MainActivity::class.java)

        if(accessToken != null){

            finish()
            startActivity(main_intent)
        }
        val register_user_intent = Intent(this, RegisterUserActivity::class.java)


        //
        login_binding = ActivityLoginBinding.inflate(layoutInflater)
        main_binding = ActivityMainBinding.inflate(layoutInflater)
        map_binding = ActivityMapBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(login_binding.root)/////////////////////////////////////////////이걸로 장난질 중
        apiService = com.kim.hackathon.config.init()

        login_binding.btnLogin.setOnClickListener {
            val input_userId:String? = login_binding.editTextId.text.toString()
            val input_userPw:String? = login_binding.editTextPw.text.toString()

            val userRequestVO:UserVO? =  UserVO(input_userId, input_userPw)

            Log.d("testId", "${input_userId}")
            Log.d("testPw", "${input_userPw}")
            login(userRequestVO)
        }
        login_binding.btnRegister.setOnClickListener {
            finish()
            startActivity(register_user_intent)
        }

    }

    private fun login(userRequestVO:UserVO?){
        val call = apiService.login("http://3.35.218.61:8000/api/auth/login",userRequestVO)
        call.enqueue(object: Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    // 성공적으로 요청이 완료된 경우 수행할 작업
                    val responseBodyString = response.body()?.string()
                    val jsonObject = JSONObject(responseBodyString)
                    val accessToken: String? = jsonObject.getString("accessToken")
                    val userId:String? = userRequestVO?.userId

                    val sharedPreferences =  getSharedPreferences("user", 0)
                    val edit = sharedPreferences.edit()
                    edit.putString("accessToken",accessToken)
                    edit.putString("userId",userId)
                    edit.apply()
                    finish()
                    startActivity(main_intent)
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                Toast.makeText(this@LoginActivity, "로그인을 다시 시도해주세요", Toast.LENGTH_SHORT).show()
                Toast.makeText(this@LoginActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


}