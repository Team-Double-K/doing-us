const { login, join, logout } = require("../controllers/auth.controller");
const {
  findRoomOfUserByUserId,
  findDetailOfRoomOfUserByUserIdAndRoomId,
  createRoomByUserId,
  joinRoomByUserIdAndRoomId,
  deleteRoomByUserIdAndRoomId,
  updateOrExitRoomByUserIdAndRoomId,
  findRoom,
} = require("../controllers/room.controller");
const {
  findUser,
  findUserByUserId,
  findUserByRoomId,
} = require("../controllers/user.controller");

const router = require("express").Router();

/**@auth_sector */ //fin!!
router.post("/auth/join", join);
router.post("/auth/login", login);
router.get("/auth/logout", logout);

/**@user_sector */
router.get("/user", findUser);
router.get("/user/:userId", findUserByUserId);

/**@user_room_sector */
router.get("/user/:userId/room", findRoomOfUserByUserId); //TODO: OWNER이거나 소속이거나
router.get(
  "/user/:userId/room/:roomId",
  findDetailOfRoomOfUserByUserIdAndRoomId
);
router.post("/user/:userId/room", createRoomByUserId);
router.post("/user/userId/room/:roomId", joinRoomByUserIdAndRoomId);
router.delete("/user/:userId/room/:roomId", deleteRoomByUserIdAndRoomId); //owner = 방파괴
router.put("/user/:userId/room/:roomId", updateOrExitRoomByUserIdAndRoomId); //TODO: owner만 수정 가능, TODO: user = 나가기,

/**@only_room_sector */

router.get("/room", findRoom);

module.exports = router;
