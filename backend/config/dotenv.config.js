require("dotenv").config();

module.exports = {
  DB_HOST: process.env.DB_HOST,
  DB_PORT: process.env.DB_PORT,
  DB_USER: process.env.DB_USER,
  DB_PASSWORD: process.env.DB_PASSWORD,
  DB_NAME: process.env.DB_NAME,
  CRYPT_SALT_ROUNDS: process.env.CRYPT_SALT_ROUNDS,
  JWT_SECRET_KEY: process.env.JWT_SECRET_KEY,
};
