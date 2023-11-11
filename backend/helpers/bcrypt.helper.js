const bcrypt = require("bcrypt");
const { CRYPT_SALT_ROUNDS } = require("../config/dotenv.config");

class Crypto {
  saltRounds = 0;
  salt = 0;
  constructor(saltRounds) {
    this.saltRounds = Number(saltRounds);
    this.initSalt();
  }
  async initSalt() {
    try {
      this.salt = await bcrypt.genSalt(this.saltRounds);
    } catch (err) {
      throw err;
    }
  }
  async getHash(plainText) {
    try {
      return await bcrypt.hash(plainText, this.salt);
    } catch (err) {
      throw err;
    }
  }
  async isCmp(plainText, hashText) {
    try {
      return await bcrypt.compare(plainText, hashText);
    } catch (err) {
      throw err;
    }
  }
}

module.exports = new Crypto(CRYPT_SALT_ROUNDS);
