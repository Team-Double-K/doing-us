package com.kim.hackathon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
//import com.kim.hackathon.databinding.ActivityRegisterBinding
import com.kim.hackathon.utils.ApiService
import com.kim.hackathon.vo.UserForJoinVO
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterUserActivity : AppCompatActivity() {
    lateinit var apiService:ApiService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val register_binding = ActivityRegisterBinding.inflate(layoutInflater)
//        setContentView(register_binding.root)
        apiService = com.kim.hackathon.config.init()

//        register_binding.btnRegister.setOnClickListener {
//            val input_userId: String? = register_binding.editTextId.text.toString()
//            val input_userPw: String? = register_binding.editTextPw.text.toString()
//            val input_userName: String? = register_binding.editTextName.text.toString()
//            val input_birthDate: String? = register_binding.datePicker.toString()//TODO: yyyy-mm-dd

//            val userRequestVO:UserForJoinVO? = UserForJoinVO(input_userId, input_userPw, input_userName, input_birthDate)
//            join(userRequestVO)
        }
    }

//    private fun join(userRequestVO:UserForJoinVO?){
//        val call = apiService.join("url",userRequestVO)
//        call.enqueue(object: Callback<ResponseBody>{
//            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
//                if (response.isSuccessful) {
//                    Toast.makeText(applicationContext, R.string.registration_successful, Toast.LENGTH_SHORT).show()
//                }
//
//            }
//            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                Toast.makeText(applicationContext, "네트워크 호출 실패: ${t.message}", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }


//}