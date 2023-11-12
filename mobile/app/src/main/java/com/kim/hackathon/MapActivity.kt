package com.kim.hackathon

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build.VERSION_CODES.P
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat.getCurrentLocation
import androidx.viewbinding.ViewBindings
import com.kim.hackathon.config.RoomDetail
import com.kim.hackathon.config.RoomDetailManger
import com.kim.hackathon.config.init
//import com.kim.hackathon.RoomAdapter.Companion.roomList
//import com.kim.hackathon.RoomAdapter.Companion.roomList

import com.kim.hackathon.databinding.ActivityMapBinding
import com.kim.hackathon.databinding.ActivityRoomInfoDetailBinding
import com.kim.hackathon.utils.ApiService
import com.kim.hackathon.vo.RoomVO4
import net.daum.mf.map.api.MapView
import net.daum.mf.map.api.CalloutBalloonAdapter
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MarkerEventListener(val context: Context): MapView.POIItemEventListener {
    lateinit var apiService:ApiService
    override fun onCalloutBalloonOfPOIItemTouched(
        p0: MapView?,
        p1: MapPOIItem?,
        p2: MapPOIItem.CalloutBalloonButtonType?
    ) {
        this.apiService = init()
        val binding = ActivityRoomInfoDetailBinding.inflate(LayoutInflater.from(context))

        val roomDetail: RoomDetail = RoomDetailManger.getRoom()
        val alertDialog = AlertDialog.Builder(context).run {

            Log.d("test",roomDetail.toString())
            // Update the views with the data from roomDetail
            binding.ballDetailTvName.text = roomDetail.r_title.toString()
            binding.ballDetailTvComments.text = roomDetail.r_comments.toString()
            binding.ballDetailTvDate.text= "모이는 날 : ${roomDetail.r_meet_date.split("|").joinToString(", ")}"
            binding.ballDetailTvHeadcount.text = "인원 : ${roomDetail.r_cur_headcount}/${roomDetail.r_headcount}"
            setView(binding.root)
            setNeutralButton("Cancel"){ dialog, which ->
                // Cancel 버튼 클릭 시 수행할 작업
                // 여기에 이벤트를 추가하십시오.
                dialog.dismiss()
            }
            setPositiveButton("참가"){
                    dialog, which ->

                    joinRoom(roomDetail.r_id)
                    dialog.dismiss()

            }
            setCancelable(true)
            show()
            // Now you can use these views as needed
        }

    }
    fun joinRoom(roomId:Int){

        val sharedPreferences =  context.getSharedPreferences("user", 0)
        val accessToken:String? = sharedPreferences.getString("accessToken", null)
        val userId:String? = sharedPreferences.getString("userId", null)

        val call = apiService.joinRoomByUserIdAndRoomId("http://3.35.218.61:8000/api/user/${userId}/room/${roomId}", accessToken)
        call.enqueue(object: Callback<ResponseBody>{

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val responseBodyString = response.body()?.string()
                    val jsonObject = JSONObject(responseBodyString)
                    val msg: String? = jsonObject.getString("msg")
                    Toast.makeText(context, "${msg}", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(context, "network error", Toast.LENGTH_SHORT).show()
            }

        })
    }


    override fun onPOIItemSelected(p0: MapView?, p1: MapPOIItem?) {
    }

    override fun onDraggablePOIItemMoved(p0: MapView?, p1: MapPOIItem?, p2: MapPoint?) {
    }

    override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?) {
    }
}
class MapActivity : AppCompatActivity() {

    lateinit var apiService:ApiService



    lateinit var mapView: MapView
    private lateinit var mapViewContainer : ViewGroup

    private var uLatitude : Double = 0.0
    private var uLongitude : Double = 0.0

    private  val eventListener = MarkerEventListener(this)
   // private  val eventListener = MarkerEventListener(this)
    //
   //내 위치정보 얻어서 변수에 저장
//   @SuppressLint("MissingPermission")
//   private fun getCurrentLocation() {
//       val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
//       try {
//           val userNowLocation: Location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)!!
//           uLatitude = userNowLocation.latitude
//           uLongitude = userNowLocation.longitude
//       } catch (e: NullPointerException) {
//           Log.e("LOCATION_ERROR", e.toString())
//           finish()
//       }
//   }



