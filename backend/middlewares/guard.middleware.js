const CustomError = require('../helpers/custom_error.helper')
const tokenHelper = require('../helpers/token.helper')
const defaultGuardMiddleware = async (req, res, next) => {
  try {
    const accessToken = req.headers['authorization']
    console.log(accessToken)
    const payload = await tokenHelper.verify(accessToken)
    next()
  } catch (err) {
    return res.send({ msg: err.message })
  }
}
const detailedGuardMiddleware = async (req, res, next) => {
  try {
    const { userId } = req.params
    const accessToken = req.headers['authorization']
    console.log(accessToken)
    const payload = await tokenHelper.verify(accessToken)
    //not matched userId and accessToken
    if (userId !== payload.userId) throw new CustomError('wrong access .. !, plz try again', 404)
    next()
  } catch (err) {
    return res.send({ msg: err.message })
  }
}
module.exports = {
  defaultGuardMiddleware,
  detailedGuardMiddleware,
}
