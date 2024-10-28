const User = require('../models/Usuario.js');
const jwt = require('jsonwebtoken');
const { SECRET_ACCESS_TOKEN } = require('../config/index.js'); // Asegúrate de que la ruta a config sea correcta
const Blacklist = require('../models/Blacklist.js'); // Asegúrate de importar tu modelo Blacklist

async function Verify(req, res, next) {
    try {
        const authHeader = req.headers["cookie"];
        if (!authHeader) return res.sendStatus(401); // Si no hay cookie, responde no autorizado
        const cookie = authHeader.split("=")[1];

        // Verificar si el token está en la lista negra
        const checkIfBlacklisted = await Blacklist.findOne({ token: cookie });
        if (checkIfBlacklisted) {
            return res.status(401).json({ message: "This session has expired. Please login" });
        }

        // Verifica la integridad y validez del token
        jwt.verify(cookie, SECRET_ACCESS_TOKEN, async (err, decoded) => {
            if (err) {
                return res.status(401).json({ message: "This session has expired. Please login" });
            }

            const { id } = decoded;
            const user = await User.findById(id);
            if (!user) {
                return res.status(404).json({ message: "User not found" });
            }

            const { password, ...data } = user._doc;
            req.user = data;
            next();
        });
    } catch (err) {
        res.status(500).json({
            status: "error",
            code: 500,
            data: [],
            message: "Internal Server Error",
        });
    }
}

module.exports = Verify;