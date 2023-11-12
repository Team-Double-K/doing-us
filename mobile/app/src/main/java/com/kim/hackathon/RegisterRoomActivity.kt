package com.kim.hackathon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.kim.hackathon.config.LocationManger
import com.kim.hackathon.databinding.ActivityMainBinding
import com.kim.hackathon.databinding.ActivityRegisterRoomBinding
import com.kim.hackathon.databinding.ActivityRegisterUserBinding
import com.kim.hackathon.databinding.ListLayoutBinding
//import com.kim.hackathon.databinding.ActivityRegisterBinding
import com.kim.hackathon.utils.ApiService
import com.kim.hackathon.vo.RoomVO
import com.kim.hackathon.vo.UserForJoinVO
import com.kim.hackathon.vo.UserVO
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterRoomActivity : AppCompatActivity() {
    lateinit var apiService:ApiService
    lateinit var register_room_binding:ActivityRegisterRoomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val main_binding = ActivityMainBinding.inflate(layoutInflater)
        val list_binding = ListLayoutBinding.inflate(layoutInflater)
        register_room_binding = ActivityRegisterRoomBinding.inflate(layoutInflater)
        setContentView(register_room_binding.root)

//
        apiService = com.kim.hackathon.config.init()

        register_room_binding.locationSearch.setOnClickListener {
            var searchmapIntent  = Intent(this, SearchMapActivity::class.java)
            startActivity(searchmapIntent)

       }
        val regroombinding = ActivityRegisterRoomBinding.inflate(layoutInflater)
        regroombinding.btnroomRegister.setOnClickListener { //완성으로
            val currentPosition:List<Double> = LocationManger.getPosition()
            val input_longitude:Double? = currentPosition.get(0)
            val input_latitude:Double?= currentPosition.get(1)
            Log.d("abab", "dddddddd")
            Log.d("test11", "${input_longitude}, ${input_latitude}")

        }


        regroombinding.btnroomRegister.setOnClickListener {
            val input_title:String? = register_room_binding.editTextTitle.text.toString()
            val input_comments:String? = register_room_binding.editTextComments.text.toString()
            Log.d("test99", "${input_title}, ${input_comments}")


            val input_date: String? = getMeetDay()
            Log.d("date", "${input_date}")
            var input_headcount: Int? = register_room_binding.editTextHeadcount.text.toString().toIntOrNull()


            //val roomRequestVO: RoomVO? =  RoomVO()//TODO : 인자 다 줘야함

            //createRoomByUserId(roomRequestVO)








        }
    }
    private fun createRoomByUserId(roomRequestVO:RoomVO?){
        val call = apiService.createRoomByUserId("http://3.35.218.61:8000/api/auth/login",roomRequestVO)
        call.enqueue(object: Callback<ResponseBody>{

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
//                if (response.isSuccessful) {
//                    // 성공적으로 요청이 완료된 경우 수행할 작업
//                    val responseBodyString = response.body()?.string()
//                    val jsonObject = JSONObject(responseBodyString)
//                    val accessToken: String? = jsonObject.getString("accessToken")
//                    val userId:String? = roomRequestVO?.userId
//
//                    val sharedPreferences =  getSharedPreferences("user", 0)
//                    val edit = sharedPreferences.edit()
//                    edit.putString("accessToken",accessToken)
//                    edit.putString("userId",userId)
//                    edit.apply()
//                    finish()
//                    startActivity(main_intent)
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@RegisterRoomActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getMeetDay(): String {
        val selectedDays = mutableListOf<String>()

        if (register_room_binding.mondayCheckbox.isChecked) {
            selectedDays.add("월")
        }

        if (register_room_binding.tuesdayCheckbox.isChecked) {
            selectedDays.add("화")
        }

        if (register_room_binding.wednesdayCheckbox.isChecked) {
            selectedDays.add("수")
        }

        if (register_room_binding.thursdayCheckbox.isChecked) {
            selectedDays.add("목")
        }

        if (register_room_binding.fridayCheckbox.isChecked) {
            selectedDays.add("금")
        }

        if (register_room_binding.saturdayCheckbox.isChecked) {
            selectedDays.add("토")
        }

        if (register_room_binding.sundayCheckbox.isChecked) {
            selectedDays.add("일")
        }

        return selectedDays.joinToString(separator = "|")
    }





}