package com.sise.tareasya.presentacion;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.sise.tareasya.R;
import com.sise.tareasya.presentacion.inicioLogin.InicioActivity;

// Pantalla inicial que muestra logo y redirige al login
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // onStart: Espera 2 segundos y redirige a InicioActivity
    @Override
    protected void onStart() {
        super.onStart();
        //settimeoout equivalente
        new Handler().postDelayed(()-> {
            Intent i = new Intent(this, InicioActivity.class);
            startActivity(i);
            this.finish();
        }, 2000);
    }


}