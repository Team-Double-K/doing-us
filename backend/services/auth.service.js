const { connection } = require("../config/db.config");
const { userService } = require("../config/service_init.config");
const bcryptHelper = require("../helpers/bcrypt.helper");
const CustomError = require("../helpers/custom_error.helper");
const token = require("../helpers/token.helper");

class AuthService {
  static async login(userId, userPw) {
    try {
      const findUser = await userService.findUser(userId);
      if (!findUser)
        throw new CustomError("not exist user, plz check your id", 401);
      if (!(await bcryptHelper.isCmp(userPw, findUser.u_pw)))
        throw new CustomError(
          "doesn't correct your password,plz check your password "
        );
      const result = await this.validExistTokenAndGetToken(userId);

      if (result)
        return {
          state: 1,
          accessToken: result.accessToken,
        };

      const payload = {
        userId: userId,
        userName: userName,
      };
      const accessToken = await token.issue(payload);
      return {
        state: 1,
        accessToken: accessToken,
      };
    } catch (err) {
      throw err;
    }
  }
  static async join(user) {
    try {
      const findUser = await userService.findUser(user.userId);
      if (findUser) throw new CustomError("already exist user", 401);

      if (!(await userService.createUser(user)))
        throw new CustomError("when create user, expect error", 401);
      return true;
    } catch (err) {
      throw err;
    }
  }
  static validExistTokenAndGetToken(userId) {
    return new Promise((resolve, reject) => {
      connection.query(
        `select * from token where u_id = '${userId}'`,
        (err, result) => {
          if (err) reject(err);
          resolve(result[0]);
        }
      );
    });
  }
  static saveToken(userId, token) {
    return new Promise((resolve, reject) => {
      connection.query(
        "insert into token values(?,?)",
        [userId, token],
        (err, result) => {
          if (err) reject(err);
          if (result.affectedRows) resolve(true);
          else resolve(false);
        }
      );
    });
  }
  static removeToken(token) {
    return new Promise((resolve, reject) => {
      connection.query(
        `delete from token where accessToken = '${token}'`,
        (err, result) => {
          if (err) reject(err);
        }
      );
    });
  }
}

module.exports = AuthService;
