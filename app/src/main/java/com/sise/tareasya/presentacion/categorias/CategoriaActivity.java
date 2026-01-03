package com.sise.tareasya.presentacion.categorias;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.sise.tareasya.R;
import com.sise.tareasya.data.model.categoria;
import com.sise.tareasya.presentacion.pantallaPrincipal.PrincipalViewModel;

import java.util.List;

import com.sise.tareasya.presentacion.ColorUtils;

public class CategoriaActivity extends AppCompatActivity {

    private PrincipalViewModel viewModel;
    private LinearLayout llCategoriasContainer;
    private Button btnNuevaCategoria;

    private int idUsuario ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_categoria);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Obtener ID del usuario de SharedPreferences
        SharedPreferences prefs = getSharedPreferences("UsuarioPrefs", MODE_PRIVATE);
        idUsuario = prefs.getInt("idUsuario", 0);

        if (idUsuario == 0) {
            // Si no hay usuario logueado, intentar obtener del Intent
            Intent intent = getIntent();
            if (intent != null && intent.hasExtra("ID_USUARIO")) {
                idUsuario = intent.getIntExtra("ID_USUARIO", 0);
            }

            if (idUsuario == 0) {
                Toast.makeText(this, "Debes iniciar sesión primero", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        }

        Log.d("CATEGORIA_ACTIVITY", "ID Usuario: " + idUsuario);

        // Inicializar el MISMO ViewModel que usas en PrincipalActivity
        viewModel = new ViewModelProvider(this).get(PrincipalViewModel.class);

        // Inicializar vistas
        inicializarVistas();

        // Configurar listeners
        configurarListeners();

        // Cargar categorías desde la API
        cargarCategorias();
    }

    private void inicializarVistas() {
        llCategoriasContainer = findViewById(R.id.llCategoriasContainer);
        btnNuevaCategoria = findViewById(R.id.actcategoria_btn_nueva);

        if (llCategoriasContainer == null) {
            Log.e("CATEGORIA_ACTIVITY", "ERROR: No se encontró llCategoriasContainer");
        } else {
            Log.d("CATEGORIA_ACTIVITY", "Contenedor de categorías encontrado");
        }
    }

    private void configurarListeners() {
        btnNuevaCategoria.setOnClickListener(v -> {
            Intent intent = new Intent(this, AgregarCategoriaActivity.class);
            intent.putExtra("ID_USUARIO", idUsuario);
            startActivity(intent);
        });
    }

    private void cargarCategorias() {

        Log.d("CATEGORIA_DEBUG", "=== INICIANDO CARGA DE CATEGORÍAS ===");
        Log.d("CATEGORIA_DEBUG", "ID Usuario para la solicitud: " + idUsuario);

        viewModel.obtenerCategoriasPorUsuario(idUsuario).observe(this, response -> {
            if (response.isSuccess()) {
                List<categoria> categorias = response.getData();
                if (categorias != null && !categorias.isEmpty()) {
                    Log.d("CATEGORIA_ACTIVITY", "Categorías recibidas: " + categorias.size());
                    mostrarCategoriasDinamicas(categorias);
                } else {
                    Log.d("CATEGORIA_ACTIVITY", "No hay categorías disponibles");
                    mostrarMensajeSinCategorias();
                }
            } else {
                Log.e("CATEGORIA_ACTIVITY", "Error al cargar categorías: " + response.getMessage());
                mostrarMensajeError();
            }
        });
    }

    private void mostrarCategoriasDinamicas(List<categoria> categorias) {
        runOnUiThread(() -> {
            // Limpiar contenedor
            llCategoriasContainer.removeAllViews();

            // Crear una tarjeta por cada categoría
            for (categoria cat : categorias) {
                // LOG para verificar datos
                Log.d("CATEGORIA_DATOS",
                        "Categoría: " + cat.getNombreCat() +
                                " - Cantidad: " + cat.getCantidadTareas());

                View cardView = crearCardCategoria(cat);
                llCategoriasContainer.addView(cardView);
            }

            Log.d("CATEGORIA_ACTIVITY", "Tarjetas creadas: " + llCategoriasContainer.getChildCount());
        });
    }

    private View crearCardCategoria(categoria cat) {
        // Inflar el layout de la tarjeta (tu nuevo diseño)
        LayoutInflater inflater = LayoutInflater.from(this);
        View cardView = inflater.inflate(R.layout.cardcategoria, llCategoriasContainer, false);

        // Obtener referencias a los elementos
        ImageView ivIcono = cardView.findViewById(R.id.itemcategoria_iv_icono);
        TextView tvNombre = cardView.findViewById(R.id.itemcategoria_tv_nombre);
        TextView tvContador = cardView.findViewById(R.id.itemcategoria_tv_contador);

        // Configurar datos de la categoría
        tvNombre.setText(cat.getNombreCat());

        // Configurar icono según el tipo de categoría
        configurarIconoCategoria(ivIcono, cat);

        int cantidad = cat.getCantidadTareas();
        String texto = cantidad + (cantidad == 1 ? " tarea" : " tareas");
        tvContador.setText(texto);

        // Configurar clic en toda la tarjeta
        cardView.setOnClickListener(v -> {
            Log.d("CATEGORIA_CLICK", "Categoría seleccionada: " + cat.getNombreCat() + " (ID: " + cat.getIdcategoria() + ")");

            // Aquí podrías:
            // 1. Mostrar tareas de esta categoría específica
            // 2. Abrir actividad de edición de categoría
            // 3. Filtrar tareas en la pantalla principal

            // Ejemplo: Filtrar tareas por categoría
            // Intent intent = new Intent(this, TareasPorCategoriaActivity.class);
            // intent.putExtra("ID_CATEGORIA", cat.getIdcategoria());
            // intent.putExtra("NOMBRE_CATEGORIA", cat.getNombreCat());
            // startActivity(intent);
        });

        return cardView;
    }

    private void configurarIconoCategoria(ImageView imageView, categoria cat) {
        String nombre = cat.getNombreCat().toLowerCase();
        int iconResource;

        // Asignar iconos basados en el nombre de la categoría
        if (nombre.contains("hogar") || nombre.contains("casa") || nombre.contains("doméstico")) {
            iconResource = android.R.drawable.ic_menu_myplaces;
        } else if (nombre.contains("trabajo") || nombre.contains("oficina")) {
            iconResource = android.R.drawable.ic_menu_agenda;
        } else if (nombre.contains("personal") || nombre.contains("vida")) {
            iconResource = android.R.drawable.ic_menu_my_calendar;
        } else if (nombre.contains("estudio") || nombre.contains("aprender")) {
            iconResource = android.R.drawable.ic_menu_edit;
        } else if (nombre.contains("compras") || nombre.contains("tienda")) {
            iconResource = android.R.drawable.ic_menu_add;
        } else if (nombre.contains("salud") || nombre.contains("médico")) {
            iconResource = android.R.drawable.ic_menu_compass;
        } else if (nombre.contains("urgente") || nombre.contains("importante")) {
            // Usar ic_dialog_alert en lugar de ic_menu_warning
            iconResource = android.R.drawable.ic_dialog_alert;
        } else if (nombre.contains("etiqueta") || nombre.contains("tag")) {
            // Usar ic_menu_info_details o ic_menu_share
            iconResource = android.R.drawable.ic_menu_info_details;
        } else {
            // Icono por defecto
            iconResource = android.R.drawable.ic_menu_myplaces;
        }

        imageView.setImageResource(iconResource);

        imageView.setColorFilter(ColorUtils.getColorIntForCategory(cat.getNombreCat()));

    }

    private int obtenerColorCategoria(categoria cat) {
        return ColorUtils.getColorIntForCategory(cat.getNombreCat());
    }



    private void mostrarMensajeSinCategorias() {
        runOnUiThread(() -> {
            llCategoriasContainer.removeAllViews();

            TextView tvMensaje = new TextView(this);
            tvMensaje.setText("No tienes categorías creadas");
            tvMensaje.setTextSize(18);
            tvMensaje.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tvMensaje.setPadding(0, 100, 0, 0);

            llCategoriasContainer.addView(tvMensaje);
        });
    }

    private void mostrarMensajeError() {
        runOnUiThread(() -> {
            llCategoriasContainer.removeAllViews();

            TextView tvMensaje = new TextView(this);
            tvMensaje.setText("Error al cargar categorías\nIntenta nuevamente");
            tvMensaje.setTextSize(16);
            tvMensaje.setTextColor(0xFFF44336); // Rojo
            tvMensaje.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tvMensaje.setPadding(0, 100, 0, 0);

            llCategoriasContainer.addView(tvMensaje);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recargar categorías cuando la actividad se reanude
        cargarCategorias();
    }

    // Método auxiliar para convertir dp a px (igual que en PrincipalActivity)
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}