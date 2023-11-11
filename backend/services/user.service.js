const { connection } = require("../config/db.config");
const crypto = require("../helpers/bcrypt.helper");
class UserService {
  users = [];
  sql = "";
  constructor() {
    connection.query("select * from user", (err, result) => {
      if (err) throw err;
      this.users = result;
    });
  }
  async findUserByRoomId(roomId) {
    return new Promise((resolve, reject) => {
      connection.query(
        `select user.u_id ,user.u_name, user.u_birth from user, (
      select u_id from user_join_room,room 
      where user_join_room.r_id = room.r_id and user_join_room.r_id = ${roomId}
      union select distinct r_owner_id as u_id from user_join_room,room 
      where user_join_room.r_id = room.r_id and user_join_room.r_id = ${roomId}) as user2 where user.u_id = user2.u_id`,
        (err, result) => {
          if (err) reject(err);
          resolve(result);
        }
      );
    });
  }
  async findUser(userId = null, userPw = null) {
    try {
      if (!userId && !userPw) return this.users;
      if (!userPw)
        return this.users.find((user) => user.u_id == userId) || null;
      return (
        this.users.find((user) => user.u_id == userId && user.u_pw == userPw) ||
        null
      );
    } catch (err) {
      throw err;
    }
  }

  createUser(user) {
    return new Promise(async (resolve, reject) => {
      this.sql = "insert into user values(?,?,?,?)";
      const hashedPw = await crypto.getHash(user.userPw);
      connection.query(
        this.sql,
        [user.userId, hashedPw, user.userName, user.birthDate],
        (err, result) => {
          if (err) reject(err);
          if (result.affectedRows) {
            this.users.push({
              u_id: user.userId,
              u_pw: user.userPw,
              u_name: user.userName,
              u_birth: user.birthDate,
            });
            resolve(true);
          } else resolve(false);
        }
      );
    });
  }
}

module.exports = UserService;
