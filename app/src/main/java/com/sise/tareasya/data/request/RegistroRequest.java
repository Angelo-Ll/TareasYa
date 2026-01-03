package com.sise.tareasya.data.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistroRequest {
    private String nombre;
    private String apePaterno;
    private String apeMaterno;
    private String email;
    private String password;
    private int auditoria = 1;
}