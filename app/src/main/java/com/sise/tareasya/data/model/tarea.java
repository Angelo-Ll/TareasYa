package com.sise.tareasya.data.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class tarea {

    private int idTarea;
    private int idUsuario;
    private Integer idCategoria;
    private String titulo;
    private String descripcion;
    private String fechaLimite;
    private String recordatorioHora;
    private String prioridad; // "A", "M", "B"
    private boolean estado;
    private int auditoria;
    private String fechaCreacion;
    private categoria categoria; // Usando tu clase categoria existente
    private List<Subtarea> subtareas;

    // Constructor útil para crear tareas rápidamente
    public tarea(int idUsuario, int idCategoria, String titulo) {
        this.idUsuario = idUsuario;
        this.idCategoria = idCategoria;
        this.titulo = titulo;
        this.estado = false;
        this.auditoria = 1;
    }

    // Métodos útiles adicionales
    public String getPrioridadTexto() {
        if (prioridad == null) return "Sin prioridad";
        switch (prioridad) {
            case "A": return "Alta";
            case "M": return "Media";
            case "B": return "Baja";
            default: return "Sin prioridad";
        }
    }

    public int getColorPrioridad() {
        if (prioridad == null) return 0xFF9E9E9E;
        switch (prioridad) {
            case "A": return 0xFFF44336; // Rojo
            case "M": return 0xFFFF9800; // Naranja
            case "B": return 0xFF4CAF50; // Verde
            default: return 0xFF9E9E9E;  // Gris
        }
    }

    public int getSubtareasCompletadas() {
        if (subtareas == null) return 0;
        int completadas = 0;
        for (Subtarea subtarea : subtareas) {
            if (subtarea.isEstado()) completadas++;
        }
        return completadas;
    }

    public int getTotalSubtareas() {
        return subtareas != null ? subtareas.size() : 0;
    }

    public String getInfoSubtareas() {
        int completadas = getSubtareasCompletadas();
        int total = getTotalSubtareas();
        return completadas + "/" + total + " completadas";
    }

    public boolean tieneSubtareas() {
        return subtareas != null && !subtareas.isEmpty();
    }

    public String getFechaLimiteFormateada() {
        if (fechaLimite == null) return "Sin fecha";
        // Puedes formatear la fecha aquí si necesitas
        return fechaLimite;
    }

    // Método para saber si la tarea está vencida
    public boolean estaVencida() {
        if (fechaLimite == null || estado) return false;
        // Lógica para comparar fechas
        return false; // Implementa según necesites
    }
}