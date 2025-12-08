package com.sise.tareasya.presentacion.administradorVistas;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.sise.tareasya.R;

import java.util.Calendar;
import java.util.Locale;

public class AgregarTareaActivity extends AppCompatActivity {

    private EditText etFecha;
    private EditText etHora;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_tarea);

        // Obtener referencias de los EditText
        etFecha = findViewById(R.id.etFecha);
        etHora = findViewById(R.id.etHora);

        Button btnGuardar = findViewById(R.id.btnGuardarTarea);

        // Configurar selectores de fecha y hora
        configurarSelectorFecha();
        configurarSelectorHora();

        // Configurar botón Guardar
        if (btnGuardar != null) {
            btnGuardar.setOnClickListener(v -> {
                // TODO: Implementar lógica para guardar la tarea
                // Por ahora, solo cerrar la actividad
                finish();
            });
        }
    }

    private void configurarSelectorFecha() {
        etFecha.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        // Formato: dd/MM/yyyy
                        String fecha = String.format(Locale.getDefault(),
                                "%02d/%02d/%04d", selectedDay, (selectedMonth + 1), selectedYear);
                        etFecha.setText(fecha);
                    },
                    year, month, day);

            datePickerDialog.setTitle("Seleccionar fecha");

            // Rango de fechas (opcional)
            Calendar minDate = Calendar.getInstance();
            minDate.set(2020, 0, 1); // 01/01/2020
            datePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());

            Calendar maxDate = Calendar.getInstance();
            maxDate.add(Calendar.YEAR, 1); // Máximo 1 año en el futuro
            datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());

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
                        // Formato: HH:mm
                        String hora = String.format(Locale.getDefault(),
                                "%02d:%02d", selectedHour, selectedMinute);
                        etHora.setText(hora);
                    },
                    hour, minute, true); // true = formato 24 horas

            timePickerDialog.setTitle("Seleccionar hora");
            timePickerDialog.show();
        });
    }
}