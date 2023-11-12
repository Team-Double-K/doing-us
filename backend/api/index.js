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
router.get("/user", defaultGuardMiddleware, findUser); //accessToken�� ���� ���� �ʿ�
router.get("/user/:userId", findUserByUserId); //accessToken�� ���� ���� �ʿ�

/**@user_room_sector */
router.get("/user/:userId/room", findRoomOfUserByUserId); //TODO: OWNER�̰ų� �Ҽ��̰ų� //accessToken�� ���� ���� �ʿ�
router.get(
  "/user/:userId/room/:roomId",
  findDetailOfRoomOfUserByUserIdAndRoomId //accessToken�� ���� ���� �ʿ�
);
router.post("/user/:userId/room", detailedGuardMiddleware, createRoomByUserId); //accessToken�� ���� ���� �ʿ�
router.post(
  "/user/:userId/room/:roomId",
  detailedGuardMiddleware,
  joinRoomByUserIdAndRoomId
);
router.delete(
  "/user/:userId/room/:roomId",
  detailedGuardMiddleware,
  deleteOrExitRoomByUserIdAndRoomId
); //owner = ���ı�TODO: user = ������,
router.put(
  "/user/:userId/room/:roomId",
  detailedGuardMiddleware,
  updateRoomByUserIdAndRoomId
); //TODO: owner�� ���� ����,

/**@only_room_sector */

router.get("/room", findRoom);

module.exports = router;
