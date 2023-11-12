package com.kim.hackathon

import ListAdapter
import ListLayout
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.kakao.util.maps.helper.Utility
import com.kim.hackathon.databinding.ActivityLoginBinding
import com.kim.hackathon.databinding.ActivityMainBinding
import com.kim.hackathon.databinding.ActivityMapBinding
import com.kim.hackathon.databinding.ActivityRegisterRoomBinding
import com.kim.hackathon.utils.ApiService
import com.kim.hackathon.vo.UserVO
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    lateinit var apiService:ApiService
    lateinit var login_binding:ActivityLoginBinding
    lateinit var main_binding:ActivityMainBinding
    lateinit var registerroom_binding:ActivityRegisterRoomBinding
    lateinit var map_binding:ActivityMapBinding
    private val listItems = arrayListOf<ListLayout>() // 리사이클러 뷰 아이템
    private val listAdapter = ListAdapter(listItems) // 리사이클러 뷰 어댑터
    private var pageNumber = 1 // 검색 페이지 번호
    private var keyword = "" // 검색 키워드

    companion object {
        const val BASE_URL = "https://dapi.kakao.com/"
        const val API_KEY = "KakaoAK 028459d34f4c5a732cb3562015fd8f82" // REST API 키
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("abc", "keyhash : ${Utility.getKeyHash(this)}")
        var map_intent = Intent(this, MapActivity::class.java)
        var register_intent = Intent(this, RegisterRoomActivity::class.java)
        var myRoom_intent = Intent(this, MyRoom::class.java)
        login_binding = ActivityLoginBinding.inflate(layoutInflater)
        main_binding = ActivityMainBinding.inflate(layoutInflater) //main_binding
        registerroom_binding = ActivityRegisterRoomBinding.inflate(layoutInflater)
        map_binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(main_binding.root)
        apiService = com.kim.hackathon.config.init()

        main_binding.searchRoom.setOnClickListener {
            startActivity(map_intent)

//                //TODO: 레트로핏

        }


        main_binding.registerRoom.setOnClickListener {
            startActivity(register_intent)

        }

        main_binding.manageMyroom.setOnClickListener {
            startActivity(myRoom_intent)
        }



    }


    //
    //받아온 데이터 마커 위에 표시
//    private fun setMissionMark(dataList: ArrayList<TargetMapData>, color:String) {
//        for (data in dataList) {
//
//            2
//            val marker = MapPOIItem()
//            marker.apply {
//
//
//
//                mapPoint = MapPoint.mapPointWithGeoCoord(data.sf_latitude, data.sf_longitude)
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
//
//
//
//    }
    //





}