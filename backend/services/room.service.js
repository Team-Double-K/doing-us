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
  static joinRoomByUserIdAndRoomId(userId, roomId) {
    return new Promise((resolve, reject) => {
      connection.query(
        "insert into user_join_room(u_id, r_id) values (?,?)",
        [userId, roomId],
        (err, result) => {
          if (err) reject(err);
          if (result.affectedRows) resolve(true);
          else reject(false);
        }
      );
    });
  }
  static createRoomByUserId(userId, roomInfo) {
    return new Promise((resolve, reject) => {
      connection.query(
        "insert into room(r_owner_id,r_title, r_comments, r_longitude, r_latitude, r_meet_date, r_headcount) values(?,?,?,?,?,?,?)",
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
          if (err) reject(err);
          if (result.affectedRows) resolve(true);
          else resolve(false);
        }
      );
    });
  }
  static isOwnerForRoom(userId, roomId) {
    return new Promise((resolve, reject) => {
      connection.query(
        `select * from room where r_id =${roomId} and r_owner_id = '${userId}'`,
        (err, result) => {
          if (err) reject(err);
          resolve(result[0]);
        }
      );
    });
  }

  static deleteRoomByUserIdAndRoomId(userId, roomId) {
    return new Promise((resolve, reject) => {
      connection.query(
        `delete from room where r_owner_id = '${userId}' and r_id=${roomId}`,
        (err, result) => {
          if (err) reject(err);
          if (result.affectedRows) resolve(true);
          else resolve(false);
        }
      );
    });
  }

  static updateRoomByUserIdAndRoomId(userId, roomId, updatedRoomInfo) {
    return new Promise((resolve, reject) => {
      connection.query(
        `update room set r_title='${updatedRoomInfo.title}', r_comments='${updatedRoomInfo.comments}', r_longitude=${updatedRoomInfo.longitude}, r_latitude=${updatedRoomInfo.latitude}, r_meet_date='${updatedRoomInfo.meetDate}',r_headcount= ${updatedRoomInfo.headcount} where r_owner_id = '${userId}' and r_id=${roomId}`,
        (err, result) => {
          console.log(err);
          if (err) reject(err);
          if (result.affectedRows) resolve(true);
          else resolve(false);
        }
      );
    });
  }

  static exitRoomByUserIdAndRoomId(userId, roomId) {
    return new Promise((resolve, reject) => {
      connection.query(
        `delete from user_join_room where u_id = '${userId}' and r_id = ${roomId}`,
        (err, result) => {
          if (err) reject(err);
          console.log(result);
          if (result.affectedRows) resolve(true);
          else resolve(false);
        }
      );
    });
  }
  static findRoomOfUserByUserId(userId) {
    return new Promise((resolve, reject) => {
      connection.query(
        `select room.r_id, r_title, r_comments,r_meet_date,r_headcount from room where r_owner_id = "${userId}" union
        select room.r_id,r_title, r_comments,r_meet_date,r_headcount from user_join_room, room where user_join_room.r_id = room.r_id and user_join_room.u_id = "${userId}"`,
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
        `select * from (select room.r_id,r_title, r_comments,r_longitude,r_latitude,r_meet_date,r_headcount from room where r_owner_id = "${userId}" union
        select room.r_id,r_title, r_comments,r_longitude,r_latitude,r_meet_date,r_headcount from user_join_room, room 
        where user_join_room.r_id = room.r_id and user_join_room.u_id = "${userId}") as t1 where t1.r_id=${roomId}`,
        (err, result) => {
          if (err) reject(err);
          resolve(result[0] || null);
        }
      );
    });
  }
}

module.exports = RoomService;
