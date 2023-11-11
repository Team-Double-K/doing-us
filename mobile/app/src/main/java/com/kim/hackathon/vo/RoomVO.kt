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