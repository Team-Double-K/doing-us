package com.kim.hackathon

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.kim.hackathon.RoomAdapter.Companion.accessToken

import com.kim.hackathon.databinding.ActivityLoginBinding
import com.kim.hackathon.databinding.ActivityMainBinding
import com.kim.hackathon.databinding.ActivityMapBinding
import com.kim.hackathon.utils.ApiService
import com.kim.hackathon.vo.RoomVO
import com.kim.hackathon.vo.RoomVO4
import com.kim.hackathon.vo.UserVO
import net.daum.mf.map.api.MapView
import net.daum.mf.map.api.CalloutBalloonAdapter
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
//TODO: 지도 띄우기
class MapActivity : AppCompatActivity() {
    lateinit var apiService:ApiService



    lateinit var mapView: MapView
    private lateinit var mapViewContainer : ViewGroup

    private var uLatitude : Double = 0.0
    private var uLongitude : Double = 0.0
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
    //얻은 내 위치정보를 기준으로 지도를 저 중앙으로 이동시킴
    private fun setCurrentLocaion() {
        val uNowPosition = MapPoint.mapPointWithGeoCoord(uLatitude, uLongitude)
        mapView.setMapCenterPoint(uNowPosition, true)
    }
    //



//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        val map_binding = ActivityMapBinding.inflate(layoutInflater)
//        setContentView(map_binding.root)
//        apiService = com.kim.hackathon.config.init()
//
//        mapViewContainer = map_binding.mapViewContainer
//
//        initMapView()
//
//
//        findRoom()
//        setMarkersOnMap()
//
//    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val map_binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(map_binding.root)
        apiService = com.kim.hackathon.config.init()

        mapViewContainer = map_binding.mapViewContainer

        findRoom() // roomList를 받아오기 위해 findRoom 함수 호출
        initMapView()

    }

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
                    itemName = room.r_title
                    markerType = MapPOIItem.MarkerType.BluePin
                    selectedMarkerType = MapPOIItem.MarkerType.RedPin
                    mapView.addPOIItem(marker)
                }
            }
        }
    }












    //받아온 데이터 마커 위에 표시
//    private fun setMissionMark(dataList: ArrayList<TargetMapData>, color:String) {
//        for (data in dataList) {
//
//            val marker = MapPOIItem()
//            marker.apply {
//
//
//                mapPoint = MapPoint.mapPointWithGeoCoord(data.sf_latitude, data.sf_longitude)
//                when(color){
//                    "yellow"->{
//                        itemName = "${data.sf_name}|${data.sf_addr}|${data.sf_id}|${uLatitude}|${uLongitude}|${data.sf_latitude}|${data.sf_longitude}|1"
//                        markerType = MapPOIItem.MarkerType.YellowPin}
//                    "blue"->{
//                        itemName = "${data.sf_name}|${data.sf_addr}|${data.sf_id}|${uLatitude}|${uLongitude}|${data.sf_latitude}|${data.sf_longitude}|0"
//                        markerType = MapPOIItem.MarkerType.BluePin}
//                }
//                selectedMarkerType = MapPOIItem.MarkerType.RedPin
//                isCustomImageAutoscale = false
//                setCustomImageAnchor(0.5f, 1.0f)
//
//
//
//            }
//            mapView.addPOIItem(marker)
//
//        }
//    }











//    private fun setCurrentLocaion() {
//        val uNowPosition = MapPoint.mapPointWithGeoCoord(uLatitude, uLongitude)
//        mapView.setMapCenterPoint(uNowPosition, true)
//
//        Log.d("Map", "${uLatitude}, ${uLongitude}")
//    }

    class CustomBalloonAdapter(inflater: LayoutInflater): CalloutBalloonAdapter {
        val mCalloutBalloon: View = inflater.inflate(R.layout.activity_ballon,null)
        val name: TextView = mCalloutBalloon.findViewById(R.id.ball_tv_name)
        val address: TextView = mCalloutBalloon.findViewById(R.id.ball_tv_address)

        override fun getCalloutBalloon(poiItem: MapPOIItem?): View {
            // 마커 클릭 시 나오는 말풍선
            var facilities = poiItem?.itemName?.split("|");
            name.text = facilities?.get(0)    // 해당 마커의 정보 이용 가능
            if(facilities?.size ==8) address.text = facilities?.get(1)
            else address.text = "current location"

            return mCalloutBalloon
        }

        override fun getPressedCalloutBalloon(poiItem: MapPOIItem?): View {
            return mCalloutBalloon
        }


    }

    fun initMapView() {
        mapView = MapView(this)
        mapView.setCalloutBalloonAdapter(CustomBalloonAdapter(layoutInflater))
        mapViewContainer.addView(mapView)

        // 줌 레벨 변경
        mapView.setZoomLevel(3, true)
    }






}

