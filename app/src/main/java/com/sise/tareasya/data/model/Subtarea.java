package com.sise.tareasya.data.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Subtarea {

    private int idSubtarea;
    private int idTarea;
    private String titulo;
    private boolean estado;
    private int auditoria;
    private String fechaCreacion;

    // Constructor útil para crear subtareas rápidamente
    public Subtarea(int idTarea, String titulo) {
        this.idTarea = idTarea;
        this.titulo = titulo;
        this.estado = false;
        this.auditoria = 1;
    }
}