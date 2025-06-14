package com.example.androidapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.text.TextWatcher;
import android.text.Editable;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;



public class MainActivity extends AppCompatActivity {
    private Timer settingsTimer; // Timer for periodic updates
    private EditText firstEdt, secondEdt;
    private Button postDataBtn;
    private TextView responseTV, response2TV, title, bluetoothTV, batteryTV, buzzerTV, movementTV, stateTV;
    private ProgressBar loadingPB;

    private String userId; // Variable to store id_usuario


    private String billeteraId; // Variable to store id_billetera
    private String billeteraNombre; // Variable to store Nombre Billetera

    // Variables for the settings
    private boolean bluetooth = true;
    private int battery = 80;
    private boolean buzzer = false;
    private float movement = 0.0f;
    private boolean state = true;

    private boolean login = true;
    StringBuilder actualPassword = new StringBuilder(); // To hold the actual password
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing views

        title = findViewById(R.id.idTVTitle);  // Assuming you have a TextView for the title
        firstEdt = findViewById(R.id.idEdt1);
        secondEdt = findViewById(R.id.idEdt2);
        postDataBtn = findViewById(R.id.idBtnPost);
        loadingPB = findViewById(R.id.idLoadingPB);

        responseTV = findViewById(R.id.idTVResponse);
        response2TV = findViewById(R.id.idTVResponse2);
        bluetoothTV = findViewById(R.id.bluetoothTV);  // TextView to show Bluetooth value
        batteryTV = findViewById(R.id.batteryTV);  // TextView to show battery value
        buzzerTV = findViewById(R.id.buzzerTV);  // TextView to show buzzer value
        movementTV = findViewById(R.id.movementTV);  // TextView to show movement value
        stateTV = findViewById(R.id.stateTV);  // TextView to show state value


