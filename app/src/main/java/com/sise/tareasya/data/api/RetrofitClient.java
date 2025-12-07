package com.sise.tareasya.data.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    // Cambia esta URL por la de TU API
    private static final String BASE_URL = "http://192.168.0.106:3000/api/v1/";
    // Ejemplo: "http://192.168.1.50:3000/api/"
    // Ejemplo: "http://10.0.2.2:3000/api/" (para emulador)
    // 192.168.0.106 ip wilmer

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    // Método para obtener la API service
    public static UsuarioApi getUsuarioApi() {
        return getClient().create(UsuarioApi.class);
    }
}