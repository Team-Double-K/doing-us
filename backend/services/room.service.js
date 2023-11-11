const { connection } = require("../config/db.config");

class RoomService {
  static findAll() {
    return new Promise((resolve, reject) => {
      connection.query("select * from room", (err, result) => {
        if (err) reject(err);
        resolve(result);
      });
    });
  }

  static findRoomOfUserByUserId(userId) {
    return new Promise((resolve, reject) => {
      connection.query(
        `select distinct room.r_id, r_owner_id,r_title, r_comments from user_join_room, room where user_join_room.r_id = room.r_id and (user_join_room.u_id = '${userId}'OR room.r_owner_id = '${userId}')`,
        (err, result) => {
          if (err) reject(err);
          resolve(result);
        }
      );
    });
  }

  static findDetailOfRoomOfUserByUserIdAndRoomId(userId, roomId) {
    return new Promise((resolve, reject) => {
      connection.query(
        `select * from 
        (select distinct room.r_id,r_owner_id,r_title, r_comments, r_meet_date, r_headcount 
        from user_join_room, room where user_join_room.r_id = room.r_id and 
        (user_join_room.u_id = '${userId}'OR room.r_owner_id = '${userId}')
        )as find where find.r_id = ${roomId}`,
        (err, result) => {
          if (err) reject(err);
          resolve(result[0] || null);
        }
      );
    });
  }
}

module.exports = RoomService;
