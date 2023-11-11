const mysql2 = require("mysql2");
const {
  DB_HOST,
  DB_PORT,
  DB_NAME,
  DB_USER,
  DB_PASSWORD,
} = require("./dotenv.config");
const opt = {
  host: DB_HOST,
  port: DB_PORT,
  database: DB_NAME,
  user: DB_USER,
  password: DB_PASSWORD,
};
module.exports = {
  connection: mysql2.createConnection(opt),
};
