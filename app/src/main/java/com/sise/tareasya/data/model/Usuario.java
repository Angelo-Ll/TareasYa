package com.sise.tareasya.data.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Usuario {

    private int idUsuario;
    private String nombre;
    private String apePaterno;
    private String apeMaterno;
    private String email;
    private String password;
    private int auditoria;
    private String fechaCreacion;


    public Usuario(String email, String password) {
        this.email = email;
        this.password = password;
    }


    public String getNombreCompleto() {
        return nombre + " " + apePaterno + " " + apeMaterno;
    }
}