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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sise.tareasya.R;
import com.sise.tareasya.data.model.categoria;
import com.sise.tareasya.data.model.tarea;
import com.sise.tareasya.presentacion.pantallaPrincipal.TareaAdapter;
import com.sise.tareasya.presentacion.administradorVistas.AgregarTareaActivity;

import java.util.List;

public class PrincipalActivity extends AppCompatActivity {

    private PrincipalViewModel viewModel;
    private LinearLayout llCategoriasContainer;
    private FloatingActionButton fabAgregar;
    private FloatingActionButton fabCategorias;
    private RecyclerView rvTareas;
    private EditText etSearch;
    private TareaAdapter tareaAdapter;

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

        // Configurar RecyclerView
        configurarRecyclerView();

        // Configurar listeners
        configurarListeners();

        // Cargar datos
        cargarDatos();
    }

    private void inicializarVistas() {
        // Buscar las vistas con los tipos correctos
        llCategoriasContainer = findViewById(R.id.llCategoriasContainer);
        fabAgregar = findViewById(R.id.fabAgregar);
        fabCategorias = findViewById(R.id.fabCategorias);
        rvTareas = findViewById(R.id.rvTareas);
        etSearch = findViewById(R.id.etSearch);

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

        if (rvTareas == null) {
            Log.e("PRINCIPAL", "ERROR: No se encontró rvTareas");
        } else {
            Log.d("PRINCIPAL", "RecyclerView tareas encontrado");
        }
    }

    private void configurarRecyclerView() {
        // Configurar LayoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvTareas.setLayoutManager(layoutManager);

        // Crear adapter vacío inicialmente
        tareaAdapter = new TareaAdapter(null, new TareaAdapter.OnTareaClickListener() {
            @Override
            public void onTareaClick(tarea tarea) {
                // Cuando se hace clic en una tarea
                Log.d("TAREA_CLICK", "Tarea clickeada: " + tarea.getTitulo());
                // TODO: Navegar a detalle de tarea
                // Intent intent = new Intent(PrincipalActivity.this, DetalleTareaActivity.class);
                // intent.putExtra("ID_TAREA", tarea.getIdTarea());
                // startActivity(intent);
            }
        });

        rvTareas.setAdapter(tareaAdapter);
    }

    private void configurarListeners() {
        // Listener para el FAB de agregar tarea
        fabAgregar.setOnClickListener(v -> {
            Intent intent = new Intent(PrincipalActivity.this, AgregarTareaActivity.class);
            intent.putExtra("ID_USUARIO", idUsuario);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        // Listener para el FAB de categorías
        fabCategorias.setOnClickListener(v -> {
            Log.d("PRINCIPAL", "Clic en botón de categorías");
            // TODO: Implementar navegación a pantalla de categorías
            // Intent intent = new Intent(PrincipalActivity.this, CategoriasActivity.class);
            // startActivity(intent);
            // overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        // Buscador
        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            String busqueda = etSearch.getText().toString().trim();
            if (!busqueda.isEmpty()) {
                buscarTareas(busqueda);
            }
            return true;
        });
    }

    private void cargarDatos() {
        // Cargar tareas
        viewModel.obtenerTareasPorUsuario(idUsuario).observe(this, response -> {
            if (response.isSuccess()) {
                List<tarea> tareas = response.getData();
                if (tareas != null && !tareas.isEmpty()) {
                    Log.d("TAREAS", "Tareas recibidas: " + tareas.size());
                    mostrarTareas(tareas);
                } else {
                    Log.d("TAREAS", "No hay tareas disponibles");
                    mostrarTareasVacia();
                }
            } else {
                Log.e("TAREAS", "Error: " + response.getMessage());
                mostrarErrorTareas();
            }
        });

        // Cargar categorías
        viewModel.obtenerCategoriasPorUsuario(idUsuario).observe(this, response -> {
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
    }

    private void mostrarTareas(List<tarea> tareas) {
        runOnUiThread(() -> {
            tareaAdapter.setTareas(tareas);

            // Actualizar título con cantidad
            TextView tvTituloTareas = findViewById(R.id.tvTituloTareas);
            if (tvTituloTareas != null) {
                tvTituloTareas.setText("Tus tareas (" + tareas.size() + ")");
            }
        });
    }

    private void mostrarTareasVacia() {
        runOnUiThread(() -> {
            // Mostrar mensaje de "No hay tareas"
            Log.d("UI", "No hay tareas para mostrar");
        });
    }

    private void mostrarErrorTareas() {
        runOnUiThread(() -> {
            // Mostrar mensaje de error
            Log.e("UI", "Error al cargar tareas");
        });
    }

    private void buscarTareas(String texto) {
        // Implementar búsqueda local o en API
        Log.d("BUSQUEDA", "Buscando: " + texto);
        // TODO: Filtrar tareas por texto
    }

    // MÉTODOS PARA CATEGORÍAS (del código original)
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

    @Override
    protected void onResume() {
        super.onResume();
        // Recargar datos cuando vuelva a la pantalla
        viewModel.obtenerTareasPorUsuario(idUsuario);
        viewModel.obtenerCategoriasPorUsuario(idUsuario);
    }
}