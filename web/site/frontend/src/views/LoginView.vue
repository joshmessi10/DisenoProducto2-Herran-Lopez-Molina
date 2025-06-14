<template>
  <div class="login">
    <h1 class="title">Iniciar sesión</h1>
    <form class="form" @submit.prevent="login">
      <label class="form-label" for="email">Email:</label>
      <input
        v-model="email"
        class="form-input"
        type="email"
        id="email"
        required
        placeholder="Email"
      />

      <label class="form-label" for="password">Contraseña:</label>
      <input
        v-model="password"
        class="form-input"
        type="password"
        id="password"
        required
        placeholder="Contraseña"
      />

      <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>

      <input class="form-submit" type="submit" value="Iniciar sesión" />
    </form>
  </div>
</template>

<script>
import axios from "axios";
import router from "../router";

export default {
  data() {
    return {
      email: "",
      password: "",
      errorMessage: "", // For error messages
    };
  },
  methods: {
    async login() {
      try {
        const response = await axios.post("http://35.188.126.68/login", {
          email: this.email,
          password: this.password,
        });

        if (response.status == 200) {
          console.log("Logged in");
          console.log(response);
          localStorage.setItem("userId", response.data.id_usuario);
          router.push("/register-wallet");
        } else {
              // If session verification fails, redirect to register
          router.push("/register");
          this.errorMessage =
            response.data.message || "Login failed. Please try again.";
        }
      } catch (error) {
        this.errorMessage =
          error.response?.data?.message || "Error connecting to the server.";
        console.error("Error during login:", error);
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.login {
  padding: 2rem;
}
.title {
  text-align: center;
}
.form {
  margin: 3rem auto;
  display: flex;
  flex-direction: column;
  justify-content: center;
  width: 20%;
  min-width: 350px;
  max-width: 100%;
  background: rgba(19, 35, 47, 0.9);
  border-radius: 5px;
  padding: 40px;
  box-shadow: 0 4px 10px 4px rgba(0, 0, 0, 0.3);
}
.form-label {
  margin-top: 2rem;
  color: white;
  margin-bottom: 0.5rem;
  &:first-of-type {
    margin-top: 0rem;
  }
}
.form-input {
  padding: 10px 15px;
  background: none;
  background-image: none;
  border: 1px solid white;
  color: white;
  &:focus {
    outline: 0;
    border-color: #1ab188;
  }
}
.form-submit {
  background: #1ab188;
  border: none;
  color: white;
  margin-top: 3rem;
  padding: 1rem 0;
  cursor: pointer;
  transition: background 0.2s;
  &:hover {
    background: #0b9185;
  }
}
.error-message {
  color: red;
  font-size: 0.9rem;
  margin-top: 1rem;
  text-align: center;
}

/* Estilo para el mensaje de éxito */
.login-success-message {
  background-color: #42b983;
  color: white;
  padding: 20px;
  border-radius: 5px;
  margin-top: 20px;
  font-size: 1.5rem;
  text-align: center;
  animation: fadeIn 1s ease-out;
}

/* Animación para el mensaje */
@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}
</style>
