package com.sise.tareasya.data.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class categoria {

    private int idcategoria;
    private int idUsuario;
    private String nombreCat;
    private int auditoria;
    private String fechaCreacion;

    // Constructor útil para crear categorías rápidamente
    public categoria(int idUsuario, String nombreCat) {
        this.idUsuario = idUsuario;
        this.nombreCat = nombreCat;
        this.auditoria = 1;
    }
}