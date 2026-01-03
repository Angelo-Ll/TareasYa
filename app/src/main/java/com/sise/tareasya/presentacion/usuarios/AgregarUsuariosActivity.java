package com.sise.tareasya.presentacion.usuarios;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.sise.tareasya.data.request.RegistroRequest;
import com.sise.tareasya.presentacion.inicioLogin.InicioActivity;


public class AgregarUsuariosActivity extends AppCompatActivity {

    private EditText etNombre, etApellidoPaterno, etApellidoMaterno, etCorreo, etContraseña;
    private Button btnAgregarUsuario;
    private UsuarioViewModel usuarioViewModel; // ← Cambia de PrincipalViewModel a UsuarioViewModel

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_agregar_usuarios);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar ViewModel CORRECTO
        usuarioViewModel = new ViewModelProvider(this).get(UsuarioViewModel.class);

        // Inicializar vistas
        initViews();

        // Configurar listeners
        configurarListeners();

        // Observar el resultado del registro
        observarRegistro();
    }

    private void initViews() {
        // Inicializar EditTexts
        etNombre = findViewById(R.id.etAgregar_user_nombre);
        etApellidoPaterno = findViewById(R.id.etAgregar_user_apePaterno);
        etApellidoMaterno = findViewById(R.id.etAgregar_user_apeMaterno);
        etCorreo = findViewById(R.id.etAgregar_user_correo);
        etContraseña = findViewById(R.id.etAgregar_user_contraseña);

        // Inicializar botones
        btnAgregarUsuario = findViewById(R.id.btnAgregarUsuario);
    }

    private void configurarListeners() {
        btnAgregarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRegistrar(v);
            }
        });
    }

    private void observarRegistro() {
        usuarioViewModel.getRegistroResponse().observe(this, new Observer<BaseResponse<Usuario>>() {
            @Override
            public void onChanged(BaseResponse<Usuario> response) {
                if (response != null) {
                    if (response.isSuccess()) {
                        // Registro exitoso
                        Toast.makeText(AgregarUsuariosActivity.this,
                                "¡Registro exitoso!", Toast.LENGTH_LONG).show();

                        // Opcional: Mostrar datos del usuario registrado
                        Usuario usuario = response.getData();
                        if (usuario != null) {
                            Toast.makeText(AgregarUsuariosActivity.this,
                                    "Bienvenido " + usuario.getNombreCompleto(),
                                    Toast.LENGTH_SHORT).show();
                        }

                        // Regresar a login
                        Intent i = new Intent(AgregarUsuariosActivity.this, InicioActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        // Mostrar error
                        Toast.makeText(AgregarUsuariosActivity.this,
                                "Error: " + response.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    // Validar formulario
    private boolean validarFormulario() {
        boolean valido = true;

        // Limpiar errores anteriores
        clearErrors();

        // Validar nombre
        if (etNombre.getText().toString().trim().isEmpty()) {
            etNombre.setError("El nombre es requerido");
            valido = false;
        }

        // Validar apellido paterno
        if (etApellidoPaterno.getText().toString().trim().isEmpty()) {
            etApellidoPaterno.setError("El apellido paterno es requerido");
            valido = false;
        }

        // Validar email
        String email = etCorreo.getText().toString().trim();
        if (email.isEmpty()) {
            etCorreo.setError("El email es requerido");
            valido = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etCorreo.setError("Email no válido");
            valido = false;
        }

        // Validar contraseña
        String password = etContraseña.getText().toString();
        if (password.isEmpty()) {
            etContraseña.setError("La contraseña es requerida");
            valido = false;
        } else if (password.length() < 6) {
            etContraseña.setError("La contraseña debe tener al menos 6 caracteres");
            valido = false;
        }

        return valido;
    }

    private void clearErrors() {
        etNombre.setError(null);
        etApellidoPaterno.setError(null);
        etApellidoMaterno.setError(null);
        etCorreo.setError(null);
        etContraseña.setError(null);
    }

    // onClickRegistrar: Procesar registro
    public void onClickRegistrar(View v) {
        if (validarFormulario()) {
            // Mostrar loading
            Toast.makeText(this, "Registrando...", Toast.LENGTH_SHORT).show();

            // Crear request
            RegistroRequest request = new RegistroRequest();
            request.setNombre(etNombre.getText().toString().trim());
            request.setApePaterno(etApellidoPaterno.getText().toString().trim());
            request.setApeMaterno(etApellidoMaterno.getText().toString().trim());
            request.setEmail(etCorreo.getText().toString().trim());
            request.setPassword(etContraseña.getText().toString());
            request.setAuditoria(1);

            // Llamar al ViewModel para registrar
            usuarioViewModel.registrarUsuario(request);
        }
    }

    // onClickCancelarRegistro: Regresa a pantalla de login
    public void onClickCancelarRegistro(View v){
        Intent i = new Intent(this, InicioActivity.class);
        startActivity(i);
        finish();
    }
}