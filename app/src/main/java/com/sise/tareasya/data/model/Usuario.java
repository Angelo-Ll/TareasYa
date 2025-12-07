package com.sise.tareasya.data.model;

import com.google.gson.annotations.SerializedName;

public class Usuario {

    @SerializedName("idUsuario")  // ¡ESTO ES CLAVE!
    private int id;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("apePaterno")
    private String apePaterno;

    @SerializedName("apeMaterno")
    private String apeMaterno;

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    @SerializedName("auditoria")
    private int auditoria;  // Cambié esto porque tu API devuelve "auditoria", no "createdAt"

    @SerializedName("fechaCreacion")
    private String fechaCreacion;  // Cambié esto porque tu API devuelve "fechaCreacion"

    // Constructor vacío (IMPORTANTE para Retrofit)
    public Usuario() {}

    // Constructor para login
    public Usuario(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApePaterno() { return apePaterno; }
    public void setApePaterno(String apePaterno) { this.apePaterno = apePaterno; }

    public String getApeMaterno() { return apeMaterno; }
    public void setApeMaterno(String apeMaterno) { this.apeMaterno = apeMaterno; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public int getAuditoria() { return auditoria; }
    public void setAuditoria(int auditoria) { this.auditoria = auditoria; }

    public String getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(String fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    // Método útil para mostrar nombre completo
    public String getNombreCompleto() {
        return nombre + " " + apePaterno + " " + apeMaterno;
    }
}