        bluetoothTV.setVisibility(View.GONE);
        batteryTV.setVisibility(View.GONE);
        buzzerTV.setVisibility(View.GONE);
        movementTV.setVisibility(View.GONE);
        stateTV.setVisibility(View.GONE);
        secondEdt.addTextChangedListener(myTextWatcher);



// Handling the post request on button click:
        postDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firstEdt.getText().toString().isEmpty() || secondEdt.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (login) {
                    postUser(firstEdt.getText().toString(), actualPassword.toString());
                } else {

                    postData(userId, firstEdt.getText().toString(), secondEdt.getText().toString());
                }
            }
        });
    }
    TextWatcher myTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // No action needed here
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // No action needed here
        }

        @Override
        public void afterTextChanged(Editable s) {
            String enteredText = s.toString();

            if (enteredText.length() > actualPassword.length()) {
                // Character added
                actualPassword.append(enteredText.charAt(enteredText.length() - 1));
            } else if (enteredText.length() < actualPassword.length()) {
                // Character removed
                actualPassword.delete(actualPassword.length() - 1, actualPassword.length());
            }

            // Build the display text
            StringBuilder displayText = new StringBuilder();
            for (int i = 0; i < actualPassword.length(); i++) {
                if (i == actualPassword.length() - 1 && enteredText.length() >= actualPassword.length()) {
                    displayText.append(actualPassword.charAt(i)); // Show the last character if typing
                } else {
                    displayText.append('*'); // Mask all other characters
                }
            }

            // Update the EditText
            secondEdt.removeTextChangedListener(this);  // Temporarily remove listener to prevent recursion
            secondEdt.setText(displayText.toString());
            secondEdt.setSelection(displayText.length());  // Ensure the cursor stays at the end
            secondEdt.addTextChangedListener(this);  // Add listener back
        }
    };

    private void postUser(String email, String password) {
        loadingPB.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://35.188.126.68/") // Base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        DataUser dataUser = new DataUser(email, password);

        Call<DataUser> call = retrofitAPI.createPost(dataUser);

        call.enqueue(new Callback<DataUser>() {
            @Override
            public void onResponse(Call<DataUser> call, Response<DataUser> response) {
                loadingPB.setVisibility(View.GONE);

                if (response.isSuccessful() && response.code() == 200) {

                    userId = response.body().getIdUsuario(); // Store the id_usuario

                    // Remove title, text boxes, and button
                    title.setVisibility(View.GONE);
                    firstEdt.setVisibility(View.GONE);
                    secondEdt.setVisibility(View.GONE);
                    postDataBtn.setVisibility(View.GONE);

                    // Set the new title with Nombre Billetera
                    title.setVisibility(View.VISIBLE);
                    title.setText("Wallet Register");



                    // Optionally, clear the input fields
                    firstEdt.setText("");
                    secondEdt.setText("");

                    firstEdt.setVisibility(View.VISIBLE);
                    firstEdt.setHint("Enter your Wallet Name");

                    secondEdt.setVisibility(View.VISIBLE);
                    secondEdt.setHint("Enter your Wallet Model");

                    postDataBtn.setVisibility(View.VISIBLE);
                    postDataBtn.setText("Register");

                    // Store the billeteraId in SharedPreferences
                    saveUserId(userId);
                    login = false;
                    secondEdt.removeTextChangedListener(myTextWatcher);
                    // Optionally, clear the input fields
                    firstEdt.setText("");
                    secondEdt.setText("");
                    responseTV.setText("");

                } else {

                    if(response.code() == 401){
                        responseTV.setText("User Not Found. Register in http://34.27.153.202/register");
                    }

                }
            }

            @Override
            public void onFailure(Call<DataUser> call, Throwable t) {
                loadingPB.setVisibility(View.GONE);
                responseTV.setText("Failure: " + t.getMessage());
            }
        });
    }

    private void postData(String id_usuario, String nombre_billetera,  String modelo_billetera) {
        loadingPB.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://34.72.19.242/") // Base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        DataModal dataModal = new DataModal(id_usuario, nombre_billetera);

        Call<DataModal> call = retrofitAPI.createPost(dataModal);

        call.enqueue(new Callback<DataModal>() {
            @Override
            public void onResponse(Call<DataModal> call, Response<DataModal> response) {
                loadingPB.setVisibility(View.GONE);

                if (response.isSuccessful() && response.code() == 201) {
                    // Get the id_billetera and nombre_billetera and update the title
                    billeteraId = response.body().getIdBilletera(); // Store the id_billetera
                    billeteraNombre = response.body().getNombreBilletera(); // Store the id_billetera

                    // Remove title, text boxes, and button
                    title.setVisibility(View.GONE);
                    firstEdt.setVisibility(View.GONE);
                    secondEdt.setVisibility(View.GONE);
                    postDataBtn.setVisibility(View.GONE);

                    // Set the new title with Nombre Billetera
                    title.setVisibility(View.VISIBLE);
                    title.setText("Welcome to " + billeteraNombre + " Wallet");

                    // Optionally, clear the input fields
                    firstEdt.setText("");
                    secondEdt.setText("");
                    responseTV.setText("");
                    // Store the billeteraId in SharedPreferences
                    saveBilleteraId(billeteraId);
                    generateConnection(billeteraId, true, battery);
                    showSettings();
                    startUpdatingSettings();

                } else {
                    if(response.code() == 409){
                        responseTV.setText("Wallet Name already used");
                    }
                }
            }

            @Override
            public void onFailure(Call<DataModal> call, Throwable t) {
                loadingPB.setVisibility(View.GONE);
                responseTV.setText("Failure: " + t.getMessage());
            }
        });
    }

    // Method to save billeteraId in SharedPreferences
    private void saveBilleteraId(String billeteraId) {
        SharedPreferences sharedPreferences = getSharedPreferences("BilleteraPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id_billetera", billeteraId); // Store the id_billetera
        editor.apply(); // Save the data
    }

    // Method to save billeteraId in SharedPreferences
    private void saveUserId(String userId) {
        SharedPreferences sharedPreferences = getSharedPreferences("UsuarioPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id_usuario", userId); // Store the id_billetera
        editor.apply(); // Save the data
    }

    // Method to show and store settings (Bluetooth, Battery, Buzzer, Movement, State)
    private void showSettings() {
        bluetoothTV.setVisibility(View.VISIBLE);
        batteryTV.setVisibility(View.VISIBLE);
        buzzerTV.setVisibility(View.VISIBLE);
        movementTV.setVisibility(View.VISIBLE);
        stateTV.setVisibility(View.VISIBLE);

        if (bluetooth) {
            bluetoothTV.setText("Bluetooth: Connected");
            batteryTV.setText("Battery: " + battery + "%");
            buzzerTV.setText("Buzzer: " + buzzer);
            movementTV.setText("Movement: " + (int) movement + " m/s²");
            stateTV.setText("State: " + state);
        } else {
            bluetoothTV.setText("Bluetooth: Disconnected");
            batteryTV.setText("Battery: N/A");
            buzzerTV.setText("Buzzer: N/A");
            movementTV.setText("Movement: N/A");
            stateTV.setText("State: N/A");
        }


        // Store the settings in SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("BilleteraPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("bluetooth", bluetooth);
        editor.putInt("battery", battery);
        editor.putBoolean("buzzer", buzzer);
        editor.putFloat("movement", movement);
        editor.putBoolean("state", state);
        editor.apply(); // Save the settings
    }

    private void updateReadings(String id_billetera, String tipo_sensor, int lectura_sensor){
        response2TV.setText("");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://34.72.19.242/") // Base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        DataSensor dataSensor = new DataSensor(id_billetera, tipo_sensor, lectura_sensor);

        Call<DataSensor> call = retrofitAPI.createPost(dataSensor);

        call.enqueue(new Callback<DataSensor>() {
            @Override
            public void onResponse(Call<DataSensor> call, Response<DataSensor> response) {
                loadingPB.setVisibility(View.GONE);

                if (response.isSuccessful() && response.code() == 201) {

                    switch(tipo_sensor){
                        case "acelerometro":
                            movement = (float) lectura_sensor;
                            if (movement <= -7.0){
                                generateEvent(id_billetera, "caida_detectada", battery);
                            }
                            if (movement >= 15.0){
                                generateEvent(id_billetera, "movimiento_brusco", battery);
                            }
                            break;
                        case "magnetico":
                            int value = state ? 1 : 0;
                            if(lectura_sensor == 1 && value != lectura_sensor){
                                state = true;
                            }
                            if(lectura_sensor == 0 && value != lectura_sensor){
                                state = false;
                                generateEvent(id_billetera, "billetera_abierta", battery);
                            }
                            break;
                        case "bateria":
                            battery = lectura_sensor;
                            if (battery <= 20){
                                generateEvent(id_billetera, "bateria_baja", battery);
                            }
                            break;
                        case "bluetooth":
                            int value2 = bluetooth ? 1 : 0;
                            if(lectura_sensor == 1 && value2 != lectura_sensor){
                                bluetooth = true;

                                generateConnection(id_billetera, true, battery);
                            }
                            if(lectura_sensor == 0 && value2 != lectura_sensor){
                                bluetooth = false;
                                generateEvent(id_billetera, "desconexion", battery);
                                generateConnection(id_billetera, false, battery);
                            }
                            break;
                    }

                    // Show and save the settings
                    showSettings();
                } else {
                    response2TV.setText("Sensor "+tipo_sensor+" Value couldn't be updated");
                }
            }

            @Override
            public void onFailure(Call<DataSensor> call, Throwable t) {
                loadingPB.setVisibility(View.GONE);
                response2TV.setText("Failure: " + t.getMessage());
            }
        });
    }

    private void generateEvent(String id_billetera, String tipo_evento, int nivel_bateria){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://34.72.19.242/") // Base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        DataEvent dataEvent = new DataEvent(id_billetera, tipo_evento, nivel_bateria);

        Call<DataEvent> call = retrofitAPI.createPost(dataEvent);

        call.enqueue(new Callback<DataEvent>() {
            @Override
            public void onResponse(Call<DataEvent> call, Response<DataEvent> response) {
                loadingPB.setVisibility(View.GONE);

                if (response.isSuccessful() && response.code() == 201) {
                    if(tipo_evento != "null"){
                        response2TV.setText("Alerta: "+tipo_evento);
                    }
                    else{
                        response2TV.setText("");
                    }

                } else {
                    response2TV.setText("Event not updated");
                }
            }

            @Override
            public void onFailure(Call<DataEvent> call, Throwable t) {
                loadingPB.setVisibility(View.GONE);
                response2TV.setText("Failure: " + t.getMessage());
            }
        });
    }

    private void generateConnection(String id_billetera, boolean tipo_conexion, int nivel_bateria){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://34.72.19.242/") // Base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        DataConexion dataConexion = new DataConexion(id_billetera, tipo_conexion, nivel_bateria);

        Call<DataConexion> call = retrofitAPI.createPost(dataConexion);

        call.enqueue(new Callback<DataConexion>() {
            @Override
            public void onResponse(Call<DataConexion> call, Response<DataConexion> response) {
                loadingPB.setVisibility(View.GONE);

                if (response.isSuccessful() && response.code() == 201) {
                        response2TV.setText("Status: Connected");


                } else {
                    response2TV.setText("Connection not updated");
                }
            }

            @Override
            public void onFailure(Call<DataConexion> call, Throwable t) {
                loadingPB.setVisibility(View.GONE);
                response2TV.setText("Failure: " + t.getMessage());
            }
        });
    }

    // Method to start periodic updates
    private void startUpdatingSettings() {
        settingsTimer = new Timer();
        settingsTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    updateSettings();
                    showSettings();
                });
            }
        }, 0, 5000); // Run every 5 seconds
    }

    // Method to update the settings with specified probabilities
    private void updateSettings() {
        Random random = new Random();

        // Update movement with a 70% chance
        if (random.nextInt(100) > 30) { // 70% chance
            movement = -10 + random.nextFloat() * (18 - (-10)); // Random value between -15 and 25
            updateReadings(billeteraId, "acelerometro", (int) movement);
        }

        // Decrease battery with a 20% chance
        if (random.nextInt(100) < 20) { // 25% chance
            battery = Math.max(0, battery - 1); // Decrease by 1, ensure it doesn't go below 0
            updateReadings(billeteraId, "bateria", battery);
        }

        // Change state to false with a 4% chance
        if (random.nextInt(100) < 4) { // 4% chance
            state = false;
            int value = state ? 1 : 0;
            updateReadings(billeteraId, "magnetico", value);
        }

        // Change Bluetooth to false with a 1% chance
        if (random.nextInt(100) < 1) { // 1% chance
            bluetooth = false;
            int value2 = bluetooth ? 1 : 0;
            updateReadings(billeteraId, "bluetooth", value2);
        }

    }

}