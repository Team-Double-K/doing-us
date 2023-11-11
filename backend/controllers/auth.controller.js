const CustomError = require("../helpers/custom_error.helper");
const tokenHelper = require("../helpers/token.helper");
const AuthService = require("../services/auth.service");
const login = async (req, res, next) => {
  try {
    const { userId, userPw } = req.body;
    if (!userId || !userPw) throw new CustomError("The wrong approach!", 401);
    const result = await AuthService.login(userId, userPw);
    result.msg = "welcome to this service";
    res.send(result);
  } catch (err) {
    next(err);
  }
};
const join = async (req, res, next) => {
  try {
    const userForJoin = req.body;
    const result = await AuthService.join(userForJoin);
    if (!result) throw new CustomError("expect ambiguous error", 400);

    const payload = {
      userId: userForJoin.userId,
      userName: userForJoin.userName,
    };
    const accessToken = await tokenHelper.issue(payload);

    if (await AuthService.saveToken(userForJoin.userId, accessToken)) {
      res.send({
        msg: "welcome to successfully join",
        state: 1,
        accessToken: accessToken,
      });
    }
  } catch (err) {
    next(err);
  }
};
const logout = async (req, res, next) => {
  try {
    //token ÆÄ±â
    const { accessToken } = req.body;
    await AuthService.removeToken(accessToken);
    res.send({
      state: 1,
      msg: "logout successful",
    });
  } catch (err) {
    next(err);
  }
};

module.exports = {
  login: login,
  join: join,
  logout: logout,
};