    private fun findRoom() {
        val sharedPreferencesToken = getSharedPreferences("token", 0)
        val serverUrl = "http://3.35.218.61:8000/api/room"

        val call = apiService.findRoom(serverUrl)
        call.enqueue(object : Callback<ArrayList<RoomVO4>> {
            override fun onResponse(call: Call<ArrayList<RoomVO4>>, response: Response<ArrayList<RoomVO4>>) {
                if (response.isSuccessful) {
                    val roomList = response.body()
                    Log.d("succes", "succes")

                    if (roomList != null) {
                        setMarkersOnMap(roomList) // 받아온 roomList를 setMarkersOnMap 함수에 전달
                    } else {
                        Toast.makeText(this@MapActivity, "서버에서 유효한 데이터를 받지 못했습니다.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@MapActivity, "서버에서 응답을 받지 못했습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ArrayList<RoomVO4>>, t: Throwable) {
                Log.e("MapActivity", "통신 실패: ${t.message}")
                Toast.makeText(this@MapActivity, "통신 실패", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setMarkersOnMap(roomList: ArrayList<RoomVO4>) {
        for (room in roomList) {
            val marker = MapPOIItem()
            marker.apply {
                val latitude: Double? = room.r_latitude
                val longitude: Double? = room.r_longitude
                Log.d("where", "${latitude}, ${longitude}")

                if (latitude != null && longitude != null) {
                    mapPoint = MapPoint.mapPointWithGeoCoord(latitude, longitude)
                    itemName = "${room.r_id}:${room.r_owner_id}:${room.r_title}:${room.r_comments}:${room.r_meet_date}:${room.r_headcount}:${room.r_cur_headcount}"

                    markerType = MapPOIItem.MarkerType.BluePin
                    selectedMarkerType = MapPOIItem.MarkerType.RedPin
                    mapView.addPOIItem(marker)
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val map_binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(map_binding.root)
        apiService = com.kim.hackathon.config.init()

        mapViewContainer = map_binding.mapViewContainer

        findRoom() // roomList를 받아오기 위해 findRoom 함수 호출
        initMapView()

        requestLocationPermission()
        getCurrentLocation()
        setCurrentLocaion()
        addCustomMarker()

    }



    class CustomBalloonAdapter(inflater: LayoutInflater): CalloutBalloonAdapter {
        val mCalloutBalloon: View = inflater.inflate(R.layout.activity_room_info_simple,null)
        val name: TextView = mCalloutBalloon.findViewById(R.id.ball_tv_name)
        val comment: TextView = mCalloutBalloon.findViewById(R.id.ball_tv_comments)


        override fun getCalloutBalloon(poiItem: MapPOIItem?): View {
            try {
                // 마커 클릭 시 나오는 말풍선
                var location = poiItem?.itemName?.split(":")
                RoomDetailManger.setRoom(
                    location?.get(0)?.toInt() ?: 0,
                    location?.get(1) ?: "",
                    location?.get(2) ?: "",
                    location?.get(3) ?: "",
                    location?.get(4) ?: "",
                    location?.get(5)?.toInt() ?: 0,
                    location?.get(6)?.toInt() ?: 0
                )
//            name.text = location?.get(0)    // 해당 마커의 정보 이용 가능
//
//            comment.text = location?.get(1)
                if (location != null && location.size >= 7) {
                    name.text = location[2]
                    comment.text = location[3]
                }

                return mCalloutBalloon
            }catch (e: Exception) {
                // 예외가 발생하면 로그를 출력하고 기본 값을 설정하거나 다른 적절한 조치를 취할 수 있습니다.

                return mCalloutBalloon
            }
        }

        override fun getPressedCalloutBalloon(poiItem: MapPOIItem?): View {
            return mCalloutBalloon
        }


    }

    fun initMapView() {
        mapView = MapView(this)
        mapView.setCalloutBalloonAdapter(CustomBalloonAdapter(layoutInflater))
        mapViewContainer.addView(mapView)
        mapView.setPOIItemEventListener(eventListener)
        // 줌 레벨 변경
        mapView.setZoomLevel(3, true)
    }

    private fun requestLocationPermission() {
        val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            // 권한 요청
            val requestPermissionLauncher = registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted ->
                if (isGranted) {
                    Log.d("carrot", "Location permission granted.")
                    // 권한이 허용된 경우, 현재 위치를 가져올 수 있음
                    getCurrentLocation()

                } else {
                    Log.d("carrot", "Location permission denied.")
                    // 권한이 거부된 경우, 사용자에게 메시지를 표시하거나 다른 조치를 취할 수 있음
                    //Toast.makeText(this, "위치 권한이 필요합니다.", Toast.LENGTH_SHORT).show()

                    finish()
                }
            }
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        else {
            // 이미 권한이 허용된 경우
            getCurrentLocation()

        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        try {
            val userNowLocation: Location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)!!
            uLatitude = userNowLocation.latitude
            uLongitude = userNowLocation.longitude
        } catch (e: NullPointerException) {
            Log.e("LOCATION_ERROR", e.toString())
            finish()
        }
    }
    //얻은 내 위치정보를 기준으로 지도를 저 중앙으로 이동시킴
    private fun setCurrentLocaion() {
        val uNowPosition = MapPoint.mapPointWithGeoCoord(uLatitude, uLongitude)
        mapView.setMapCenterPoint(uNowPosition, true)
    }

    private fun addCustomMarker() {
        // 현재 위치에 마커 추가
        val marker = MapPOIItem()
        marker.apply {
            itemName = "current location"   // 마커 이름
            mapPoint = MapPoint.mapPointWithGeoCoord(uLatitude, uLongitude)   // 좌표
            //markerType = MapPOIItem.MarkerType.CustomImage          // 마커 모양 (커스텀)
            //customImageResourceId = R.drawable.maker1              // 커스텀 마커 이미지
            markerType= MapPOIItem.MarkerType.YellowPin
            setCustomImageAnchor(0.5f, 1.0f)    // 마커 이미지 기준점
            isShowCalloutBalloonOnTouch = false // 풍선뷰 터치 이벤트 비활성화

        }

        mapView.addPOIItem(marker)
    }






}

