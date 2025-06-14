#include <SPI.h>
#include <BLEPeripheral.h>
#include <Adafruit_Sensor.h>
#include <Adafruit_ADXL345_U.h>
#include <Wire.h>
#include <math.h>

// Definir pines para BLEPeripheral
#define BLE_REQ 9
#define BLE_RDY 6
#define BLE_RST 7

// Pines del sistema
#define REED_PIN 14
#define BUZZER_PIN 2
#define BATTERY_PIN 1
#define LED 8

// Crear instancia de BLEPeripheral
BLEPeripheral blePeripheral(BLE_REQ, BLE_RDY, BLE_RST);

// Crear servicio y características BLE
BLEService accelService("fff0");
BLECharacteristic accelMagnitudeCharacteristic("fff1", BLENotify, 20); // Característica para la magnitud
BLECharacteristic alertCharacteristic("fff2", BLENotify, 20);         // Característica para alertas
BLECharacteristic batteryCharacteristic("fff3", BLENotify, 20);       // Característica para nivel de batería
BLECharacteristic walletStateCharacteristic("fff4", BLENotify, 1);    // Característica para estado de billetera
BLECharacteristic buzzerStateCharacteristic("fff5", BLENotify, 1);    // Nueva característica para controlar el buzzer
BLECharacteristic buzzerControlCharacteristic("fff6", BLEWrite, 1);    // Nueva característica para controlar el buzzer

// Instancia del ADXL345
Adafruit_ADXL345_Unified adxl = Adafruit_ADXL345_Unified(12345);

// Umbrales para eventos
#define FALL_THRESHOLD 3       // Umbral ajustado para detectar caída libre
#define IMPACT_THRESHOLD 20    // Umbral para detectar movimiento brusco
#define BATTERY_LOW_PERCENTAGE 20 // Nivel de batería baja en porcentaje

bool isFalling = false; // Estado de caída libre
unsigned long lastUpdate = 0;       // Tiempo de la última transmisión
unsigned long lastAlertTime = 0;    // Tiempo del último mensaje de alerta
unsigned long lastBlinkTime = 0;    // Tiempo del último parpadeo del LED
bool ledState = false;              // Estado actual del LED
bool buzzerState = false;

// Definir pines para el control de encendido y apagado por pulsador
const int pinPulsador = 12;    // Pin donde está conectado el pulsador 
const int pinSalida = 10;      // Pin GPIO que mantiene encendida la ESP32 

bool estadoSalida = true;     // Estado inicial de la salida (encendida)
bool ultimoEstadoPulsador = LOW; // Último estado del pulsador

void setup() {
  // Configurar pines
  pinMode(REED_PIN, INPUT_PULLUP);
  pinMode(BUZZER_PIN, OUTPUT);
  pinMode(LED, OUTPUT);
  pinMode(pinPulsador, INPUT_PULLUP);
  pinMode(pinSalida, OUTPUT);

  // Inicializar I2C
  Wire.begin();
  // Configurar BLEPeripheral
  blePeripheral.setLocalName("MPU6050_BLE");
  blePeripheral.setAdvertisedServiceUuid(accelService.uuid());

  blePeripheral.addAttribute(accelService);
  blePeripheral.addAttribute(accelMagnitudeCharacteristic);
  blePeripheral.addAttribute(alertCharacteristic);
  blePeripheral.addAttribute(batteryCharacteristic);
  blePeripheral.addAttribute(walletStateCharacteristic);
  blePeripheral.addAttribute(buzzerStateCharacteristic);
  blePeripheral.addAttribute(buzzerControlCharacteristic);

  blePeripheral.begin();
  

  
}

// Función para medir el voltaje de la batería
float readBatteryVoltage() {
  int adcValue = analogRead(BATTERY_PIN); // Leer el valor analógico
  float voltage = (adcValue / 1023.0) * 3.6; // Convertir a voltaje (3.6V rango máximo con divisor)
  return voltage;
}

// Función para calcular el porcentaje de batería
int calculateBatteryPercentage(float voltage) {
  const float MAX_VOLTAGE = 3.3;
  const float MIN_VOLTAGE = 2.7;

  // Limitar el voltaje dentro del rango esperado
  if (voltage > MAX_VOLTAGE) voltage = MAX_VOLTAGE;
  if (voltage < MIN_VOLTAGE) voltage = MIN_VOLTAGE;

  // Calcular porcentaje (interpolación lineal)
  int percentage = ((voltage - MIN_VOLTAGE) / (MAX_VOLTAGE - MIN_VOLTAGE)) * 100;
  return percentage;
}

