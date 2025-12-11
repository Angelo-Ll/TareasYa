package com.sise.tareasya.presentacion.inicioLogin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.sise.tareasya.R;
import com.sise.tareasya.data.common.BaseResponse;
import com.sise.tareasya.data.model.Usuario;
import com.sise.tareasya.presentacion.common.Validator;
import com.sise.tareasya.presentacion.pantallaPrincipal.PrincipalActivity;
import com.sise.tareasya.presentacion.usuarios.AgregarUsuariosActivity;


    // Actividad principal de inicio de sesión de usuarios
public class InicioActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private TextView tvShowPassword, tvRegistro;
    private ProgressBar progressBar;
    private boolean passwordVisible = false;
    private LoginViewModel loginViewModel;

    // Método onCreate: Configura la interfaz y ViewModel
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inicio);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar ViewModel
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // Inicializar vistas
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        tvShowPassword = findViewById(R.id.tvShowPassword);
        tvRegistro = findViewById(R.id.tvRegistro);
        progressBar = findViewById(R.id.progressBar);

        // Configurar loading observer (si tu ViewModel lo tiene)
        // Si no, elimina esta parte

        tvShowPassword.setOnClickListener(v -> AlternarVisibilidaddeContra());

        View btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        if (btnIniciarSesion != null) {
            btnIniciarSesion.setOnClickListener(v -> onClicIniciarSesion(v));
        }

    }

    // onClickIniciarSesion: Valida y envía credenciales al servidor
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

        // Mostrar loading
        progressBar.setVisibility(View.VISIBLE);
        findViewById(R.id.btnIniciarSesion).setEnabled(false);

        // Llamar al ViewModel para hacer login REAL
        loginViewModel.login(email, password).observe(this, new Observer<BaseResponse<Usuario>>() {
            @Override
            public void onChanged(BaseResponse<Usuario> response) {
                // Ocultar loading
                progressBar.setVisibility(View.GONE);
                findViewById(R.id.btnIniciarSesion).setEnabled(true);

                if (response != null) {
                    handleLoginResponse(response);
                }
            }
        });
    }

    // handleLoginResponse: Procesa respuesta del servidor
    private void handleLoginResponse(BaseResponse<Usuario> response) {
        if (response.isSuccess()) {
            Usuario usuario = response.getData();
            if (usuario != null) {
                Toast.makeText(this, "¡Bienvenido " + usuario.getNombre() + "!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, PrincipalActivity.class);
                intent.putExtra("ID_USUARIO", usuario.getIdUsuario());
                intent.putExtra("NOMBRE_USUARIO", usuario.getNombre());
                intent.putExtra("EMAIL_USUARIO", usuario.getEmail());
                startActivity(intent);
                finish();
            }
        } else {
            Toast.makeText(this, "Error: " + response.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    // AlternarVisibilidaddeContra: Muestra/oculta contraseña
    private void AlternarVisibilidaddeContra() {
        if (passwordVisible) {
            etPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT |
                    android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
            tvShowPassword.setText(R.string.mostrar_password);
        } else {
            etPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT |
                    android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            tvShowPassword.setText(R.string.ocultar_password);
        }

        etPassword.setSelection(etPassword.getText().length());
        passwordVisible = !passwordVisible;
    }

    // onClickRegistrarUser: Navega a pantalla de registro
    public void onClickRegistrarUser(View v){
        Intent i = new Intent(this, AgregarUsuariosActivity.class);
        startActivity(i);
    }


}