<template>
  <div class="alertas">
    <h1 class="title">Alerta de Seguridad Más Reciente</h1>
    <ul class="alert-list">
      <li v-for="alerta in alertas" :key="alerta.id" class="alert-item">
        <p><strong>Alerta:</strong> {{ alerta.tipo_evento }}</p>
        <p><strong>Nivel de Batería:</strong> {{ alerta.nivel_bateria }}%</p>
        <p><strong>Fecha:</strong> {{ alerta.fecha_hora }}</p>
      </li>
    </ul>
    <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "AlertasView",
  data() {
    return {
      alertas: [],
      errorMessage: "",
      lastFechaHora: null, // To track the last fecha_hora
    };
  },
  async created() {
    this.fetchAlertas();
    this.interval = setInterval(this.fetchAlertas, 5000); // Fetch data every 5 seconds
  },
  beforeDestroy() {
    clearInterval(this.interval); // Clear the interval when the component is destroyed
  },
  methods: {
    async fetchAlertas() {
      try {
        const walletId = localStorage.getItem("selectedWalletId"); // Retrieve the wallet ID from localStorage
        if (!walletId) {
          throw new Error("Wallet ID not found. Please select a wallet.");
        }

        const response = await axios.get(`http://35.188.126.68/evento`, {
          params: { id_billetera: walletId },
        });

        if (response.status !== 200) {
          throw new Error("Failed to fetch event data");
        }

        const event = response.data.data;

        // Check if the fecha_hora has changed
        if (this.lastFechaHora !== event.fecha_hora) {
          this.alertas = [
            {
              id: event.id_evento,
              tipo_evento: event.tipo_evento,
              nivel_bateria: event.nivel_bateria,
              fecha_hora: event.fecha_hora,
            },
          ];
          this.lastFechaHora = event.fecha_hora; // Update the last fecha_hora
        }
      } catch (error) {
        this.errorMessage = error.response?.data?.message || "Error al conectar con el servidor";
        console.error("Error fetching alert data:", error);
      }
    },
  },
};
</script>

<style scoped>
.alertas {
  padding: 20px;
  font-family: Arial, sans-serif;
}

.title {
  text-align: center;
  font-size: 2em;
  color: #0056b3;
  margin-bottom: 20px;
}

.alert-list {
  list-style-type: none;
  padding: 0;
}

.alert-item {
  background-color: #f9f9f9;
  border: 1px solid #ddd;
  padding: 10px;
  margin-bottom: 10px;
}

.error-message {
  color: red;
  margin-top: 1rem;
}
</style>