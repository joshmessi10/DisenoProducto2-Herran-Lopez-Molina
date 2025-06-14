<template>
  <div class="home">
    <!-- Barra superior -->
    <div class="status-bar">
      <span class="time">{{ formattedDate }} {{ formattedTime }}</span>
      <div class="status-icons">
        <!-- Icono de alerta -->
        <img
          v-if="alertLevel === 'none'"
          src="../assets/alert-none.png"
          class="icon"
          title="Sin Alerta"
          alt="Sin Alerta"
        />
        <img
          v-else-if="alertLevel === 'warning'"
          src="../assets/alert-warning.png"
          class="icon"
          title="Alerta Amarilla"
          alt="Alerta Amarilla"
        />
        <img
          v-else-if="alertLevel === 'alert'"
          src="../assets/alert-red.png"
          class="icon"
          title="Alerta Roja"
          alt="Alerta Roja"
        />

        <!-- Icono de Bluetooth -->
        <img
          v-if="bluetoothStatus === 'Conectado'"
          src="../assets/bluetooth-connected.png"
          class="icon"
          title="Bluetooth Conectado"
          alt="Bluetooth Conectado"
        />
        <img
          v-else
          src="../assets/bluetooth-disconnected.png"
          class="icon"
          title="Bluetooth Desconectado"
          alt="Bluetooth Desconectado"
        />

        <!-- Icono de batería -->
        <img
          v-if="batteryLevel <= 10"
          src="../assets/battery-empty.png"
          class="icon"
          title="Batería Baja"
          alt="Batería Baja"
        />
        <img
          v-else-if="batteryLevel <= 30"
          src="../assets/battery-low.png"
          class="icon"
          title="Batería Baja"
          alt="Batería Baja"
        />
        <img
          v-else-if="batteryLevel <= 70"
          src="../assets/battery-medium.png"
          class="icon"
          title="Batería Media"
          alt="Batería Media"
        />
        <img
          v-else-if="batteryLevel <= 90"
          src="../assets/battery-high.png"
          class="icon"
          title="Batería Alta"
          alt="Batería Alta"
        />
        <img
          v-else
          src="../assets/battery-full.png"
          class="icon"
          title="Batería Llena"
          alt="Batería Llena"
        />
      </div>
    </div>

    <!-- Encabezado -->
    <header class="header">
      <img
        alt="Billetera Inteligente"
        src="../assets/wallet-logo.png"
        class="logo"
      />
      <h1 class="main-title">Bienvenido a Tu Billetera Inteligente</h1>
    </header>

    <!-- Módulo de bloqueo de seguridad manual -->
    <section class="lock-section">
      <button v-on:click="toggleLock" :class="{ locked: isLocked }">
        {{ isLocked ? "Desbloquear" : "Bloquear" }} Billetera
      </button>
    </section>

    <!-- Estado general de la billetera -->
    <section class="status-section">
      <h2>Estado de la Billetera</h2>
      <div class="status-item">
        <p>
          Conexión Bluetooth:
          <span
            :style="{
              color: bluetoothStatus === 'Conectado' ? 'green' : 'red',
            }"
          >
            <strong>{{ bluetoothStatus }}</strong>
          </span>
        </p>
        <button v-on:click="toggleBluetooth">
          {{
            bluetoothStatus === "Conectado" ? "Desconectar" : "Conectar"
          }}
          Bluetooth
        </button>
        <p>
          Nivel de Batería:
          <span
            :style="{
              color:
                batteryLevel <= 20
                  ? 'red'
                  : batteryLevel <= 50
                  ? 'yellow'
                  : 'green',
            }"
          >
            <strong>{{ batteryLevel }}%</strong>
          </span>
        </p>
        <p>
          Última Ubicación: <strong>{{ lastLocation }}</strong>
        </p>
      </div>
    </section>

    <!-- Enlaces a otras páginas -->
    <section class="links-section">
      <router-link to="/alertas" class="link-button"
        >Ver Alertas de Seguridad</router-link
      >
      <router-link to="/historial" class="link-button"
        >Ver Historial de Conexiones</router-link
      >
    </section>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "HomeView",
  data() {
    return {
      bluetoothStatus: "Desconectado", // Estado de Bluetooth
      batteryLevel: 0, // Nivel de batería
      lastLocation: "Latitud: 34.0522, Longitud: -118.2437", // Ejemplo de ubicación
      isLocked: true, // Estado de bloqueo de la billetera
      alertLevel: "none", // Nivel de alerta: 'none', 'warning', 'alert'
      previousAlertLevel: "none", // To track the previous alert level
      errorMessage: "",
    };
  },
  async created() {
    this.fetchSensorData();
    this.fetchEventData();
    this.interval = setInterval(() => {
      this.fetchSensorData();
      this.fetchEventData();
      this.checkAlertLevel();
    }, 5000); // Fetch data every 5 seconds
  },
  beforeDestroy() {
    clearInterval(this.interval); // Clear the interval when the component is destroyed
  },
  methods: {
    async fetchSensorData() {
      try {
        const walletId = localStorage.getItem("selectedWalletId"); // Retrieve the wallet ID from localStorage
        if (!walletId) {
          throw new Error("Wallet ID not found. Please select a wallet.");
        }

        const response = await axios.get(`http://35.188.126.68/sensor`, {
          params: { id_billetera: walletId },
        });

        if (response.status !== 200) {
          throw new Error("Failed to fetch sensor data");
        }

        const data = response.data.data;
        this.bluetoothStatus = data.bluetooth?.lectura_sensor === 0 ? "Conectado" : "Desconectado";
        this.batteryLevel = data.bateria?.lectura_sensor || 0;
        this.isLocked = data.magnetico?.lectura_sensor === 0;
      } catch (error) {
        this.errorMessage = error.message;
      }
    },
    async fetchEventData() {
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
        const tipoEvento = event.tipo_evento;

        this.previousAlertLevel = this.alertLevel; // Store the current alert level

        if (tipoEvento === "caida_detectada" || tipoEvento === "movimiento_brusco") {
          this.alertLevel = "warning";
        } else if (
          tipoEvento === "bateria_baja" ||
          tipoEvento === "billetera_abierta" ||
          tipoEvento === "desconexion"
        ) {
          this.alertLevel = "alert";
        } else {
          this.alertLevel = "none";
        }
      } catch (error) {
        this.errorMessage = error.message;
      }
    },
    checkAlertLevel() {
      if (this.alertLevel === this.previousAlertLevel && (this.alertLevel === "warning" || this.alertLevel === "alert")) {
        this.alertLevel = "none";
      }
    },
    toggleLock() {
      this.isLocked = !this.isLocked;
    },
    toggleBluetooth() {
      this.bluetoothStatus =
        this.bluetoothStatus === "Conectado" ? "Desconectado" : "Conectado";
    },
  },
  computed: {
    // Obtiene la fecha en formato "Día, Mes DD, YYYY"
    formattedDate() {
      const options = {
        weekday: "long",
        month: "long",
        day: "numeric",
        year: "numeric",
      };
      return new Date().toLocaleDateString("es-ES", options);
    },
    // Obtiene la hora en formato de 12 horas con AM/PM
    formattedTime() {
      const date = new Date();
      const hours = date.getHours();
      const minutes = date.getMinutes().toString().padStart(2, "0");
      const ampm = hours >= 12 ? "PM" : "AM";
      const formattedHours = hours % 12 || 12;
      return `${formattedHours}:${minutes} ${ampm}`;
    },
  },
  mounted() {
    // Actualiza la hora cada segundo
    setInterval(() => {
      this.$forceUpdate(); // Fuerza la actualización para refrescar la fecha y hora
    }, 1000);
  },
};
</script>

