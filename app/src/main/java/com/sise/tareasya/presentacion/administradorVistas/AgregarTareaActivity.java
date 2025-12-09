package com.sise.tareasya.presentacion.administradorVistas;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sise.tareasya.R;
import com.sise.tareasya.data.api.RetrofitClient;
import com.sise.tareasya.data.api.TareaApi;
import com.sise.tareasya.data.common.BaseResponse;
import com.sise.tareasya.data.model.tarea;
import com.sise.tareasya.data.model.categoria;
import com.sise.tareasya.data.api.CategoriaApi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgregarTareaActivity extends AppCompatActivity {

    private EditText etTitulo, etDescripcion, etFecha, etHora;
    private Spinner spinnerCategoria, spinnerPrioridad;
    private Button btnGuardar;

    private int idUsuario;
    private List<categoria> listaCategorias = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_tarea);

        // Obtener ID del usuario del Intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("ID_USUARIO")) {
            idUsuario = intent.getIntExtra("ID_USUARIO", 1);
            Log.d("AGREGAR_TAREA", "ID Usuario recibido: " + idUsuario);
        }

        // Inicializar vistas
        inicializarVistas();

        // Cargar categorías del usuario
        cargarCategorias();

        // Configurar spinners
        configurarSpinnerPrioridad();

        // Configurar selectores de fecha y hora
        configurarSelectorFecha();
        configurarSelectorHora();

        // Configurar botón Guardar
        configurarBotonGuardar();
    }

    private void inicializarVistas() {
        etTitulo = findViewById(R.id.etTitulo);
        etDescripcion = findViewById(R.id.etDescripcion);
        etFecha = findViewById(R.id.etFecha);
        etHora = findViewById(R.id.etHora);
        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        spinnerPrioridad = findViewById(R.id.spinnerPrioridad);
        btnGuardar = findViewById(R.id.btnGuardarTarea);
    }

    private void cargarCategorias() {
        CategoriaApi categoriaApi = RetrofitClient.getCategoriaApi();
        categoriaApi.obtenerCategoriasPorUsuario(idUsuario).enqueue(new Callback<BaseResponse<List<categoria>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<categoria>>> call, Response<BaseResponse<List<categoria>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    listaCategorias = response.body().getData();
                    if (listaCategorias != null && !listaCategorias.isEmpty()) {
                        configurarSpinnerCategorias();
                    } else {
                        mostrarMensaje("No hay categorías disponibles. Crea una primero.");
                    }
                } else {
                    mostrarMensaje("Error al cargar categorías");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<categoria>>> call, Throwable t) {
                mostrarMensaje("Error de conexión: " + t.getMessage());
                Log.e("CATEGORIAS", "Error: " + t.toString());
            }
        });
    }

    private void configurarSpinnerCategorias() {
        List<String> nombresCategorias = new ArrayList<>();

        nombresCategorias.add("Seleccionar categoría");

        for (categoria cat : listaCategorias) {
            nombresCategorias.add(cat.getNombreCat());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, nombresCategorias);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(adapter);

        Log.d("CATEGORIAS", "Categorías cargadas: " + nombresCategorias.size());
    }

    private void configurarSpinnerPrioridad() {
        String[] prioridades = {"Seleccionar prioridad", "Alta (A)", "Media (M)", "Baja (B)"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, prioridades);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPrioridad.setAdapter(adapter);
    }

    private void configurarSelectorFecha() {
        etFecha.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        // Formato: yyyy-MM-dd (formato que acepta la API)
                        String fecha = String.format(Locale.getDefault(),
                                "%04d-%02d-%02d", selectedYear, (selectedMonth + 1), selectedDay);
                        etFecha.setText(fecha);
                    },
                    year, month, day);

            datePickerDialog.setTitle("Seleccionar fecha");
            datePickerDialog.show();
        });
    }

    private void configurarSelectorHora() {
        etHora.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    (view, selectedHour, selectedMinute) -> {
                        // Formato: HH:mm:ss (formato que acepta la API)
                        String hora = String.format(Locale.getDefault(),
                                "%02d:%02d:00", selectedHour, selectedMinute);
                        etHora.setText(hora);
                    },
                    hour, minute, true);

            timePickerDialog.setTitle("Seleccionar hora");
            timePickerDialog.show();
        });
    }

    private void configurarBotonGuardar() {
        btnGuardar.setOnClickListener(v -> {
            if (validarFormulario()) {
                guardarTarea();
            }
        });
    }

    private boolean validarFormulario() {
        // Validar título
        String titulo = etTitulo.getText().toString().trim();
        if (titulo.isEmpty()) {
            etTitulo.setError("El título es requerido");
            etTitulo.requestFocus();
            return false;
        }

        // Validar categoría seleccionada
        if (spinnerCategoria.getSelectedItemPosition() == 0) { // 0 = "Seleccionar categoría"
            mostrarMensaje("Selecciona una categoría");
            return false;
        }

        // Validar prioridad seleccionada
        if (spinnerPrioridad.getSelectedItemPosition() == 0) {
            mostrarMensaje("Selecciona una prioridad");
            return false;
        }

        return true;
    }

    private void guardarTarea() {
        // Crear objeto tarea
        tarea nuevaTarea = new tarea();

        // Datos básicos
        nuevaTarea.setIdUsuario(idUsuario);
        nuevaTarea.setTitulo(etTitulo.getText().toString().trim());
        nuevaTarea.setDescripcion(etDescripcion.getText().toString().trim());

        // Categoría
        if (!listaCategorias.isEmpty() && spinnerCategoria.getSelectedItemPosition() > 0) {
            categoria catSeleccionada = listaCategorias.get(spinnerCategoria.getSelectedItemPosition() -1 );
            nuevaTarea.setIdCategoria(catSeleccionada.getIdcategoria());
        }

        // Fecha límite
        if (!etFecha.getText().toString().trim().isEmpty()) {
            nuevaTarea.setFechaLimite(etFecha.getText().toString().trim());
        }

        // Hora recordatorio
        if (!etHora.getText().toString().trim().isEmpty()) {
            nuevaTarea.setRecordatorioHora(etHora.getText().toString().trim());
        }

        // Prioridad
        int posPrioridad = spinnerPrioridad.getSelectedItemPosition();
        String prioridad = "";
        switch (posPrioridad) {
            case 1: prioridad = "A"; break; // Alta
            case 2: prioridad = "M"; break; // Media
            case 3: prioridad = "B"; break; // Baja
        }
        nuevaTarea.setPrioridad(prioridad);

        // Estado y auditoría por defecto
        nuevaTarea.setEstado(false);

        // Mostrar datos en log
        Log.d("TAREA_CREAR", "Título: " + nuevaTarea.getTitulo());
        Log.d("TAREA_CREAR", "Categoría ID: " + nuevaTarea.getIdCategoria());
        Log.d("TAREA_CREAR", "Prioridad: " + nuevaTarea.getPrioridad());

        // Deshabilitar botón mientras se guarda
        btnGuardar.setEnabled(false);
        btnGuardar.setText("Guardando...");

        // Llamar a la API
        TareaApi tareaApi = RetrofitClient.getTareaApi();
        tareaApi.crearTarea(nuevaTarea).enqueue(new Callback<BaseResponse<tarea>>() {
            @Override
            public void onResponse(Call<BaseResponse<tarea>> call, Response<BaseResponse<tarea>> response) {
                btnGuardar.setEnabled(true);
                btnGuardar.setText("Guardar Tarea");

                if (response.isSuccessful() && response.body() != null) {
                    BaseResponse<tarea> respuesta = response.body();
                    if (respuesta.isSuccess()) {
                        mostrarMensaje("¡Tarea creada exitosamente!");
                        Log.d("TAREA_CREAR", "Tarea creada con ID: " + respuesta.getData().getIdTarea());

                        // Devolver resultado exitoso
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("TAREA_CREADA", true);
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    } else {
                        mostrarMensaje("Error: " + respuesta.getMessage());
                        Log.e("TAREA_CREAR", "Error del servidor: " + respuesta.getMessage());
                    }
                } else {
                    mostrarMensaje("Error en la respuesta del servidor");
                    Log.e("TAREA_CREAR", "Respuesta no exitosa: " + response.code());

                    // Intentar leer el error body
                    if (response.errorBody() != null) {
                        try {
                            String errorBody = response.errorBody().string();
                            Log.e("TAREA_CREAR", "Error body: " + errorBody);
                        } catch (Exception e) {
                            Log.e("TAREA_CREAR", "Error al leer error body", e);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<tarea>> call, Throwable t) {
                btnGuardar.setEnabled(true);
                btnGuardar.setText("Guardar Tarea");

                mostrarMensaje("Error de conexión: " + t.getMessage());
                Log.e("TAREA_CREAR", "Error de red: " + t.toString());
                t.printStackTrace();
            }
        });
    }

    private void mostrarMensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
        Log.d("AGREGAR_TAREA", mensaje);
    }
}