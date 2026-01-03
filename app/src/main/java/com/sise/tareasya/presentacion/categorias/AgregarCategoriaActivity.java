package com.sise.tareasya.presentacion.categorias;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.sise.tareasya.R;
import com.sise.tareasya.data.common.BaseResponse;
import com.sise.tareasya.data.model.categoria;
import com.sise.tareasya.presentacion.pantallaPrincipal.PrincipalViewModel;

import java.util.List;

public class AgregarCategoriaActivity extends AppCompatActivity {

    // Views
    private ImageButton btnCerrar;
    private TextInputEditText etNombre;
    private TextInputLayout tilNombre;
    private MaterialButton btnCrear;
    private ProgressBar progressBar;
    // ViewModel
    private PrincipalViewModel principalViewModel;

    // ID del usuario actual (deberías obtenerlo del login)
    private int idUsuarioActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_agregar_categoria);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Obtener ID del usuario (de SharedPreferences o Intent)
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("id_usuario")) {
            idUsuarioActual = intent.getIntExtra("id_usuario", 0);
        }else {
            // Si no viene del Intent, obtener de SharedPreferences
            SharedPreferences prefs = getSharedPreferences("UsuarioPrefs", MODE_PRIVATE);
            idUsuarioActual = prefs.getInt("idUsuario", 0);
        }

        if (idUsuarioActual == 0) {
            Toast.makeText(this, "Error: No se identificó al usuario", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Log.d("AGREGAR_CATEGORIA", "Usuario ID: " + idUsuarioActual);

        // Inicializar ViewModel
        principalViewModel = new ViewModelProvider(this).get(PrincipalViewModel.class);

        // Inicializar vistas
        initViews();

        // Configurar listeners
        configurarListeners();

        // Observar la respuesta de creación de categoría
        observarCreacionCategoria();
    }

    private void initViews() {
        // Botón cerrar
        btnCerrar = findViewById(R.id.btnCerrar);

        // Inputs (solo nombre, ya que no hay descripción en la BD)
        etNombre = findViewById(R.id.etNombre);
        tilNombre = findViewById(R.id.tilNombre);



        // Botón crear
        btnCrear = findViewById(R.id.btnCrear);

        // Progress bar
        progressBar = findViewById(R.id.progressBar);
    }

    private void configurarListeners() {
        // Botón cerrar
        btnCerrar.setOnClickListener(v -> finish());

        // Validar nombre mientras escribe
        etNombre.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                actualizarEstadoBoton();
                if (s.length() > 0) {
                    tilNombre.setError(null);
                }
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });

        // Botón crear
        btnCrear.setOnClickListener(v -> crearCategoria());
    }

    private void observarCreacionCategoria() {
        principalViewModel.getCrearCategoriaLiveData().observe(this, response -> {
            progressBar.setVisibility(View.GONE);
            btnCrear.setEnabled(true);

            if (response != null) {
                if (response.isSuccess()) {
                    Toast.makeText(AgregarCategoriaActivity.this,
                            "Categoría creada exitosamente", Toast.LENGTH_SHORT).show();

                    // Enviar resultado
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("categoria_creada", true);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    Toast.makeText(AgregarCategoriaActivity.this,
                            "Error: " + response.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void actualizarEstadoBoton() {
        boolean nombreValido = etNombre.getText().toString().trim().length() > 0;
        btnCrear.setEnabled(nombreValido);
    }

    private void crearCategoria() {
        String nombre = etNombre.getText().toString().trim();

        if (nombre.isEmpty()) {
            tilNombre.setError("El nombre es requerido");
            return;
        }

        // Mostrar progress bar
        progressBar.setVisibility(View.VISIBLE);
        btnCrear.setEnabled(false);

        // Crear objeto categoría
        categoria categoria = new categoria(idUsuarioActual, nombre);

        // Llamar al ViewModel para crear la categoría
        principalViewModel.crearCategoria(categoria);
    }
}