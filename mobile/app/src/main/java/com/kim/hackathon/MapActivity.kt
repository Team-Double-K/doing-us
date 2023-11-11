package com.kim.hackathon

import android.content.Intent
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



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val map_binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(map_binding.root)
        apiService = com.kim.hackathon.config.init()

        mapViewContainer = map_binding.mapViewContainer

        initMapView()



    }
   // private fun postroom@@








    private fun setCurrentLocaion() {
        val uNowPosition = MapPoint.mapPointWithGeoCoord(uLatitude, uLongitude)
        mapView.setMapCenterPoint(uNowPosition, true)

        Log.d("Map", "${uLatitude}, ${uLongitude}")
    }

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
//        mapView.setPOIItemEventListener(eventListener)
        mapViewContainer.addView(mapView)

        // 줌 레벨 변경
        mapView.setZoomLevel(3, true)
    }






}

