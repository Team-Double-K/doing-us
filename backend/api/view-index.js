const router = require("express").Router();

router.get("/", (req, res) => {
  res.render("index");
});
router.get("/main", (req, res) => {
  res.render("main");
});

router.post("/login/callback", (req, res) => {
  const { userId, userPw } = req.body;
});

module.exports = router;
