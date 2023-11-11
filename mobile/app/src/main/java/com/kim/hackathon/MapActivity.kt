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

import com.kim.hackathon.databinding.ActivityLoginBinding
import com.kim.hackathon.databinding.ActivityMainBinding
import com.kim.hackathon.databinding.ActivityMapBinding
import com.kim.hackathon.utils.ApiService
import com.kim.hackathon.vo.UserVO
import net.daum.mf.map.api.MapView
import net.daum.mf.map.api.CalloutBalloonAdapter
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import okhttp3.ResponseBody
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
    //



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val map_binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(map_binding.root)
        apiService = com.kim.hackathon.config.init()

        mapViewContainer = map_binding.mapViewContainer

        initMapView()




    }
   // private fun postroom@@








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

