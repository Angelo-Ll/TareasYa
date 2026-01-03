package com.sise.tareasya.presentacion;

import android.graphics.Color;
import android.util.Log;

public class ColorUtils {

    private static final String TAG = "ColorUtils";

    // Colores predefinidos (Material Design colors)
    private static final String[] CATEGORY_COLORS = {
            "#2196F3", // Azul - Personal
            "#4CAF50", // Verde - Trabajo
            "#FF9800", // Naranja - Estudio
            "#F44336", // Rojo - Urgente
            "#9C27B0", // Morado - Hogar
            "#795548", // Café - Compras
            "#607D8B", // Gris azulado - Salud
            "#009688", // Turquesa - Deporte
            "#FFC107", // Ámbar - Finanzas
            "#3F51B5", // Índigo - Viajes
            "#E91E63", // Rosa - Familia
            "#00BCD4"  // Cian - Otros
    };

    /**
     * Obtiene un color HEX basado en el nombre de la categoría
     * Mismo nombre = mismo color (consistente)
     */
    public static String getColorForCategory(String categoryName) {
        if (categoryName == null || categoryName.trim().isEmpty()) {
            return "#2196F3";
        }

        String name = categoryName.toLowerCase().trim();


        if (name.contains("personal") || name.contains("vida") || name.contains("privado")) {
            return "#2196F3"; // Azul
        } else if (name.contains("trabajo") || name.contains("oficina") || name.contains("empleo")) {
            return "#4CAF50"; // Verde
        } else if (name.contains("estudio") || name.contains("aprender") || name.contains("escuela") || name.contains("universidad")) {
            return "#FF9800"; // Naranja
        } else if (name.contains("urgente") || name.contains("importante") || name.contains("prioridad")) {
            return "#F44336"; // Rojo
        } else if (name.contains("hogar") || name.contains("casa") || name.contains("doméstico")) {
            return "#9C27B0"; // Morado
        } else if (name.contains("compras") || name.contains("tienda") || name.contains("supermercado")) {
            return "#795548"; // Café
        } else if (name.contains("salud") || name.contains("médico") || name.contains("hospital")) {
            return "#607D8B"; // Gris azulado
        } else if (name.contains("deporte") || name.contains("ejercicio") || name.contains("gimnasio")) {
            return "#009688"; // Turquesa
        } else if (name.contains("finanzas") || name.contains("dinero") || name.contains("banco")) {
            return "#FFC107"; // Ámbar
        } else if (name.contains("viajes") || name.contains("vacaciones") || name.contains("turismo")) {
            return "#3F51B5"; // Índigo
        } else if (name.contains("familia") || name.contains("amigos") || name.contains("social")) {
            return "#E91E63"; // Rosa
        }

        // Para otros nombres, usar hash para consistencia
        return getColorFromHash(name);
    }


    private static String getColorFromHash(String text) {
        try {
            int hash = text.hashCode();
            int index = Math.abs(hash) % CATEGORY_COLORS.length;
            return CATEGORY_COLORS[index];
        } catch (Exception e) {
            Log.e(TAG, "Error al calcular color from hash: " + e.getMessage());
            return "#2196F3";
        }
    }


    public static int parseColor(String hexColor) {
        try {
            return Color.parseColor(hexColor);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Color HEX inválido: " + hexColor + ", usando azul por defecto");
            return Color.parseColor("#2196F3");
        }
    }

    /**
     * Obtiene color como int directamente desde nombre
     */
    public static int getColorIntForCategory(String categoryName) {
        String hexColor = getColorForCategory(categoryName);
        return parseColor(hexColor);
    }
}
