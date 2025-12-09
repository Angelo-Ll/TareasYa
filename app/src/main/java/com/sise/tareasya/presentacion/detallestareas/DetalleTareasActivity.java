package com.sise.tareasya.presentacion.detallestareas;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.sise.tareasya.R;
import com.sise.tareasya.data.model.tarea;

public class DetalleTareasActivity extends AppCompatActivity {

    private DetalleTareasViewModel viewModel;

    // Views
    private TextView tvNombreTarea;
    private CheckBox cbTarea;
    private TextView tvDescripcion;
    private TextView tvCategoria;
    private TextView tvFecha;
    private TextView tvHora;
    private TextView tvPrioridad;
    private TextView tvEstado;
    private Button btnEditar;
    private Button btnEliminar;

    private int idTarea = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detalle_tareas);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Obtener ID de la tarea del Intent
        obtenerIdTarea();

        // Inicializar ViewModel
        viewModel = new ViewModelProvider(this).get(DetalleTareasViewModel.class);

        // Inicializar vistas
        inicializarVistas();

        // Configurar listeners
        configurarListeners();

        // Cargar datos de la tarea
        if (idTarea != -1) {
            cargarDetalleTarea();
        } else {
            Toast.makeText(this, "Error: No se encontró la tarea", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void obtenerIdTarea() {
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("ID_TAREA")) {
            idTarea = extras.getInt("ID_TAREA", -1);
        }
    }

    private void inicializarVistas() {
        tvNombreTarea = findViewById(R.id.tvNombreTarea);
        cbTarea = findViewById(R.id.cbTarea);
        tvDescripcion = findViewById(R.id.tvDescripcion);
        tvCategoria = findViewById(R.id.tvCategoria);
        tvFecha = findViewById(R.id.tvFecha);
        tvHora = findViewById(R.id.tvHora);
        tvPrioridad = findViewById(R.id.tvPrioridad);
        tvEstado = findViewById(R.id.tvEstado);
        btnEditar = findViewById(R.id.btnEditar);
        btnEliminar = findViewById(R.id.btnEliminar);
    }

    private void configurarListeners() {
        // CheckBox para marcar/completar tarea
        cbTarea.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                tvEstado.setText("Completada");
                tvEstado.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                // Actualizar estado en ViewModel
                viewModel.actualizarEstadoTarea(idTarea, true);
            } else {
                tvEstado.setText("Pendiente");
                tvEstado.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
                // Actualizar estado en ViewModel
                viewModel.actualizarEstadoTarea(idTarea, false);
            }
        });

        // Botón Editar
        btnEditar.setOnClickListener(v -> {
            Toast.makeText(this, "Funcionalidad de editar en desarrollo", Toast.LENGTH_SHORT).show();
            // TODO: Navegar a actividad de edición
            // Intent intent = new Intent(this, EditarTareaActivity.class);
            // intent.putExtra("ID_TAREA", idTarea);
            // startActivity(intent);
        });

        // Botón Eliminar
        btnEliminar.setOnClickListener(v -> {
            // Mostrar diálogo de confirmación
            new android.app.AlertDialog.Builder(this)
                    .setTitle("Eliminar Tarea")
                    .setMessage("¿Estás seguro de que quieres eliminar esta tarea?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        viewModel.eliminarTarea(idTarea);
                        Toast.makeText(this, "Tarea eliminada", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    private void cargarDetalleTarea() {
        viewModel.obtenerTareaPorId(idTarea).observe(this, response -> {
            if (response.isSuccess()) {
                tarea tarea = response.getData();
                if (tarea != null) {
                    mostrarDatosTarea(tarea);
                } else {
                    Toast.makeText(this, "No se encontró la tarea", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } else {
                Toast.makeText(this, "Error al cargar la tarea: " + response.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void mostrarDatosTarea(tarea tarea) {
        // Configurar CheckBox según el estado booleano
        cbTarea.setChecked(tarea.isEstado());

        // Título
        tvNombreTarea.setText(tarea.getTitulo());

        // Descripción
        tvDescripcion.setText(tarea.getDescripcion() != null ? tarea.getDescripcion() : "Sin descripción");

        // Categoría - usando el objeto categoria
        if (tarea.getCategoria() != null && tarea.getCategoria().getNombreCat() != null) {
            tvCategoria.setText(tarea.getCategoria().getNombreCat());
        } else {
            tvCategoria.setText("Sin categoría");
        }

        // Fecha y Hora - usando los campos correctos
        tvFecha.setText(tarea.getFechaLimite() != null ? tarea.getFechaLimite() : "Sin fecha");
        tvHora.setText(tarea.getRecordatorioHora() != null ? tarea.getRecordatorioHora() : "Sin hora");

        // Prioridad - usando el método helper que ya tienes
        tvPrioridad.setText(tarea.getPrioridadTexto());

        // Estado - convertir boolean a String
        String estadoTexto = tarea.isEstado() ? "Completada" : "Pendiente";
        tvEstado.setText(estadoTexto);

        // Cambiar color del estado
        if (tarea.isEstado()) {
            tvEstado.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        } else {
            tvEstado.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
        }
    }
}