package com.kim.hackathon.utils
import com.kim.hackathon.vo.RoomVO
import com.kim.hackathon.vo.UserForJoinVO
import com.kim.hackathon.vo.UserVO
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiService {
    /**
     * @auth_sector
     * login, join, logout
     */
    @POST
    fun login(@Url url:String?,@Body body:UserVO?):Call<ResponseBody>
    @POST
    fun join(@Url url:String?,@Body body:UserForJoinVO?):Call<ResponseBody>
    @GET
    fun logout(@Url url:String?):Call<ResponseBody>

    /**
     * @user_sector
     * findAllUser, findUserByUserId
     */
    @GET
    fun findAllUser(@Url url:String?):Call<ResponseBody>
    @GET //::url/+:id -> id 는 userid(계정)
    fun findUserByUserId(@Url url:String?):Call<ResponseBody>

    /**
     * @user_room_sector
     * findRoomOfUserByUserId, findDetailOfRoomOfUserByUserIdAndRoomId,
     * createRoomByUserId, joinRoomByUserIdAndRoomId
     * deleteRoomByUserIdAndRoomId, exitRoomByUserIdAndRoomId
     * updateRoomByUserIdAndRoomId
     */
    @GET
    fun findRoomOfUserByUserId(@Url url:String?):Call<ResponseBody>
    @GET//url:8000/api/user/:userId/room/:roomId (콜론은 다 변수로 넣어 줘야 해 ~)
    fun findDetailOfRoomOfUserByUserIdAndRoomId(@Url url:String?):Call<ResponseBody>
    @POST
    fun createRoomByUserId(@Url url: String?, @Body body:RoomVO?):Call<ResponseBody>
    @POST
    fun joinRoomByUserIdAndRoomId(@Url url: String?):Call<ResponseBody>
    @DELETE // 방삭제 TODO: OWNER만 수행가능
    fun deleteRoomByUserIdAndRoomId(@Url url:String?):Call<ResponseBody>
    @PUT // 방나가기 TODO: OWNER를 제외한 anyuser 다 가능
    fun updateOrExitRoomByUserIdAndRoomId(@Url url:String?):Call<ResponseBody>
}
