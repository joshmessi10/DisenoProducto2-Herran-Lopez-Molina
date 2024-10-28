const dotenv = require('dotenv');
dotenv.config();

const { URI, PORT, SECRET_ACCESS_TOKEN } = process.env;
console.log(process.env.SECRET_ACCESS_TOKEN);

module.exports = { URI, PORT, SECRET_ACCESS_TOKEN };
