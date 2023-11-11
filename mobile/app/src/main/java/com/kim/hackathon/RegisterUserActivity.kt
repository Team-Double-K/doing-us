package com.kim.hackathon


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.kim.hackathon.databinding.ActivityRegisterUserBinding
import com.kim.hackathon.utils.ApiService
import com.kim.hackathon.vo.UserForJoinVO
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterUserActivity : AppCompatActivity() {
    lateinit var apiService:ApiService

    lateinit var main_intent: Intent  // main_intent 선언

    lateinit var login_intent: Intent  // main_intent 선언
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val register_user_binding = ActivityRegisterUserBinding.inflate(layoutInflater)
        setContentView(register_user_binding.root)
        apiService = com.kim.hackathon.config.init()
//
        main_intent = Intent(this, MainActivity::class.java)


        register_user_binding.btnRegister.setOnClickListener {
            val input_userId: String? = register_user_binding.editTextId.text.toString()
            val input_userPw: String? = register_user_binding.editTextPw.text.toString()
            val input_userName: String? = register_user_binding.editTextName.text.toString()
            val year = register_user_binding.datePicker.year
            val month = register_user_binding.datePicker.month + 1 // 월은 0부터 시작하므로 1을 더해줍니다.
            val day = register_user_binding.datePicker.dayOfMonth

            val formattedDate = String.format("%04d-%02d-%02d", year, month, day)

            Log.d("data", "${formattedDate}")

            val userRequestVO:UserForJoinVO? = UserForJoinVO(input_userId, input_userPw, input_userName, formattedDate)
            join(userRequestVO)
        }
    }

    private fun join(userRequestVO:UserForJoinVO?){
        val call = apiService.join("http://3.35.218.61:8000/api/auth/join",userRequestVO)
        call.enqueue(object: Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Toast.makeText(applicationContext, R.string.registration_successful, Toast.LENGTH_SHORT).show()
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
                Toast.makeText(applicationContext, "네트워크 호출 실패: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


}