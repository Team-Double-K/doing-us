package com.kim.hackathon

import KakaoAPI
import ListAdapter
import ListLayout
import ResultSearchKeyword
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.kim.hackathon.config.LocationManger
import com.kim.hackathon.databinding.ActivitySearchLocationBinding
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchMapActivity : AppCompatActivity() {

//    lateinit var mapView: MapView
    private lateinit var mapViewContainer : ViewGroup

    private var uLatitude : Double = 0.0
    private var uLongitude : Double = 0.0
    companion object {
        const val BASE_URL = "https://dapi.kakao.com/"
        const val API_KEY = "KakaoAK adf7a24378327ceec425ec5dfd86de82" // REST API 키
    }

    private lateinit var searchmapbinding : ActivitySearchLocationBinding
    private val listItems = arrayListOf<ListLayout>() // 리사이클러 뷰 아이템
    private val listAdapter = ListAdapter(listItems) // 리사이클러 뷰 어댑터
    private var pageNumber = 1 // 검색 페이지 번호
    private var keyword = "" // 검색 키워드

//    fun initMapView() {
//        mapView = MapView(this)
//        mapViewContainer.addView(mapView)
//
//        // 줌 레벨 변경
//        mapView.setZoomLevel(3, true)
//    }


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
//    private fun setCurrentLocaion() {
//        val uNowPosition = MapPoint.mapPointWithGeoCoord(uLatitude, uLongitude)
//        mapView.setMapCenterPoint(uNowPosition, true)
//    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchmapbinding = ActivitySearchLocationBinding.inflate(layoutInflater)
        setContentView(searchmapbinding.root)

        com.kim.hackathon.config.init()


// 리사이클러 뷰
        searchmapbinding.rvList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        searchmapbinding.rvList.adapter = listAdapter
// 리스트 아이템 클릭 시 해당 위치로 이동
        listAdapter.setItemClickListener(object: ListAdapter.OnItemClickListener {////@@@@@@@@@@@@@@@@
            override fun onClick(v: View, position: Int) {
                val mapPoint = MapPoint.mapPointWithGeoCoord(listItems[position].y, listItems[position].x)
                LocationManger.setPosition(listItems[position].x,listItems[position].y)
                Log.d("rere", "${listItems[position].x}, ${listItems[position].y}")

                searchmapbinding.mapView.setMapCenterPointAndZoomLevel(mapPoint, 1, true)


            }
        })

// 검색 버튼
        searchmapbinding.btnSearch.setOnClickListener {
            keyword = searchmapbinding.etSearchField.text.toString()
            pageNumber = 1
            searchKeyword(keyword, pageNumber)
        }

// 이전 페이지 버튼
        searchmapbinding.btnPrevPage.setOnClickListener {
            pageNumber--
            searchmapbinding.tvPageNumber.text = pageNumber.toString()
            searchKeyword(keyword, pageNumber)
        }

// 다음 페이지 버튼
        searchmapbinding.btnNextPage.setOnClickListener {
            pageNumber++
            searchmapbinding.tvPageNumber.text = pageNumber.toString()
            searchKeyword(keyword, pageNumber)
        }
    }

    // 키워드 검색 함수
    private fun searchKeyword(keyword: String, page: Int) {
        val retrofit = Retrofit.Builder() // Retrofit 구성
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(KakaoAPI::class.java) // 통신 인터페이스를 객체로 생성
        val call = api.getSearchKeyword(API_KEY, keyword, page) // 검색 조건 입력

// API 서버에 요청
        call.enqueue(object: Callback<ResultSearchKeyword> {
            override fun onResponse(call: Call<ResultSearchKeyword>, response: Response<ResultSearchKeyword>) {
// 통신 성공
                addItemsAndMarkers(response.body())
            }

            override fun onFailure(call: Call<ResultSearchKeyword>, t: Throwable) {
// 통신 실패
                Log.w("LocalSearch", "통신 실패: ${t.message}")
            }
        })
    }


    private fun addItemsAndMarkers(searchResult: ResultSearchKeyword?) {
        //내 위치에 마커 찍기
        val marker = MapPOIItem()
        marker.apply {
            itemName = "내 위치"
             mapPoint = MapPoint.mapPointWithGeoCoord(uLatitude, uLongitude)
        }


        if (!searchResult?.documents.isNullOrEmpty()) {
// 검색 결과 있음
            listItems.clear() // 리스트 초기화
            searchmapbinding.mapView.removeAllPOIItems() // 지도의 마커 모두 제거
            for (document in searchResult!!.documents) {
// 결과를 리사이클러 뷰에 추가
                val item = ListLayout(document.place_name,
                    document.road_address_name,
                    document.address_name,
                    document.x.toDouble(),
                    document.y.toDouble())
                listItems.add(item)

// 지도에 마커 추가
                val point = MapPOIItem()
                point.apply {
                    itemName = document.place_name
                    mapPoint = MapPoint.mapPointWithGeoCoord(document.y.toDouble(),
                        document.x.toDouble())
                    markerType = MapPOIItem.MarkerType.BluePin
                    selectedMarkerType = MapPOIItem.MarkerType.RedPin
                }
                searchmapbinding.mapView.addPOIItem(point)
            }
            listAdapter.notifyDataSetChanged()

            searchmapbinding.btnNextPage.isEnabled = !searchResult.meta.is_end // 페이지가 더 있을 경우 다음 버튼 활성화
            searchmapbinding.btnPrevPage.isEnabled = pageNumber != 1 // 1페이지가 아닐 경우 이전 버튼 활성화

        } else {
// 검색 결과 없음
            Toast.makeText(this, "검색 결과가 없습니다", Toast.LENGTH_SHORT).show()
        }
    }


}