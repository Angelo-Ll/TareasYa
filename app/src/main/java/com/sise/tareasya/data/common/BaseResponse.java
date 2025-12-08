package com.sise.tareasya.data.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse<T> {
    private boolean success;
    private String message;
    private T data;

    // Métodos estáticos de fábrica (opcionales pero útiles)

    /**
     * Crea una respuesta exitosa
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(true, "Ok", data);
    }

    /**
     * Crea una respuesta exitosa con mensaje personalizado
     */
    public static <T> BaseResponse<T> success(String message, T data) {
        return new BaseResponse<>(true, message, data);
    }

    /**
     * Crea una respuesta de error
     */
    public static <T> BaseResponse<T> error(String message) {
        return new BaseResponse<>(false, message, null);
    }

    /**
     * Crea una respuesta de error con datos
     */
    public static <T> BaseResponse<T> error(String message, T data) {
        return new BaseResponse<>(false, message, data);
    }
}