const JWT = require('jsonwebtoken')
const { JWT_SECRET_KEY } = require('../config/dotenv.config')
const CustomError = require('./custom_error.helper')
class Token {
  secretKey = ''
  expiresIn = ''
  constructor(secretKey) {
    this.secretKey = secretKey
    this.expiresIn = '30d'
  }

  issue(payload) {
    return new Promise((resolve, reject) => {
      JWT.sign(payload, this.secretKey, { expiresIn: this.expiresIn }, (err, accessToken) => {
        if (err) reject(err)
        resolve(accessToken)
      })
    })
  }

  verify(accessToken) {
    return new Promise((resolve, reject) => {
      JWT.verify(accessToken, this.secretKey, (err, decoded) => {
        if (err) {
          if (err.name === 'TokenExpiredError') {
            reject(new CustomError('Token expired', 401))
          } else {
            reject(new CustomError('Forbidden', 404))
          }
        }
        resolve(decoded)
      })
    })
  }
}

module.exports = new Token(JWT_SECRET_KEY)
