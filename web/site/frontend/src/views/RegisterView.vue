<template>
  <div class="register">
    <h1 class="title">Regístrate:</h1>
    <form @submit.prevent="register" class="form">
      <label class="form-label" for="nombre_usuario">Nombre:</label>
      <input
        v-model="nombre_usuario"
        class="form-input"
        type="text"
        id="nombre_usuario"
        required
        placeholder="Nombre"
      />

      <label class="form-label" for="apellido_usuario">Apellido:</label>
      <input
        v-model="apellido_usuario"
        class="form-input"
        type="text"
        id="apellido_usuario"
        required
        placeholder="Apellido"
      />

      <label class="form-label" for="email">Email:</label>
      <input
        v-model="email"
        class="form-input"
        type="email"
        id="email"
        required
        placeholder="Email"
      />

      <label class="form-label" for="telefono">Teléfono:</label>
      <input
        v-model="telefono"
        class="form-input"
        type="text"
        id="telefono"
        required
        placeholder="Teléfono"
      />

      <label class="form-label" for="password">Contraseña:</label>
      <input
        v-model="password"
        class="form-input"
        type="password"
        id="password"
        required
        placeholder="Password"
      />

      <label class="form-label" for="passwordRepeat"
        >Repite la contraseña:</label
      >
      <input
        v-model="passwordRepeat"
        class="form-input"
        type="password"
        id="passwordRepeat"
        required
        placeholder="Repite la contraseña"
      />

      <input class="form-submit" type="submit" value="Sign Up" />

      <!-- Mensaje de error -->
      <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
    </form>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      nombre_usuario: "",
      apellido_usuario: "",
      email: "",
      telefono: "",
      password: "",
      passwordRepeat: "",
      errorMessage: "", // Variable para almacenar el mensaje de error
    };
  },
  methods: {
    async register() {
      if (this.password != this.passwordRepeat) {
        this.errorMessage = "Las contraseñas no coinciden";
        return;
      }

      try {
        const response = await axios.post("http://35.188.126.68/register", {
          nombre_usuario: this.nombre_usuario,
          apellido_usuario: this.apellido_usuario,
          email: this.email,
          telefono: this.telefono,
          password: this.password,
        });

        if (response.status == 200) {
          console.log("Registro exitoso:", response.data);
          alert("Usuario registrado exitosamente.");
          // Redirigir a Login después del registro exitoso
          this.$router.push("/");
        } else {
          // Manejo de errores de validación específicos
          if (response.data.error == "Error de validación") {
            let errorMessages = "";
            response.data.detalles.forEach((error) => {
              errorMessages += `${error.mensaje}\n`; // Concatenar los mensajes de error
            });
            this.errorMessage = `Errores de validación:\n${errorMessages}`;
          } else {
            this.errorMessage =
              "Error desconocido, por favor intente nuevamente.";
            console.error("Error:", response.data.error);
          }
        }
      } catch (error) {
        console.error("Error al registrar el usuario:", error);
        this.errorMessage = "Error al conectar con el servidor.";
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.register {
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
.error {
  color: red;
  margin-top: 1rem;
  text-align: center;
}
</style>
