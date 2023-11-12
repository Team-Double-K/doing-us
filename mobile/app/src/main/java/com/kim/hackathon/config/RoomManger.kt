package com.kim.hackathon.config

class RoomManger {
    companion object {
        var title: String = ""
        var comments: String = ""
        fun setRoomManger(title:String, comments:String){
            this.title = title
            this.comments = comments
        }
        fun getRoomManger():List<String>{
            return listOf(title, comments)
        }
    }
}
data class RoomDetail(

    var r_id:Int,
    var r_owner_id:String,
    var r_title:String,
    var r_comments:String,
    var r_meet_date:String,
    var r_headcount:Int,
    var r_cur_headcount:Int
)
class RoomDetailManger {
    companion object {
        var r_id:Int=0
        var r_owner_id:String=""
        var r_title:String =""
        var r_comments:String=""
        var r_meet_date:String=""
        var r_headcount:Int=0
        var r_cur_headcount:Int=0
        fun setRoom(
            r_id: Int, r_owner_id:String,
            r_title:String, r_comments:String,
            r_meet_date:String, r_headcount: Int,
            r_cur_headcount: Int
        ){

            this.r_id = r_id
            this.r_owner_id = r_owner_id
            this.r_title = r_title
            this.r_comments = r_comments
            this.r_meet_date = r_meet_date
            this.r_headcount = r_headcount
            this.r_cur_headcount = r_cur_headcount
        }
        fun getRoom():RoomDetail{
            return RoomDetail(r_id, r_owner_id, r_title,r_comments, r_meet_date, r_headcount, r_cur_headcount)
        }
    }
}