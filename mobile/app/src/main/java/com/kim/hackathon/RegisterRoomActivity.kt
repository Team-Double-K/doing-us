package com.kim.hackathon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Toast
import com.kim.hackathon.config.LocationManger
import com.kim.hackathon.config.RoomManger
import com.kim.hackathon.databinding.ActivityMainBinding
import com.kim.hackathon.databinding.ActivityRegisterRoomBinding
import com.kim.hackathon.databinding.ActivityRegisterUserBinding
import com.kim.hackathon.databinding.ListLayoutBinding
//import com.kim.hackathon.databinding.ActivityRegisterBinding
import com.kim.hackathon.utils.ApiService
import com.kim.hackathon.vo.RoomVO
import com.kim.hackathon.vo.RoomVO3
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
    // Extension function to convert Editable to String
    fun Editable?.toString(): String {
        return this?.toString() ?: ""
    }

    // Extension function to convert String to Editable
    fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        apiService = com.kim.hackathon.config.init()
        val main_binding = ActivityMainBinding.inflate(layoutInflater)
        val list_binding = ListLayoutBinding.inflate(layoutInflater)
        register_room_binding = ActivityRegisterRoomBinding.inflate(layoutInflater)
        setContentView(register_room_binding.root)
        var currentPosition: List<Double> = LocationManger.getPosition()
        val positionString: String = "${currentPosition[0]}, ${currentPosition[1]}"
        var roomInfo: List<String>  = RoomManger.getRoomManger()
        register_room_binding.editTextTitle.text = roomInfo[0].toEditable()
        register_room_binding.editTextComments.text = roomInfo[1].toEditable()
        register_room_binding.editTextWhere.text = positionString.toEditable()
        apiService = com.kim.hackathon.config.init()
        register_room_binding.editTextWhere.isFocusable = false
        register_room_binding.editTextWhere.isFocusableInTouchMode = false
        register_room_binding.locationSearch.setOnClickListener {
            var searchmapIntent  = Intent(this, SearchMapActivity::class.java)
            startActivity(searchmapIntent)

       }
        //  regroombinding.btnroomRegister.setOnClickListener { //완성으로
//            val currentPosition:List<Double> = LocationManger.getPosition()
//            val input_longitude:Double? = currentPosition.get(0)
//            val input_latitude:Double?= currentPosition.get(1)
//            Log.d("abab", "dddddddd")
//            Log.d("test11", "${input_longitude}, ${input_latitude}")
//
//        }


        register_room_binding.btnroomRegister.setOnClickListener {
            var currentPosition: List<Double> = LocationManger.getPosition()
            if(currentPosition[0] == 0.0 && currentPosition[1] == 0.0) {
                Toast.makeText(this, "모임 장소를 선택해주세요", Toast.LENGTH_SHORT).show()

            }else{
                var title:String? = register_room_binding.editTextTitle.text.toString()
                var comments:String? = register_room_binding.editTextComments.text.toString()
                val headcount: String? = register_room_binding.editTextHeadcount.text.toString()
                val number: Int? = headcount?.toIntOrNull()
                if( title.equals("")|| comments.equals("")|| headcount.equals("")) {
                    Toast.makeText(this, "다시시도 해주세요", Toast.LENGTH_SHORT).show()
                }                else {
                    val requestRoomData:RoomVO3? = RoomVO3(title, comments, currentPosition[0], currentPosition[1], getMeetDay(),number)
                    registerRoom(requestRoomData);
                }
            }
        }
    }
    fun registerRoom(requestRoomData:RoomVO3?){
        val sharedPreferences = getSharedPreferences("user",0)
        val accessToken:String? = sharedPreferences.getString("accessToken", null)
        val u_id:String? = sharedPreferences.getString("userId", null)

        val call =apiService.createRoomByUserId("http://3.35.218.61:8000/api/user/${u_id}/room",requestRoomData, accessToken)
        call.enqueue(object:Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.isSuccessful) {
                    val responseBodyString = response.body()?.string()
                    val jsonObject = JSONObject(responseBodyString)
                    val msg: String? = jsonObject.getString("msg")
                    Toast.makeText(this@RegisterRoomActivity, "${msg}", Toast.LENGTH_SHORT).show()
                    var register_intent = Intent(this@RegisterRoomActivity, MainActivity::class.java)
                    LocationManger.setPosition(0.0,0.0)
                    RoomManger.setRoomManger("","")
                    startActivity(register_intent)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("Network err", "Network err")
                Toast.makeText(this@RegisterRoomActivity, "error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getMeetDay(): String? {
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