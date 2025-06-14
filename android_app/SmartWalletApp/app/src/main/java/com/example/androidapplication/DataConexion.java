package com.example.androidapplication;

public class DataConexion {
    private String id_billetera;
    private boolean tipo_conexion;
    private int nivel_bateria;

    public DataConexion(String id_billetera, boolean tipo_conexion, int nivel_bateria) {
        this.id_billetera = id_billetera;
        this.tipo_conexion = tipo_conexion;
        this.nivel_bateria = nivel_bateria;
    }
    // Getters and Setters
    public String getIdBilletera() {
        return id_billetera;
    }

    public void setIdBilletera(String id_billetera) {
        this.id_billetera= id_billetera;
    }

    public boolean getTipoConexion() {
        return tipo_conexion;
    }

    public void setTipoConexion(boolean tipo_evento) {
        this.tipo_conexion = tipo_conexion;
    }
    public int getNivelBateria() {
        return nivel_bateria;
    }

    public void setNivelBateria(int nivel_bateria) {
        this.nivel_bateria = nivel_bateria;
    }
}
