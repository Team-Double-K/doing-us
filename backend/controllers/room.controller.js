const CustomError = require("../helpers/custom_error.helper");
const RoomService = require("../services/room.service");

const findRoom = async (req, res, next) => {
  try {
    const result = await RoomService.findAll();
    res.send(result);
  } catch (err) {
    next(err);
  }
};
const findRoomByRoomId = async (req, res, next) => {
  try {
    const { roomId } = req.params;
    const result = await RoomService.findRoomByRoomId(roomId);
    res.send(result);
  } catch (err) {
    next(err);
  }
};

const findRoomOfUserByUserId = async (req, res, next) => {
  try {
    const { userId } = req.params;

    const result = await RoomService.findRoomOfUserByUserId(userId);
    res.send(result);
  } catch (err) {
    next(err);
  }
};
const findDetailOfRoomOfUserByUserIdAndRoomId = async (req, res, next) => {
  try {
    const { userId, roomId } = req.params;

    const result = await RoomService.findDetailOfRoomOfUserByUserIdAndRoomId(
      userId,
      roomId
    );
    if (!result)
      throw new CustomError("failed searching data, not exist data", 400);
    res.send(result);
  } catch (err) {
    next(err);
  }
};
const createRoomByUserId = async (req, res, next) => {
  try {
    const { userId } = req.params;
    const roomInfo = req.body;
    if (!(await RoomService.createRoomByUserId(userId, roomInfo)))
      throw new CustomError("expect error, when create room", 400);

    res.send({
      state: 1,
      msg: "successful to create room that is your owns",
    });
  } catch (err) {
    next(err);
  }
};
const joinRoomByUserIdAndRoomId = async (req, res, next) => {
  try {
    const { userId, roomId } = req.params;
    if (!(await RoomService.joinRoomByUserIdAndRoomId(userId, roomId)))
      throw new CustomError("error joining room, check roomId or userId", 400);

    res.send({
      state: 1,
      msg: "successful joining room ",
    });
  } catch (err) {
    next(err);
  }
};

/**
 * TODO: exit room and delete room
 */
const deleteOrExitRoomByUserIdAndRoomId = async (req, res, next) => {
  try {
    const { userId, roomId } = req.params;
    if (await RoomService.isOwnerForRoom(userId, roomId)) {
      //ownerÀÏ ¶§,
      if (!(await RoomService.deleteRoomByUserIdAndRoomId(userId, roomId)))
        throw new CustomError("It was not processed properly.", 401);

      res.send({
        state: 1,
        msg: "successful to delete room",
      });
    } else {
      //owner¾Æ´Ò¶§
      if (!(await RoomService.exitRoomByUserIdAndRoomId(userId, roomId)))
        throw new CustomError("It was not processed properly.", 401);

      res.send({
        state: 1,
        msg: "successful to exit room",
      });
    }
  } catch (err) {
    next(err);
  }
};

const updateRoomByUserIdAndRoomId = async (req, res, next) => {
  try {
    const { userId, roomId } = req.params;
    const roomInfo = req.body;
    if (!(await RoomService.isOwnerForRoom(userId, roomId)))
      throw new CustomError("wrong access ..!", 404);

    if (
      !(await RoomService.updateRoomByUserIdAndRoomId(userId, roomId, roomInfo))
    )
      throw new CustomError("It was not processed properly.", 401);

    res.send({
      state: 1,
      msg: "successful to update room",
    });
  } catch (err) {
    next(err);
  }
};

module.exports = {
  findRoom: findRoom,
  findRoomOfUserByUserId: findRoomOfUserByUserId,
  findDetailOfRoomOfUserByUserIdAndRoomId:
    findDetailOfRoomOfUserByUserIdAndRoomId,
  createRoomByUserId: createRoomByUserId,
  joinRoomByUserIdAndRoomId: joinRoomByUserIdAndRoomId,
  deleteOrExitRoomByUserIdAndRoomId: deleteOrExitRoomByUserIdAndRoomId,
  updateRoomByUserIdAndRoomId: updateRoomByUserIdAndRoomId,
  findRoomByRoomId: findRoomByRoomId,
};
