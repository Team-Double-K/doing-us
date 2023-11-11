const { connection } = require('../config/db.config')

class RoomService {
  static findAll() {
    return new Promise((resolve, reject) => {
      connection.query('select * from room', (err, result) => {
        if (err) reject(err)
        resolve(result)
      })
    })
  }
  static joinRoomByUserIdAndRoomId(userId, roomId) {
    return new Promise((resolve, reject) => {
      connection.query('insert into user_join_room(u_id, r_id) values (?,?)', [userId, roomId], (err, result) => {
        if (err) reject(err)
        if (result.affectedRows) resolve(true)
        else reject(false)
      })
    })
  }
  static createRoomByUserId(userId, roomInfo) {
    return new Promise((resolve, reject) => {
      connection.query(
        'insert into room(r_owner_id,r_title, r_comments, r_longitude, r_latitude, r_meet_date, r_headcount) values(?,?,?,?,?,?,?)',
        [
          userId,
          roomInfo.title,
          roomInfo.comments,
          roomInfo.longitude,
          roomInfo.latitude,
          roomInfo.meetDate,
          roomInfo.headcount,
        ],
        (err, result) => {
          if (err) reject(err)
          if (result.affectedRows) resolve(true)
          else resolve(false)
        },
      )
    })
  }
  static findRoomOfUserByUserId(userId) {
    return new Promise((resolve, reject) => {
      connection.query(
        `select room.r_id, r_title, r_comments,r_meet_date,r_headcount from room where r_owner_id = "${userId}" union
        select room.r_id,r_title, r_comments,r_meet_date,r_headcount from user_join_room, room where user_join_room.r_id = room.r_id and user_join_room.u_id = "${userId}"`,
        (err, result) => {
          if (err) reject(err)
          resolve(result)
        },
      )
    })
  }

  static findDetailOfRoomOfUserByUserIdAndRoomId(userId, roomId) {
    return new Promise((resolve, reject) => {
      connection.query(
        `select * from (select room.r_id,r_title, r_comments,r_longitude,r_latitude,r_meet_date,r_headcount from room where r_owner_id = "${userId}" union
        select room.r_id,r_title, r_comments,r_longitude,r_latitude,r_meet_date,r_headcount from user_join_room, room 
        where user_join_room.r_id = room.r_id and user_join_room.u_id = "${userId}") as t1 where t1.r_id=${roomId}`,
        (err, result) => {
          if (err) reject(err)
          resolve(result[0] || null)
        },
      )
    })
  }
}

module.exports = RoomService
