const express = require("express");
const { connection } = require("./config/db.config");
const apiRouter = require("./api/index");
const errorMiddleware = require("./middlewares/error.middleware");
const app = express();
const port = 8000;

app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use("/api", apiRouter);
app.use(errorMiddleware);
connection.connect((err) => {
  if (err) throw err;
  console.log("db connection succeed");
});
app.listen(port, () => {
  console.log("server open");
});
