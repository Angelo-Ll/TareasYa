// app/src/main/java/com/sise/tareasya/data/repository/CategoriaRepository.java
package com.sise.tareasya.data.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.sise.tareasya.data.api.CategoriaApi;
import com.sise.tareasya.data.api.RetrofitClient;
import com.sise.tareasya.data.common.BaseResponse;
import com.sise.tareasya.data.model.categoria;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoriaRepository {

    private final CategoriaApi categoriaApi;
    private final String TAG = CategoriaRepository.class.getSimpleName();

    public CategoriaRepository() {
        categoriaApi = RetrofitClient.getCategoriaApi();
    }

    public LiveData<BaseResponse<List<categoria>>> obtenerCategoriasPorUsuario(int idUsuario) {
        Log.i(TAG, "Iniciando petición obtenerCategoriasPorUsuario para usuario: " + idUsuario);

        MutableLiveData<BaseResponse<List<categoria>>> data = new MutableLiveData<>();

        categoriaApi.obtenerCategoriasPorUsuario(idUsuario).enqueue(new Callback<BaseResponse<List<categoria>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<categoria>>> call, Response<BaseResponse<List<categoria>>> response) {
                // Respuesta satisfactoria (código HTTP 200 o similar)
                if (response.isSuccessful() && response.body() != null) {
                    Log.i(TAG, "Categorías obtenidas exitosamente");
                    data.setValue(response.body());
                }

                // Cuando la respuesta es error (código HTTP 400 o 500)
                if (response.errorBody() != null) {
                    try {
                        String errorJson = response.errorBody().string();
                        Log.e(TAG, "Error JSON: " + errorJson);
                    } catch (Exception e) {
                        Log.e(TAG, "Error al convertir respuesta error api: " + e.getMessage());
                    }
                    data.setValue(BaseResponse.error("El API devolvió un error"));
                }
            }

            // Cuando no hay conexión
            @Override
            public void onFailure(Call<BaseResponse<List<categoria>>> call, Throwable throwable) {
                Log.e(TAG, "Error de conexión: " + throwable.toString());
                data.setValue(BaseResponse.error("Fallo de conexión"));
            }
        });

        return data;
    }

    // Método adicional para obtener todas las categorías
    public LiveData<BaseResponse<List<categoria>>> obtenerTodasLasCategorias() {
        Log.i(TAG, "Iniciando petición obtenerTodasLasCategorias");

        MutableLiveData<BaseResponse<List<categoria>>> data = new MutableLiveData<>();

        categoriaApi.obtenerTodasLasCategorias().enqueue(new Callback<BaseResponse<List<categoria>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<categoria>>> call, Response<BaseResponse<List<categoria>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.i(TAG, "Todas las categorías obtenidas exitosamente");
                    data.setValue(response.body());
                }

                if (response.errorBody() != null) {
                    try {
                        String errorJson = response.errorBody().string();
                        Log.e(TAG, "Error JSON: " + errorJson);
                    } catch (Exception e) {
                        Log.e(TAG, "Error al convertir respuesta error api: " + e.getMessage());
                    }
                    data.setValue(BaseResponse.error("El API devolvió un error"));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<categoria>>> call, Throwable throwable) {
                Log.e(TAG, "Error de conexión: " + throwable.toString());
                data.setValue(BaseResponse.error("Fallo de conexión"));
            }
        });

        return data;
    }
}