package com.sise.tareasya.data.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearCategoriaRequest {
    private int idUsuario;
    private String nombreCat;
    private int auditoria = 1;
}