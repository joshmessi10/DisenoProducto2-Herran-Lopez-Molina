package com.example.androidapplication;

public class DataSensor {

    private String id_billetera;
    private String tipo_sensor;
    private int lectura_sensor;

    public DataSensor(String id_billetera, String tipo_sensor, int lectura_sensor) {
        this.id_billetera = id_billetera;
        this.tipo_sensor = tipo_sensor;
        this.lectura_sensor = lectura_sensor;
    }
    // Getters and Setters
    public String getIdBilletera() {
        return id_billetera;
    }

    public void setIdBilletera(String id_billetera) {
        this.id_billetera= id_billetera;
    }

    public String getTipoSensor() {
        return tipo_sensor;
    }

    public void setTipoSensor(String tipo_sensor) {
        this.tipo_sensor = tipo_sensor;
    }
    public int getLecturaSensor() {
        return lectura_sensor;
    }

    public void setLecturaSensor(int lectura_sensor) {
        this.lectura_sensor = lectura_sensor;
    }

}
