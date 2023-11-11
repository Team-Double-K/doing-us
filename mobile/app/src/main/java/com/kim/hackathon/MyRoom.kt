package com.kim.hackathon

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kim.hackathon.RoomAdapter
import com.kim.hackathon.databinding.ActivityMyRoomBinding
import com.kim.hackathon.utils.ApiService
import com.kim.hackathon.vo.RoomVO2
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyRoom : AppCompatActivity() {
    lateinit var apiService: ApiService
    lateinit var roomAdapter: RoomAdapter // 추가
    lateinit var recyclerView: RecyclerView // 추가

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMyRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)
        apiService = com.kim.hackathon.config.init()

        // RecyclerView 초기화
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        roomAdapter = RoomAdapter()
        recyclerView.adapter = roomAdapter

        getMyRoom()
    }

    fun getMyRoom(){
        val sharedPreferences = getSharedPreferences("user",0)
        val accessToken:String? = sharedPreferences.getString("accessToken", null)
        val u_id:String? = sharedPreferences.getString("userId", null)

        val call = apiService.findRoomOfUserByUserId("http://3.35.218.61:8000/api/user/${u_id}/room",accessToken)
        call.enqueue(object : Callback<ArrayList<RoomVO2>> {
            override fun onResponse(
                call: Call<ArrayList<RoomVO2>>,
                response: Response<ArrayList<RoomVO2>>
            ) {
                if (response.isSuccessful) {
                    val roomList = response.body()
                    if (roomList != null) {
                        // 서버로부터 받아온 roomList를 어댑터에 설정
                        roomAdapter.setData(roomList,u_id, accessToken)
                    } else {
                        Log.e("MyRoom", "Response body is null.")
                    }
                } else {
                    Log.e("MyRoom", "Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ArrayList<RoomVO2>>, t: Throwable) {
                Log.e("MyRoom", "Error: ${t.message}")
            }
        })
    }
    private fun deleteRoom(position: Int) {
        // 해당 position에 있는 Room을 삭제하는 동작을 수행
        // (서버로 삭제 요청 등 필요한 동작 수행)
    }
}
