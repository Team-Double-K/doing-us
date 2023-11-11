const CustomError = require('../helpers/custom_error.helper')
const RoomService = require('../services/room.service')

const findRoom = async (req, res, next) => {
  try {
    const result = await RoomService.findAll()
    res.send(result)
  } catch (err) {
    next(err)
  }
}

const findRoomOfUserByUserId = async (req, res, next) => {
  try {
    const { userId } = req.params

    const result = await RoomService.findRoomOfUserByUserId(userId)
    res.send(result)
  } catch (err) {
    next(err)
  }
}
const findDetailOfRoomOfUserByUserIdAndRoomId = async (req, res, next) => {
  try {
    const { userId, roomId } = req.params

    const result = await RoomService.findDetailOfRoomOfUserByUserIdAndRoomId(userId, roomId)
    if (!result) throw new CustomError('failed searching data, not exist data', 400)
    res.send(result)
  } catch (err) {
    next(err)
  }
}
const createRoomByUserId = async (req, res, next) => {
  try {
    const { userId } = req.params
    const roomInfo = req.body
    if (!(await RoomService.createRoomByUserId(userId, roomInfo)))
      throw new CustomError('expect error, when create room', 400)

    res.send({
      state: 1,
      msg: 'successful to create room that is your owns',
    })
  } catch (err) {
    next(err)
  }
}
const joinRoomByUserIdAndRoomId = async (req, res, next) => {
  try {
    const { userId, roomId } = req.params
    if (!(await RoomService.joinRoomByUserIdAndRoomId(userId, roomId)))
      throw new CustomError('error joining room, check roomId or userId', 400)

    res.send({
      state: 1,
      msg: 'successful joining room ',
    })
  } catch (err) {
    next(err)
  }
}
const deleteRoomByUserIdAndRoomId = async (req, res, next) => {}
const updateOrExitRoomByUserIdAndRoomId = async (req, res, next) => {}

module.exports = {
  findRoom: findRoom,
  findRoomOfUserByUserId: findRoomOfUserByUserId,
  findDetailOfRoomOfUserByUserIdAndRoomId: findDetailOfRoomOfUserByUserIdAndRoomId,
  createRoomByUserId: createRoomByUserId,
  joinRoomByUserIdAndRoomId: joinRoomByUserIdAndRoomId,
  deleteRoomByUserIdAndRoomId: deleteRoomByUserIdAndRoomId,
  updateOrExitRoomByUserIdAndRoomId: updateOrExitRoomByUserIdAndRoomId,
}
