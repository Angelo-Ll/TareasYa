// app/src/main/java/com/sise/tareasya/data/repository/TareaRepository.java
package com.sise.tareasya.data.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.sise.tareasya.data.api.TareaApi;
import com.sise.tareasya.data.api.RetrofitClient;
import com.sise.tareasya.data.common.BaseResponse;
import com.sise.tareasya.data.model.tarea;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TareaRepository {

    private final TareaApi tareaApi;
    private final String TAG = TareaRepository.class.getSimpleName();

    public TareaRepository() {
        tareaApi = RetrofitClient.getTareaApi();
    }

    public LiveData<BaseResponse<List<tarea>>> obtenerTareasPorUsuario(int idUsuario) {
        Log.i(TAG, "Iniciando petición obtenerTareasPorUsuario para usuario: " + idUsuario);

        MutableLiveData<BaseResponse<List<tarea>>> data = new MutableLiveData<>();

        tareaApi.getTareasPorUsuario(idUsuario).enqueue(new Callback<BaseResponse<List<tarea>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<tarea>>> call, Response<BaseResponse<List<tarea>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.i(TAG, "Tareas obtenidas exitosamente");
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
            public void onFailure(Call<BaseResponse<List<tarea>>> call, Throwable throwable) {
                Log.e(TAG, "Error de conexión: " + throwable.toString());
                data.setValue(BaseResponse.error("Fallo de conexión"));
            }
        });

        return data;
    }

    public LiveData<BaseResponse<List<tarea>>> obtenerTodasLasTareas() {
        Log.i(TAG, "Iniciando petición obtenerTodasLasTareas");

        MutableLiveData<BaseResponse<List<tarea>>> data = new MutableLiveData<>();

        tareaApi.getAllTareas().enqueue(new Callback<BaseResponse<List<tarea>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<tarea>>> call, Response<BaseResponse<List<tarea>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.i(TAG, "Todas las tareas obtenidas exitosamente");
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
            public void onFailure(Call<BaseResponse<List<tarea>>> call, Throwable throwable) {
                Log.e(TAG, "Error de conexión: " + throwable.toString());
                data.setValue(BaseResponse.error("Fallo de conexión"));
            }
        });

        return data;
    }

    // Método para obtener una tarea específica
    public LiveData<BaseResponse<tarea>> obtenerTareaPorId(int idTarea) {
        Log.i(TAG, "Iniciando petición obtenerTareaPorId: " + idTarea);

        MutableLiveData<BaseResponse<tarea>> data = new MutableLiveData<>();

        tareaApi.getTareaById(idTarea).enqueue(new Callback<BaseResponse<tarea>>() {
            @Override
            public void onResponse(Call<BaseResponse<tarea>> call, Response<BaseResponse<tarea>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.i(TAG, "Tarea obtenida exitosamente");
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
            public void onFailure(Call<BaseResponse<tarea>> call, Throwable throwable) {
                Log.e(TAG, "Error de conexión: " + throwable.toString());
                data.setValue(BaseResponse.error("Fallo de conexión"));
            }
        });

        return data;
    }
}