package com.kim.hackathon

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.kim.hackathon.config.init
import com.kim.hackathon.databinding.ItemRoomBinding
import com.kim.hackathon.utils.ApiService
import com.kim.hackathon.vo.RoomVO
import com.kim.hackathon.vo.RoomVO2
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.recyclerview.widget.RecyclerView

class RoomAdapter : RecyclerView.Adapter<RoomAdapter.RoomViewHolder>() {
    companion object {
        var accessToken: String? = null
        var userId: String? = null
        private var roomList: List<RoomVO2> = emptyList()
        private lateinit var adapter: RoomAdapter
    }

    fun setData(newList: List<RoomVO2>, userId: String?, accessToken: String?) {
        roomList = newList
        RoomAdapter.accessToken = accessToken
        RoomAdapter.userId = userId
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_room, parent, false)
        //adapter = this  // adapter 변수 초기화
        return RoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        holder.bind(roomList[position])
    }

    override fun getItemCount(): Int {
        return roomList.size
    }

    class RoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var apiService: ApiService
        private val roomIdTextView: TextView = itemView.findViewById(R.id.room_id)
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val commentsTextView: TextView = itemView.findViewById(R.id.commentsTextView)
        private val deleteButton: Button = itemView.findViewById(R.id.deleteButton)

        fun deleteRoomByRoomId(roomId: Int?, position: Int) {
            val call = apiService.deleteRoomByUserIdAndRoomId("http://3.35.218.61:8000/api/user/${RoomAdapter.userId}/room/${roomId}",
                RoomAdapter.accessToken)

            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        val jsonResponse = response.body()?.string()

                        try {
                            val jsonObject = JSONObject(jsonResponse)
                            val msg = jsonObject.getString("msg")

                            Toast.makeText(itemView.context, msg, Toast.LENGTH_SHORT).show()

                            val updatedList = roomList.toMutableList()
                            updatedList.removeAt(position)

                            (itemView.context as Activity).runOnUiThread {
                                adapter.setData(updatedList, RoomAdapter.userId, RoomAdapter.accessToken)
                            }

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("err", "err")
                }
            })
        }

        fun bind(room: RoomVO2) {
            apiService = init()

            roomIdTextView.text = "[번호 ${room.r_id}]"
            titleTextView.text = "제목 : ${room.r_title}"
            commentsTextView.text = room.r_comments

            deleteButton.setOnClickListener {
                deleteRoomByRoomId(room.r_id, adapterPosition)
            }
        }
    }
}