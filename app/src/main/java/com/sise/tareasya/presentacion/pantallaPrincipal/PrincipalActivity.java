package com.sise.tareasya.presentacion.pantallaPrincipal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton; // AÑADE ESTO
import com.sise.tareasya.R;
import com.sise.tareasya.data.common.BaseResponse;
import com.sise.tareasya.data.model.categoria;
import com.sise.tareasya.presentacion.administradorVistas.AgregarTareaActivity;

import java.util.List;

public class PrincipalActivity extends AppCompatActivity {

    private PrincipalViewModel viewModel;
    private LinearLayout llCategoriasContainer;
    private FloatingActionButton fabAgregar; // CAMBIADO: de Button a FloatingActionButton
    private FloatingActionButton fabCategorias; // NUEVO: para el botón de categorías
    private int idUsuario = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_principal);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Obtener ID del usuario del Intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("ID_USUARIO")) {
            idUsuario = intent.getIntExtra("ID_USUARIO", 1);
            Log.d("PRINCIPAL", "ID Usuario recibido: " + idUsuario);
        }

        // Inicializar ViewModel
        viewModel = new ViewModelProvider(this).get(PrincipalViewModel.class);

        // Inicializar vistas
        inicializarVistas();

        // Configurar listeners
        configurarListeners();

        // Cargar categorías dinámicamente desde API
        cargarCategorias();
    }

    private void inicializarVistas() {
        // Buscar las vistas con los tipos correctos
        llCategoriasContainer = findViewById(R.id.llCategoriasContainer);
        fabAgregar = findViewById(R.id.fabAgregar); // CAMBIADO: ya no es btnAgregar
        fabCategorias = findViewById(R.id.fabCategorias); // NUEVO

        // Verificar que se encontraron
        if (llCategoriasContainer == null) {
            Log.e("PRINCIPAL", "ERROR: No se encontró llCategoriasContainer");
        } else {
            Log.d("PRINCIPAL", "Contenedor de categorías encontrado");
        }

        if (fabAgregar == null) {
            Log.e("PRINCIPAL", "ERROR: No se encontró fabAgregar");
        } else {
            Log.d("PRINCIPAL", "FAB agregar encontrado");
        }

        if (fabCategorias == null) {
            Log.e("PRINCIPAL", "ERROR: No se encontró fabCategorias");
        } else {
            Log.d("PRINCIPAL", "FAB categorías encontrado");
        }
    }

    private void configurarListeners() {
        // Listener para el FAB de agregar tarea
        fabAgregar.setOnClickListener(v -> {
            Intent intent = new Intent(PrincipalActivity.this, AgregarTareaActivity.class);
            intent.putExtra("ID_USUARIO", idUsuario);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        // Listener para el FAB de categorías (NUEVO)
        fabCategorias.setOnClickListener(v -> {
            Log.d("PRINCIPAL", "Clic en botón de categorías");
            // TODO: Implementar navegación a pantalla de categorías
            // Intent intent = new Intent(PrincipalActivity.this, CategoriasActivity.class);
            // startActivity(intent);
            // overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }

    private void cargarCategorias() {
        // Observar las categorías del ViewModel
        viewModel.getCategoriasLiveData().observe(this, response -> {
            if (response.isSuccess()) {
                List<categoria> categorias = response.getData();
                if (categorias != null && !categorias.isEmpty()) {
                    Log.d("CATEGORIAS", "Categorías recibidas: " + categorias.size());
                    mostrarCategoriasDinamicas(categorias);
                } else {
                    Log.d("CATEGORIAS", "No hay categorías disponibles");
                    mostrarCategoriasPorDefecto();
                }
            } else {
                Log.e("CATEGORIAS", "Error: " + response.getMessage());
                mostrarCategoriasPorDefecto();
            }
        });

        // Llamar al ViewModel para cargar categorías
        Log.d("CATEGORIAS", "Solicitando categorías para usuario: " + idUsuario);
        viewModel.cargarCategorias(idUsuario);
    }

    private void mostrarCategoriasDinamicas(List<categoria> categorias) {
        if (llCategoriasContainer != null) {
            // Limpiar contenedor
            llCategoriasContainer.removeAllViews();

            // Crear un botón por cada categoría
            for (categoria cat : categorias) {
                Button botonCategoria = crearBotonCategoria(cat);
                llCategoriasContainer.addView(botonCategoria);
            }

            Log.d("CATEGORIAS", "Botones creados: " + llCategoriasContainer.getChildCount());
        }
    }

    private Button crearBotonCategoria(categoria cat) {
        Button boton = new Button(this);

        // Configurar propiedades
        boton.setText(cat.getNombreCat());
        boton.setTag(cat.getIdcategoria());

        // Estilos
        boton.setBackgroundColor(obtenerColorCategoria(cat));
        boton.setTextColor(getResources().getColor(android.R.color.white));
        boton.setAllCaps(false);
        boton.setTextSize(14);

        // Padding para mejor apariencia
        int paddingHorizontal = dpToPx(16);
        int paddingVertical = dpToPx(8);
        boton.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);

        // Layout params con márgenes
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, dpToPx(8), 0);
        boton.setLayoutParams(params);

        // Click listener
        boton.setOnClickListener(v -> {
            int idCategoria = (int) v.getTag();
            String nombreCategoria = cat.getNombreCat();
            Log.d("CATEGORIA_CLICK", "Categoría seleccionada: " + nombreCategoria + " (ID: " + idCategoria + ")");
            // TODO: Filtrar tareas por categoría
        });

        return boton;
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    private int obtenerColorCategoria(categoria cat) {
        String nombre = cat.getNombreCat().toLowerCase();

        // Colores basados en el nombre
        if (nombre.contains("personal")) {
            return 0xFF2196F3; // Azul
        } else if (nombre.contains("trabajo")) {
            return 0xFF4CAF50; // Verde
        } else if (nombre.contains("estudio") || nombre.contains("escuela")) {
            return 0xFFFF9800; // Naranja
        } else if (nombre.contains("urgente") || nombre.contains("importante")) {
            return 0xFFF44336; // Rojo
        } else if (nombre.contains("hogar") || nombre.contains("casa")) {
            return 0xFF9C27B0; // Morado
        }

        // Colores aleatorios basados en ID
        int[] colores = {
                0xFF2196F3, // Azul
                0xFF4CAF50, // Verde
                0xFFFF9800, // Naranja
                0xFFF44336, // Rojo
                0xFF9C27B0, // Morado
                0xFF795548, // Café
                0xFF607D8B  // Gris azulado
        };

        return colores[cat.getIdcategoria() % colores.length];
    }

    private void mostrarCategoriasPorDefecto() {
        if (llCategoriasContainer != null) {
            llCategoriasContainer.removeAllViews();

            // Botones por defecto (solo si la API falla)
            String[] categoriasDefault = {"Personal", "Trabajo", "Estudio"};
            int[] coloresDefault = {0xFF2196F3, 0xFF4CAF50, 0xFFFF9800};

            for (int i = 0; i < categoriasDefault.length; i++) {
                Button boton = new Button(this);
                boton.setText(categoriasDefault[i]);
                boton.setBackgroundColor(coloresDefault[i]);
                boton.setTextColor(getResources().getColor(android.R.color.white));
                boton.setAllCaps(false);
                boton.setTextSize(14);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 0, dpToPx(8), 0);
                boton.setLayoutParams(params);

                llCategoriasContainer.addView(boton);
            }

            Log.d("CATEGORIAS", "Mostrando categorías por defecto");
        }
    }
}