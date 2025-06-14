package com.audioheaven.audioheaven.payload.response;

public class ComentarioResponse {
    private Long id;
    private String contenido;
    private String nombreUsuario;

    public ComentarioResponse(Long id, String contenido, String nombreUsuario) {
        this.id = id;
        this.contenido = contenido;
        this.nombreUsuario = nombreUsuario;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
}