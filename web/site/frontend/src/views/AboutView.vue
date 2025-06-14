<template>
  <div class="about">
    <!-- Encabezado -->
    <header class="header">
      <img alt="About" src="../assets/about-logo.png" class="logo" />
      <h1>Acerca de la Billetera Inteligente</h1>
      <p>Tu aliado en seguridad y tecnología personal.</p>
    </header>

    <!-- Propósito y Descripción -->
    <section class="purpose-section">
      <h2>¿Qué es la Billetera Inteligente?</h2>
      <p>
        La Billetera Inteligente es un dispositivo innovador que protege tus
        pertenencias y facilita tu vida diaria. Gracias a su tecnología de
        punta, puedes mantener tus documentos seguros, rastrear la ubicación de
        tu billetera, y recibir alertas en tu dispositivo móvil en caso de
        acceso no autorizado o pérdida.
      </p>
    </section>

    <!-- Características principales -->
    <section class="features-section">
      <h2>Características Principales</h2>
      <ul>
        <li>
          🔒 <strong>Seguridad contra robos</strong>: Alertas de acceso no
          autorizado.
        </li>
        <li>
          📍 <strong>Geolocalización</strong>: Localización precisa vía conexión
          con el móvil.
        </li>
        <li>
          📱 <strong>Alerta por desconexión</strong>: Notificación si te alejas
          demasiado de tu billetera.
        </li>
        <li>
          🚨 <strong>Detección de caídas</strong>: Sensores para detectar caídas
          bruscas.
        </li>
        <li>
          🔋 <strong>Monitoreo de batería</strong>: Nivel de batería para
          asegurar disponibilidad.
        </li>
        <li>
          💼 <strong>Bloqueo RFID</strong>: Protección contra escaneo no
          autorizado de tarjetas.
        </li>
      </ul>
    </section>

    <!-- Seguridad y Protección -->
    <section class="security-section">
      <h2>Seguridad y Protección</h2>
      <p>
        La Billetera Inteligente está equipada con múltiples sensores y
        materiales de bloqueo RFID que garantizan la seguridad de tu información
        y de tus pertenencias en todo momento. Conéctate a través de Bluetooth y
        recibe notificaciones y alertas en tiempo real.
      </p>
    </section>

    <!-- Soporte y Contacto -->
    <section class="support-section">
      <h2>Contacto y Soporte</h2>
      <p>
        ¿Tienes preguntas o necesitas ayuda? Contáctanos para obtener asistencia
        o envíanos tus comentarios.
      </p>
      <ul>
        <li>📧 <strong>Email:</strong> soporte@billeterainteligente.com</li>
        <li>📞 <strong>Teléfono:</strong> +34 123 456 789</li>
        <li>
          🌐 <strong>Sitio Web:</strong>
          <a href="https://billeterainteligente.com" target="_blank"
            >billeterainteligente.com</a
          >
        </li>
      </ul>
    </section>
  </div>
</template>

<script>
export default {
  name: "AboutView",
};
</script>

<style scoped>
.about {
  text-align: center;
  font-family: Arial, sans-serif;
  color: #333;
  padding: 20px;
}
.logo {
  width: 375px;
  margin-bottom: 10px;
}

.header {
  margin-bottom: 30px;
}

.header h1 {
  font-size: 2em;
  color: #0056b3;
}

.header p {
  font-size: 1.1em;
  color: #666;
}

section {
  margin: 20px auto;
  width: 80%;
  text-align: left;
}

h2 {
  color: #0056b3;
  margin-bottom: 10px;
}

.features-section ul {
  list-style-type: none;
  padding: 0;
}

.features-section li {
  margin: 10px 0;
  font-size: 1.1em;
}

.support-section ul {
  list-style-type: none;
  padding: 0;
  font-size: 1em;
}

.support-section a {
  color: #0056b3;
  text-decoration: none;
}

.support-section a:hover {
  text-decoration: underline;
}
</style>
