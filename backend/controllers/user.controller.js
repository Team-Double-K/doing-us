const { userService } = require('../config/service_init.config')
const CustomError = require('../helpers/custom_error.helper')

const findUser = async (req, res, next) => {
  try {
    const { roomId } = req.query
    if (roomId) {
      const user = await userService.findUserByRoomId(roomId)
      return res.send(user)
    }
    const users = await userService.findUser()
    return res.send(
      users
        .map(({ u_pw, ...etc }) => etc)
        .map((user) => ({
          userId: user.u_id,
          userName: user.u_name,
          birthDate: user.u_birth,
        })),
    )
  } catch (err) {
    next(err)
  }
}
const findUserByUserId = async (req, res, next) => {
  try {
    const { userId } = req.params
    const user = await userService.findUser(userId)

    if (!user) throw new CustomError('no exist user', 404)
    res.send({
      userId: user.u_id,
      userName: user.u_name,
      birthDate: user.u_birth,
    })
  } catch (err) {
    next(err)
  }
}

module.exports = {
  findUser: findUser,
  findUserByUserId: findUserByUserId,
}
