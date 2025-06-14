<template>
  <div id="app">
    <nav class="navbar">
      <div v-if="!isLoginPage && !isRegisterPage" class="navbar-links">
        <router-link to="/home">Home</router-link> |
        <router-link to="/alertas">Alertas de Seguridad</router-link> |
        <router-link to="/historial">Historial de Conexiones</router-link> |
        <router-link to="/about">About</router-link>
      </div>
      <div class="navbar-buttons">
        <button v-if="isRegisterPage" @click="signIn">Sign In</button>
        <button v-if="isLoginPage" @click="signUp">Sign Up</button>
        <button v-if="!isLoginPage && !isRegisterPage" @click="logout">Cerrar sesión</button>
      </div>
    </nav>

    <router-view />
  </div>
</template>

<script>
import axiosInstance from "@/axios.js";

export default {
  data() {
    return {
      isLoggedIn: false,
    };
  },

  created() {
    // Verify the session when the component is created
    this.verifySession();
  },

  computed: {
    // Check if the current route path is login
    isLoginPage() {
      return this.$route.path === "/";
    },

    // Check if the current route path is register
    isRegisterPage() {
      return this.$route.path === "/register";
    },
  },

  methods: {
    // Verify if we are in the login form
    signIn() {
      this.$router.push("/");
    },

    // Verify if we are in the register form
    signUp() {
      this.$router.push("/register");
    },

    // Method to verify if the user is authenticated
    async verifySession() {
      try {
        const response = await axiosInstance.get("/verify-session", {
          // Ensure cookies are included in the request
          withCredentials: true,
        });

        // The user is authenticated
        this.isLoggedIn = response.status == 200;
      } catch (error) {
        // Assume the user is not authenticated on error
        console.error("Error verifying session:", error);
        this.isLoggedIn = false;
      }
    },

    // Method to log out
    async logout() {
      try {
        await axiosInstance.post("/logout", {}, { withCredentials: true });
        this.isLoggedIn = false;
        // Redirect to home after logging out
        this.$router.push("/");
      } catch (error) {
        console.error("Error logging out:", error);
      }
    },
  },
};
</script>

<style scoped>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

.navbar {
  display: flex;
  justify-content: space-between;
  padding: 30px;
  align-items: center;
}

.navbar-links {
  display: flex;
  gap: 10px;
}

.navbar-buttons {
  display: flex;
  gap: 10px;
  justify-content: flex-end; /* Align buttons to the right */
  width: 100%; /* Ensure the navbar takes full width */
}

button {
  font-weight: bold;
  color: white;
  background-color: #42b983;
  border: none;
  padding: 10px 15px;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
}

button:hover {
  background-color: #2c3e50;
}

nav a {
  font-weight: bold;
  color: #2c3e50;
  text-decoration: none;
}

nav a.router-link-exact-active {
  color: #42b983;
}
</style>
