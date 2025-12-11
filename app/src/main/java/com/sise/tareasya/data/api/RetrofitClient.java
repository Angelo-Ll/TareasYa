package com.sise.tareasya.data.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor; // ¡AGREGA ESTO!
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.concurrent.TimeUnit; // ¡AGREGA ESTO!

public class RetrofitClient {

    private static final String BASE_URL = "http://192.168.0.106:3000/api/v1/";
    //192.168.1.5
    //192.168.0.106 will

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            // ¡AGREGA ESTE INTERCEPTOR PARA VER LAS PETICIONES!
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor) // ¡IMPORTANTE!
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client) // ¡NO OLVIDES ESTO!
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static UsuarioApi getUsuarioApi() {
        return getClient().create(UsuarioApi.class);
    }

    // NUEVO: Método para obtener CategoriaApi
    public static CategoriaApi getCategoriaApi() {
        return getClient().create(CategoriaApi.class);
    }

    // NUEVO: Método para obtener TareaApi
    public static TareaApi getTareaApi() {
        return getClient().create(TareaApi.class);
    }
}