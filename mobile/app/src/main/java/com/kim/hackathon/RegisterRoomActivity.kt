package com.kim.hackathon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.kim.hackathon.databinding.ActivityMainBinding
//import com.kim.hackathon.databinding.ActivityRegisterBinding
import com.kim.hackathon.utils.ApiService
import com.kim.hackathon.vo.UserForJoinVO
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterRoomActivity : AppCompatActivity() {
    lateinit var apiService:ApiService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val main_binding = ActivityMainBinding.inflate(layoutInflater)
//        val register_
//
        setContentView(main_binding.root)
//        apiService = com.kim.hackathon.config.init()
//
//        main_binding.editText_title.setOnClickListener {
//            val ownerId:String? =  //TODO : 이거 모임 만들 수 있는 레이아웃 틀 만들고 설정하기
//            var title:String?,
//            var comments:String?,
//            var longitude:Double?,
//            var latitude:Double?,
//            var meetDate:String?,
//            var headcount:Int?
//            val input_userId: String? = register_binding.editTextId.text.toString()
//            val input_userPw: String? = register_binding.editTextPw.text.toString()
//            val input_userName: String? = register_binding.editTextName.text.toString()
//            val input_birthDate: String? = register_binding.datePicker.toString()//TODO: yyyy-mm-dd
//
//            val userRequestVO:UserForJoinVO? = UserForJoinVO(input_userId, input_userPw, input_userName, input_birthDate)
//            join(userRequestVO)
//        }
    }

    private fun join(userRequestVO:UserForJoinVO?){
        val call = apiService.join("url",userRequestVO)
        call.enqueue(object: Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Toast.makeText(applicationContext, R.string.registration_successful, Toast.LENGTH_SHORT).show()
                }

            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(applicationContext, "네트워크 호출 실패: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


}