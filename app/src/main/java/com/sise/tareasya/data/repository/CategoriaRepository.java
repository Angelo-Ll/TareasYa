package com.sise.tareasya.data.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.sise.tareasya.data.api.CategoriaApi;
import com.sise.tareasya.data.api.RetrofitClient;
import com.sise.tareasya.data.common.BaseResponse;
import com.sise.tareasya.data.model.categoria;
import com.sise.tareasya.data.request.CrearCategoriaRequest;

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

    // Método para crear categoría
    public LiveData<BaseResponse<categoria>> crearCategoria(categoria categoria) {
        Log.i(TAG, "Iniciando petición crearCategoria: " + categoria.getNombreCat());

        MutableLiveData<BaseResponse<categoria>> data = new MutableLiveData<>();

        // Crear request para enviar al servidor
        CrearCategoriaRequest request = new CrearCategoriaRequest(
                categoria.getIdUsuario(),
                categoria.getNombreCat(),
                categoria.getAuditoria()
        );

        categoriaApi.crearCategoria(request).enqueue(new Callback<BaseResponse<categoria>>() {
            @Override
            public void onResponse(Call<BaseResponse<categoria>> call, Response<BaseResponse<categoria>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.i(TAG, "Categoría creada exitosamente: " + categoria.getNombreCat());
                    data.setValue(response.body());
                } else {
                    // Manejar error
                    if (response.errorBody() != null) {
                        try {
                            String errorJson = response.errorBody().string();
                            Log.e(TAG, "Error JSON al crear categoría: " + errorJson);
                        } catch (Exception e) {
                            Log.e(TAG, "Error al convertir respuesta error api: " + e.getMessage());
                        }
                    }

                    BaseResponse<categoria> errorResponse = new BaseResponse<>();
                    errorResponse.setSuccess(false);

                    // Manejar diferentes códigos de error
                    if (response.code() == 400) {
                        errorResponse.setMessage("Datos inválidos");
                    } else if (response.code() == 401) {
                        errorResponse.setMessage("No autorizado");
                    } else if (response.code() == 409) {
                        errorResponse.setMessage("La categoría ya existe");
                    } else {
                        errorResponse.setMessage("Error del servidor: " + response.code());
                    }

                    data.setValue(errorResponse);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<categoria>> call, Throwable t) {
                Log.e(TAG, "Error de conexión al crear categoría: " + t.toString());

                BaseResponse<categoria> errorResponse = new BaseResponse<>();
                errorResponse.setSuccess(false);
                errorResponse.setMessage("Error de conexión: " + t.getMessage());
                data.setValue(errorResponse);
            }
        });

        return data;
    }
}