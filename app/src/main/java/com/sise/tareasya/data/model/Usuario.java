package com.sise.tareasya.data.model;

public class Usuario {
    private int id;
    private String nombre;
    private String apePaterno;
    private String apeMaterno;
    private String email;
    private String password; // Solo para login, no enviar de vuelta
    private String createdAt;
    private String updatedAt;

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

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

    // Método útil para mostrar nombre completo
    public String getNombreCompleto() {
        return nombre + " " + apePaterno + " " + apeMaterno;
    }
}