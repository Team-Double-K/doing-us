package com.kim.hackathon.vo

data class RoomVO (
    var ownerId:String?,
    var title:String?,
    var comments:String?,
    var longitude:Double?,
    var latitude:Double?,
    var meetDate:String?,
    var headcount:Int?
)
data class RoomVO2 (
    var r_id:Int?,
    var r_title:String?,
    var r_comments:String?,
    var r_meet_date:String?,
    var r_headcount:Int?
)

<<<<<<< HEAD
data class RoomVO3 (
    var title:String?,
    var comments:String?,
    var longitude:Double?,
    var latitude:Double?,
    var meetDate:String?,
    var headcount:Int?
=======
data class RoomVO4 (
    var r_id:Int?,
    var r_ownerId:String?,
    var r_title:String?,
    var r_comments:String?,
    var r_longitude:Double?,
    var r_latitude:Double?,
    var r_meet_date:String?,
    var r_headcount:Int?
>>>>>>> b
)

/**
 *  r_id int not null auto_increment primary key,
 *    r_owner_id varchar(255),
 *    r_title varchar(255),
 *    r_comments varchar(255),
 *     r_longitude double not null,
 *     r_latitude double not null,
 *    r_meet_date datetime,
 *    headcount
 */