<template>
  <div class="register-cellphone">
    <h2 class="title">Registra tu celular</h2>
    <form @submit.prevent="registerCellphone" class="form">
      <label for="cellphoneName" class="form-label">Nombre del Celular:</label>
      <input
        type="text"
        id="cellphoneName"
        v-model="cellphoneName"
        class="form-input"
        placeholder="Nombre"
        required
      />

      <label for="address" class="form-label">Dirección MAC:</label>
      <input
        type="text"
        id="address"
        v-model="address"
        class="form-input"
        placeholder="MAC"
        required
      />

      <label for="osType" class="form-label">Sistema Operativo:</label>
      <select v-model="osType" id="osType" class="form-input">
        <option value="android">Android</option>
        <option value="ios">iOS</option>
      </select>

      <button type="submit" class="form-submit">Regístralo</button>
    </form>
    <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      cellphoneName: "",
      address: "",
      osType: "android",
      errorMessage: "",
    };
  },
  methods: {
    async registerCellphone() {
      try {
        const userId = localStorage.getItem("userId"); // Retrieve the user ID from localStorage
        const walletId = localStorage.getItem("selectedWalletId"); // Retrieve the wallet ID from localStorage

        if (!userId || !walletId) {
          throw new Error("User ID or Wallet ID not found. Please log in again.");
        }

        const response = await axios.post("http://35.188.126.68/celular", {
          id_billetera: walletId,
          id_usuario: userId,
          nombre_dispositivo: this.cellphoneName,
          direccion_mac: this.address,
          sistema_operativo: this.osType,
        });

        if (response.status === 201) {
          console.log("Celular registrado exitosamente");
          this.$router.push("/home"); 
        } else {
          this.errorMessage = response.data.message || "Error al registrar el celular";
        }
      } catch (error) {
        this.errorMessage = error.response?.data?.message || "Error al conectar con el servidor";
        console.error("Error during cellphone registration:", error);
      }
    },
  },
};
</script>

<style scoped>
.register-cellphone {
  padding: 2rem;
  max-width: 600px;
  margin: 0 auto;
}

.title {
  font-size: 1.5rem;
  margin-bottom: 1rem;
}

.form {
  display: flex;
  flex-direction: column;
}

.form-label {
  margin-bottom: 0.5rem;
}

.form-input {
  margin-bottom: 1rem;
  padding: 0.5rem;
  font-size: 1rem;
}

.form-submit {
  padding: 0.5rem;
  font-size: 1rem;
  cursor: pointer;
}

.error-message {
  color: red;
  margin-top: 1rem;
}
</style>