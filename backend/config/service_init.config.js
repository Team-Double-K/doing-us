const UserService = require('../services/user.service')

const userService = new UserService()
console.log('userService init succeed .. !')

module.exports = { userService }
