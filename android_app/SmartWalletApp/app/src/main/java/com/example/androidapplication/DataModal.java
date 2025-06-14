package com.example.androidapplication;

public class DataModal {

    // Fields required by the server
    private String id_usuario;
    private String nombre_billetera;
    private String modelo_billetera;
    private String id_billetera;

    // Constructor
    public DataModal(String id_usuario, String nombre_billetera) {
        this.id_usuario = id_usuario;
        this.nombre_billetera = nombre_billetera;
    }



    public String getNombreBilletera() {
        return nombre_billetera;
    }

    public void setNombreBilletera(String nombre_billetera) {
        this.nombre_billetera = nombre_billetera;
    }

    public String getModeloBilletera() {
        return modelo_billetera;
    }

    public void setModeloBilletera(String modelo_billetera) {
        this.modelo_billetera = modelo_billetera;
    }

    public String getIdBilletera() {
        return id_billetera; // Use id_billetera to match the response
    }

    public void setIdBilletera(String id_billetera) {
        this.id_billetera = id_billetera; // Set id_billetera here
    }
}