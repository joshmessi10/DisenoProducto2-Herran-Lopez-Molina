package com.example.androidapplication;

public class DataEvent {
    private String id_billetera;
    private String tipo_evento;
    private int nivel_bateria;

    public DataEvent(String id_billetera, String tipo_evento, int nivel_bateria) {
        this.id_billetera = id_billetera;
        this.tipo_evento = tipo_evento;
        this.nivel_bateria = nivel_bateria;
    }
    // Getters and Setters
    public String getIdBilletera() {
        return id_billetera;
    }

    public void setIdBilletera(String id_billetera) {
        this.id_billetera= id_billetera;
    }

    public String getTipoEvento() {
        return tipo_evento;
    }

    public void setTipoEvento(String tipo_evento) {
        this.tipo_evento = tipo_evento;
    }
    public int getNivelBateria() {
        return nivel_bateria;
    }

    public void setNivelBateria(int nivel_bateria) {
        this.nivel_bateria = nivel_bateria;
    }

}
