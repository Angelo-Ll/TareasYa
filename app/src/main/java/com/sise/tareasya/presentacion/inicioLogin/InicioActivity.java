package com.sise.tareasya.presentacion.inicioLogin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.sise.tareasya.R;
import com.sise.tareasya.presentation.common.Validator;
import com.sise.tareasya.presentacion.pantallaPrincipal.PrincipalActivity;

public class InicioActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private TextView tvShowPassword, tvRegistro, tvDigito;
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
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        tvShowPassword = findViewById(R.id.tvShowPassword);
        tvRegistro = findViewById(R.id.tvRegistro);

        // Configurar clic en "Mostrar"
        tvShowPassword.setOnClickListener(v -> togglePasswordVisibility());

        // Configurar botón de login (si está en el XML con android:onClick)
        // O puedes hacerlo programáticamente:
        View btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        if (btnIniciarSesion != null) {
            btnIniciarSesion.setOnClickListener(v -> onClicIniciarSesion(v));
        }

        // Configurar registro (si existe)
        if (tvRegistro != null) {
            tvRegistro.setOnClickListener(v -> {
                // Ir a pantalla de registro
                // Intent intent = new Intent(this, RegistroActivity.class);
                // startActivity(intent);
            });
        }
    }

    // Método llamado desde el XML con android:onClick="onClicIniciarSesion"
    public void onClicIniciarSesion(View v) {

        // Validar email
        if (!Validator.with(etEmail)
                .required()
                .email()
                .validate()) {
            return;
        }

        // Validar contraseña
        if (!Validator.with(etPassword)
                .required()
                .minLength(6)
                .validate()) {
            return;
        }

        // Obtener datos
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Aquí iría la llamada a tu API
        // Por ahora simulamos login exitoso

        // Mostrar mensaje
        Toast.makeText(this, "Iniciando sesión...", Toast.LENGTH_SHORT).show();

        // Simular delay de red
        new android.os.Handler().postDelayed(() -> {
            // Aquí normalmente verificarías con tu API
            // Por ahora vamos directamente a la pantalla principal

            Intent intent = new Intent(this, PrincipalActivity.class);
            startActivity(intent);
            finish(); // Opcional: cierra esta actividad
        }, 1500);
    }

    private void togglePasswordVisibility() {
        if (passwordVisible) {
            // Ocultar contraseña
            etPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT |
                    android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
            tvShowPassword.setText(R.string.mostrar_password);
        } else {
            // Mostrar contraseña
            etPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT |
                    android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            tvShowPassword.setText(R.string.ocultar_password);
        }

        // Mantener cursor al final
        etPassword.setSelection(etPassword.getText().length());

        // Cambiar estado
        passwordVisible = !passwordVisible;
    }
}