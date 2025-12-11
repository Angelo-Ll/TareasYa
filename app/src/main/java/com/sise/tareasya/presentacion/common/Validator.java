package com.sise.tareasya.presentacion.common;

import android.text.TextUtils;
import android.widget.EditText;
import java.util.regex.Pattern;

// Clase para validar campos de entrada (EditText) con diferentes reglas
public class Validator {
    private EditText editText;
    private String message = "";
    private boolean isValid = true;

    private Validator(EditText editText) {
        this.editText = editText;
    }
    // Inicia la cadena de validación
    public static Validator with(EditText editText) {
        return new Validator(editText);
    }

    // Valida que el campo no esté vacío
    public Validator required() {
        if (!isValid) return this;

        String text = editText.getText().toString().trim();
        if (TextUtils.isEmpty(text)) {
            message = "Este campo es requerido";
            editText.setError(message);
            editText.requestFocus();
            isValid = false;
        }
        return this;
    }
    // Valida formato de correo electrónico
    public Validator email() {
        if (!isValid) return this;

        String text = editText.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";

        if (!Pattern.matches(emailPattern, text)) {
            message = "Ingresa un correo válido";
            editText.setError(message);
            editText.requestFocus();
            isValid = false;
        }
        return this;
    }
    // Valida longitud mínima
    public Validator minLength(int length) {
        if (!isValid) return this;

        String text = editText.getText().toString().trim();
        if (text.length() < length) {
            message = "Mínimo " + length + " caracteres";
            editText.setError(message);
            editText.requestFocus();
            isValid = false;
        }
        return this;
    }
    // Valida longitud máxima
    public Validator maxLength(int length) {
        if (!isValid) return this;

        String text = editText.getText().toString().trim();
        if (text.length() > length) {
            message = "Máximo " + length + " caracteres";
            editText.setError(message);
            editText.requestFocus();
            isValid = false;
        }
        return this;
    }
    // Ejecuta validación y muestra/oculta errores
    public boolean validate() {
        if (isValid) {
            editText.setError(null);
        }
        return isValid;
    }


}