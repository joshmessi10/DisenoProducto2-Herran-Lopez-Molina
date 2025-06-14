<template>
  <div class="historial">
    <h1 class="title">Historial de Conexiones</h1>
    <table class="historial-table">
      <thead>
        <tr>
          <th>Fecha</th>
          <th>Hora</th>
          <th>Tipo de Conexión</th>
          <th>Nivel de Batería</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="conexion in historialConexiones" :key="conexion.id">
          <td>{{ conexion.fecha }}</td>
          <td>{{ conexion.hora }}</td>
          <td>{{ conexion.tipo_conexion }}</td>
          <td>{{ conexion.nivel_bateria }}%</td>
        </tr>
      </tbody>
    </table>
    <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "HistorialView",
  data() {
    return {
      historialConexiones: [],
      errorMessage: "",
      lastFechaHora: null, // To track the last fecha_hora
    };
  },
  async created() {
    this.fetchHistorialConexiones();
    this.interval = setInterval(this.fetchHistorialConexiones, 5000); // Fetch data every 5 seconds
  },
  beforeDestroy() {
    clearInterval(this.interval); // Clear the interval when the component is destroyed
  },
  methods: {
    async fetchHistorialConexiones() {
      try {
        const walletId = localStorage.getItem("selectedWalletId"); // Retrieve the wallet ID from localStorage
        if (!walletId) {
          throw new Error("Wallet ID not found. Please select a wallet.");
        }

        const response = await axios.get(`http://35.188.126.68/conexion`, {
          params: { id_billetera: walletId },
        });

        if (response.status !== 200) {
          throw new Error("Failed to fetch connection data");
        }

        const conexion = response.data.data;

        // Check if the fecha_hora has changed
        if (this.lastFechaHora !== conexion.fecha_hora) {
          this.historialConexiones = [
            {
              id: conexion.id_conexion,
              fecha: new Date(conexion.fecha_hora).toLocaleDateString(),
              hora: new Date(conexion.fecha_hora).toLocaleTimeString(),
              tipo_conexion: conexion.tipo_conexion,
              nivel_bateria: conexion.nivel_bateria,
            },
          ];
          this.lastFechaHora = conexion.fecha_hora; // Update the last fecha_hora
        }
      } catch (error) {
        this.errorMessage = error.response?.data?.message || "Error al conectar con el servidor";
        console.error("Error fetching connection data:", error);
      }
    },
  },
};
</script>

<style scoped>
.historial {
  padding: 20px;
  font-family: Arial, sans-serif;
}

.title {
  text-align: center;
  font-size: 2em;
  color: #0056b3;
  margin-bottom: 20px;
}

.historial-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 20px;
}

.historial-table th,
.historial-table td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: left;
}

.historial-table th {
  background-color: #f2f2f2;
}

.error-message {
  color: red;
  margin-top: 1rem;
}
</style>