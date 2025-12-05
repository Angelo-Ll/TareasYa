package com.sise.tareasya.presentacion.inicioLogin;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.sise.tareasya.R;

public class InicioActivity extends AppCompatActivity {

    private EditText etPassword;
    private TextView tvShowPassword;
    private boolean passwordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Habilitar EdgeToEdge
        EdgeToEdge.enable(this);

        // Establecer el layout
        setContentView(R.layout.activity_inicio);

        // Configurar EdgeToEdge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar vistas
        etPassword = findViewById(R.id.etPassword);
        tvShowPassword = findViewById(R.id.tvShowPassword);

        // Configurar clic en "Mostrar"
        tvShowPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility();
            }
        });

        // También puedes agregar funcionalidad para el botón de login
        // y otros elementos si los necesitas
    }

    private void togglePasswordVisibility() {
        if (passwordVisible) {
            // Ocultar contraseña
            etPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT |
                    android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
            tvShowPassword.setText(R.string.mostrar_password); // O usa @string/mostrar_password
        } else {
            // Mostrar contraseña
            etPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT |
                    android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            tvShowPassword.setText(R.string.ocultar_password);
        }

        // Mantener el cursor al final del texto
        etPassword.setSelection(etPassword.getText().length());

        // Cambiar estado
        passwordVisible = !passwordVisible;
    }

    // Opcional: También puedes hacer que funcione al presionar Enter
    private void setupLoginButton() {
        View btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        if (btnIniciarSesion != null) {
            btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Aquí va la lógica para iniciar sesión
                    String email = ((EditText) findViewById(R.id.etEmail)).getText().toString();
                    String password = etPassword.getText().toString();

                    // Validar y procesar login
                    realizarLogin(email, password);
                }
            });
        }
    }

    private void realizarLogin(String email, String password) {
        // Aquí implementas la lógica de login
        // Por ejemplo, validar campos y llamar a API

        if (email.isEmpty() || password.isEmpty()) {
            // Mostrar error
            return;
        }

        // Lógica de autenticación...
    }
}