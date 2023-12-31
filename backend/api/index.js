const { login, join, logout } = require("../controllers/auth.controller");
const {
  findRoomOfUserByUserId,
  findDetailOfRoomOfUserByUserIdAndRoomId,
  createRoomByUserId,
  joinRoomByUserIdAndRoomId,
  findRoom,
  deleteOrExitRoomByUserIdAndRoomId,
  updateRoomByUserIdAndRoomId,
} = require("../controllers/room.controller");
const {
  findUser,
  findUserByUserId,
} = require("../controllers/user.controller");
const {
  defaultGuardMiddleware,
  detailedGuardMiddleware,
} = require("../middlewares/guard.middleware");

const router = require("express").Router();

/**@auth_sector */ //fin!!
router.post("/auth/join", join);
router.post("/auth/login", login);
router.get("/auth/logout", logout);

/**@user_sector */
router.get("/user", defaultGuardMiddleware, findUser); //accessToken을 통한 인증 필요
router.get("/user/:userId", findUserByUserId); //accessToken을 통합 인증 필요

/**@user_room_sector */
router.get("/user/:userId/room", findRoomOfUserByUserId); //TODO: OWNER이거나 소속이거나 //accessToken을 통한 인증 필요
router.get(
  "/user/:userId/room/:roomId",
  findDetailOfRoomOfUserByUserIdAndRoomId //accessToken을 통한 인증 필요
);
router.post("/user/:userId/room", detailedGuardMiddleware, createRoomByUserId); //accessToken을 통한 인증 필요
router.post(
  "/user/:userId/room/:roomId",
  detailedGuardMiddleware,
  joinRoomByUserIdAndRoomId
);
router.delete(
  "/user/:userId/room/:roomId",
  detailedGuardMiddleware,
  deleteOrExitRoomByUserIdAndRoomId
); //owner = 방파괴TODO: user = 나가기,
router.put(
  "/user/:userId/room/:roomId",
  detailedGuardMiddleware,
  updateRoomByUserIdAndRoomId
); //TODO: owner만 수정 가능,

/**@only_room_sector */

router.get("/room", findRoom);

module.exports = router;