<style scoped>
.home {
  font-family: Arial, sans-serif;
  color: #333;
}

/* Barra de estado superior */
.status-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
  background-color: #f5f5f5;
  border-bottom: 1px solid #ddd;
}

.time {
  font-size: 1.1em;
  font-weight: bold;
}

.status-icons {
  display: flex;
  gap: 15px;
}

.icon {
  width: 24px;
  height: 24px;
}

/* Estilo de encabezado */
.header {
  margin: 20px 0;
  text-align: center;
}

.logo {
  width: 410px;
  margin-bottom: 10px;
}

.lock-section {
  display: flex;
  justify-content: center;
  align-items: center;
}

.main-title {
  font-size: 2em;
  color: #0056b3;
}

h2 {
  color: #0056b3;
  margin-bottom: 10px;
}

/* Otros estilos */
.lock-section,
.status-section,
.links-section {
  margin: 20px auto;
  width: 80%;
  text-align: left;
}

.lock-section button {
  padding: 10px 20px;
  font-size: 1em;
  background-color: #0056b3;
  color: white;
  border: none;
  cursor: pointer;
  border-radius: 5px;
  transition: background-color 0.3s;
}

.lock-section button.locked {
  background-color: #d9534f;
}

.link-button {
  padding: 10px 15px;
  font-size: 1em;
  background-color: #0056b3;
  color: white;
  text-decoration: none;
  border-radius: 5px;
  transition: background-color 0.3s;
  margin-right: 50px;
}

.link-button:hover {
  background-color: #004080;
}

button {
  padding: 10px 15px;
  font-size: 1em;
  background-color: #4caf50;
  color: white;
  border: none;
  cursor: pointer;
  border-radius: 5px;
}

button:hover {
  background-color: #45a049;
}
</style>