// Función para manejar la batería
void handleBattery() {
  float voltage = readBatteryVoltage();
  int batteryPercentage = calculateBatteryPercentage(voltage);

  String batteryMessage = String(batteryPercentage) + "%";
  batteryCharacteristic.setValue(batteryMessage.c_str());

  // Verificar si la batería está baja
  if (batteryPercentage <= BATTERY_LOW_PERCENTAGE) {
    alertCharacteristic.setValue("BL!"); // Enviar alerta BLE
  } 
}



// Función para manejar el estado del LED de conexión BLE
void handleBLEConnectionLED(bool isConnected) {
  if (isConnected) {
    digitalWrite(LED, LOW); // Apagar LED si está conectado
  } else {
    // Parpadeo del LED si no está conectado
    if (millis() - lastBlinkTime > 500) {
      lastBlinkTime = millis();
      ledState = !ledState;
      digitalWrite(LED, ledState);
    }
  }
}

void handleBuzzerControl() {
  if (buzzerControlCharacteristic.written()) {
    byte buzzerState = buzzerControlCharacteristic.value()[0]; // Acceder al primer valor
    if (buzzerState == 1) {
      analogWrite(BUZZER_PIN, 128); // Encender el buzzer con PWM
      buzzerState = true;

      
    } else {
      analogWrite(BUZZER_PIN, 0);   // Apagar el buzzer
      buzzerState = false;
      
    }
  }
}

void loop() {
  // Escuchar conexiones BLE
  BLECentral central = blePeripheral.central();

  bool isConnected = central.connected();

  // Manejar el LED de conexión BLE
  handleBLEConnectionLED(isConnected);

  if (isConnected) {
    if (!adxl.begin()) {
        alertCharacteristic.setValue("NAC"); // Enviar alerta BLE
      }
    if (central.connected() && adxl.begin()) {
      // Inicializar MPU6050
      
      adxl.setRange(ADXL345_RANGE_2_G);
      // Leer datos del MPU6050 y enviar por BLE cada 500 ms
      if (millis() - lastUpdate > 500) {
        lastUpdate = millis();

        sensors_event_t accel;
        adxl.getEvent(&accel);
        

        // Obtener los valores de aceleración
        float ax = accel.acceleration.x;
        float ay = accel.acceleration.y;
        float az = accel.acceleration.z;

        // Calcular la magnitud de la aceleración
        float accelMagnitude = sqrt(ax * ax + ay * ay + az * az);
        
        // Convertir la magnitud a cadena
        String accelMagnitudeString = String(accelMagnitude, 1) + "m/s^2"; // Con dos decimales

            
        // Crear una cadena con los valores de aceleración en los tres ejes
        String accelDataString = "X: " + String(ax, 1) + " Y: " + String(ay, 1) + " Z: " + String(az, 1); // Con dos decimales
        
        // Establecer el valor en la característica BLE
        accelMagnitudeCharacteristic.setValue(accelMagnitudeString.c_str());

        

        // Detectar caída libre
        if (accelMagnitude < FALL_THRESHOLD) {
          if (!isFalling) {
            isFalling = true;
            alertCharacteristic.setValue("FD!"); // Enviar alerta por BLE
            lastAlertTime = millis(); // Actualizar tiempo de última alerta
          }
        } else if (isFalling) {
          isFalling = false;
        }

        // Detectar movimiento brusco solo si no hay caída libre
        if (!isFalling && accelMagnitude > IMPACT_THRESHOLD) {
          alertCharacteristic.setValue("ID!"); // Enviar alerta por BLE
          lastAlertTime = millis(); // Actualizar tiempo de última alerta
        }

        // Verificar el estado del reed switch (estado de la billetera)
        bool isClosed = (digitalRead(REED_PIN) == LOW);
        walletStateCharacteristic.setValue(isClosed ? "1" : "0"); // Enviar estado como 1 o 0

        buzzerStateCharacteristic.setValue(buzzerState ? "1" : "0"); // Enviar alerta BLE
      }

      // Manejar el estado de la batería
      handleBattery();

      // Manejar el control del buzzer a través de BLE
      handleBuzzerControl();
    }
  }
  handlePulsador();
}

void handlePulsador() {
  bool estadoPulsador = digitalRead(pinPulsador);

  if (estadoPulsador == LOW && ultimoEstadoPulsador == HIGH) {
    // Cambiar estado del pin de salida
    estadoSalida = !estadoSalida;
    digitalWrite(pinSalida, estadoSalida ? HIGH : LOW);
    delay(200); // Retardo para evitar rebotes del pulsador
  }
  ultimoEstadoPulsador = estadoPulsador;
}


