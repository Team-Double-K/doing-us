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