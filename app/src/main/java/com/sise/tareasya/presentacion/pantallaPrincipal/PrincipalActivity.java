package com.sise.tareasya.presentacion.pantallaPrincipal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.sise.tareasya.R;
import com.sise.tareasya.presentacion.administradorVistas.AgregarTareaActivity;

public class PrincipalActivity extends AppCompatActivity {

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

        // Obtener referencia al botón Agregar
        Button btnAgregar = findViewById(R.id.btnAgregar);

        // Configurar el click listener
        btnAgregar.setOnClickListener(v -> {
            // Crear intent para ir a AgregarTareaActivity
            Intent intent = new Intent(PrincipalActivity.this, AgregarTareaActivity.class);
            startActivity(intent);

            // Opcional: agregar animación de transición
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }
}