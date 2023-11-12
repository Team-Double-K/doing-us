const CustomError = require("../helpers/custom_error.helper");

module.exports = (err, req, res, next) => {
  if (err instanceof CustomError) {
    res
      .send({
        msg: err.message,
      })
      .status(err.statusCode);
  } else {
    res
      .send({
        msg: err.message,
      })
      .status(400);
  }
};
