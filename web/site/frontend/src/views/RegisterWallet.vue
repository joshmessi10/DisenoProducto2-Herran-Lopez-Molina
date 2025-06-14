<template>
  <div class="register-wallet">
    <h2 class="title">Selecciona tu Billetera Inteligente</h2>
    <form @submit.prevent="selectWallet" class="form">
      <label for="walletSelect" class="form-label">Selecciona una Billetera:</label>
      <select
        id="walletSelect"
        v-model="selectedWalletId"
        class="form-input"
        required
      >
        <option v-for="wallet in wallets" :key="wallet.id" :value="wallet.id">
          {{ wallet.name }}
        </option>
      </select>
      <button type="submit" class="form-submit">Seleccionar</button>
    </form>
    <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      wallets: [],
      selectedWalletId: "",
      errorMessage: "",
    };
  },
  async created() {
    try {
      const userId = localStorage.getItem("userId"); // Retrieve the user ID from localStorage
      if (!userId) {
        throw new Error("User ID not found. Please log in again.");
      }

      const response = await axios.get(`http://35.188.126.68/billetera`, {
        params: { id_usuario: userId },
      });

      if (response.status !== 200) {
        throw new Error("Failed to fetch wallets");
      }

      const data = response.data;
      this.wallets = data.names.map((name, index) => ({
        id: data.ids[index],
        name: name,
      }));
    } catch (error) {
      this.errorMessage = error.message;
    }
  },
  methods: {
    selectWallet() {
      try {
        if (!this.selectedWalletId) {
          throw new Error("Please select a wallet");
        }

        // Store the selected wallet ID in localStorage or handle it as needed
        localStorage.setItem("selectedWalletId", this.selectedWalletId);
        this.$router.push("/register-cellphone"); // Redirect to the next step
      } catch (error) {
        this.errorMessage = error.message;
      }
    },
  },
};
</script>

<style scoped>
.register-wallet {
  padding: 2rem;
  max-width: 600px;
  margin: auto;
  background: #f9f9f9;
  border-radius: 10px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}

.title {
  text-align: center;
  font-size: 1.8rem;
  color: #333;
}

.form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.form-label {
  font-size: 1rem;
  color: #555;
}

.form-input {
  padding: 10px;
  font-size: 1rem;
  border: 1px solid #ddd;
  border-radius: 5px;
}

.form-submit {
  background-color: #1ab188;
  color: white;
  border: none;
  padding: 1rem;
  cursor: pointer;
  border-radius: 5px;
  transition: background-color 0.3s;
}

.form-submit:hover {
  background-color: #0b9185;
}

.error-message {
  color: red;
  text-align: center;
}
</style>
