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

    // Interface para callbacks
    public interface TareaCallback {
        void onSuccess(tarea tarea);
        void onError(String mensaje);
    }

    public interface SimpleCallback {
        void onSuccess();
        void onError(String mensaje);
    }

    public TareaRepository() {
        tareaApi = RetrofitClient.getTareaApi();
    }

    // ================== MÉTODOS DE OBTENCIÓN ==================

    public void obtenerTareaPorId(int idTarea, TareaCallback callback) {
        Log.i(TAG, "Iniciando petición obtenerTareaPorId: " + idTarea);

        tareaApi.getTareaById(idTarea).enqueue(new Callback<BaseResponse<tarea>>() {
            @Override
            public void onResponse(Call<BaseResponse<tarea>> call, Response<BaseResponse<tarea>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Log.i(TAG, "Tarea obtenida exitosamente");
                    callback.onSuccess(response.body().getData());
                } else {
                    String errorMsg = "Error al obtener tarea";
                    if (response.body() != null) {
                        errorMsg = response.body().getMessage();
                    }
                    Log.e(TAG, errorMsg);
                    callback.onError(errorMsg);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<tarea>> call, Throwable throwable) {
                Log.e(TAG, "Error de conexión: " + throwable.toString());
                callback.onError("Fallo de conexión: " + throwable.getMessage());
            }
        });
    }

    public LiveData<BaseResponse<tarea>> obtenerTareaPorIdLiveData(int idTarea) {
        MutableLiveData<BaseResponse<tarea>> liveData = new MutableLiveData<>();

        obtenerTareaPorId(idTarea, new TareaCallback() {
            @Override
            public void onSuccess(tarea tarea) {
                liveData.setValue(BaseResponse.success(tarea));
            }

            @Override
            public void onError(String mensaje) {
                liveData.setValue(BaseResponse.error(mensaje));
            }
        });

        return liveData;
    }

    // ================== MÉTODOS DE ACTUALIZACIÓN/ELIMINACIÓN ==================

    public void actualizarEstadoTarea(int idTarea, boolean completada, SimpleCallback callback) {
        Log.i(TAG, "Actualizando estado de tarea " + idTarea + " a " + completada);

        tareaApi.actualizarEstadoTarea(idTarea, completada).enqueue(new Callback<BaseResponse<Void>>() {
            @Override
            public void onResponse(Call<BaseResponse<Void>> call, Response<BaseResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Log.i(TAG, "Estado actualizado exitosamente en API");
                    callback.onSuccess();
                } else {
                    String errorMsg = "Error al actualizar estado en API";
                    if (response.body() != null) {
                        errorMsg = response.body().getMessage();
                    }
                    Log.e(TAG, errorMsg);
                    callback.onError(errorMsg);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Void>> call, Throwable throwable) {
                Log.e(TAG, "Error de conexión al actualizar estado: " + throwable.getMessage());
                callback.onError("Error de conexión: " + throwable.getMessage());
            }
        });
    }

    public void eliminarTarea(int idTarea, SimpleCallback callback) {
        Log.i(TAG, "Eliminando tarea: " + idTarea);

        tareaApi.eliminarTarea(idTarea).enqueue(new Callback<BaseResponse<Void>>() {
            @Override
            public void onResponse(Call<BaseResponse<Void>> call, Response<BaseResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Log.i(TAG, "Tarea eliminada exitosamente en API");
                    callback.onSuccess();
                } else {
                    String errorMsg = "Error al eliminar tarea en API";
                    if (response.body() != null) {
                        errorMsg = response.body().getMessage();
                    }
                    Log.e(TAG, errorMsg);
                    callback.onError(errorMsg);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Void>> call, Throwable throwable) {
                Log.e(TAG, "Error de conexión al eliminar tarea: " + throwable.getMessage());
                callback.onError("Error de conexión: " + throwable.getMessage());
            }
        });
    }

    // ================== MÉTODOS EXISTENTES (ya funcionando) ==================

    public LiveData<BaseResponse<List<tarea>>> obtenerTareasPorUsuario(int idUsuario) {
        Log.i(TAG, "Iniciando petición obtenerTareasPorUsuario para usuario: " + idUsuario);

        MutableLiveData<BaseResponse<List<tarea>>> data = new MutableLiveData<>();

        tareaApi.getTareasPorUsuario(idUsuario).enqueue(new Callback<BaseResponse<List<tarea>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<tarea>>> call, Response<BaseResponse<List<tarea>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.i(TAG, "Tareas obtenidas exitosamente");
                    data.setValue(response.body());
                } else {
                    String errorMsg = "El API devolvió un error";
                    if (response.errorBody() != null) {
                        try {
                            String errorJson = response.errorBody().string();
                            Log.e(TAG, "Error JSON: " + errorJson);
                            errorMsg = "Error del servidor: " + errorJson;
                        } catch (Exception e) {
                            Log.e(TAG, "Error al convertir respuesta error api: " + e.getMessage());
                        }
                    }
                    data.setValue(BaseResponse.error(errorMsg));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<tarea>>> call, Throwable throwable) {
                Log.e(TAG, "Error de conexión: " + throwable.toString());
                data.setValue(BaseResponse.error("Fallo de conexión: " + throwable.getMessage()));
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
                } else {
                    String errorMsg = "El API devolvió un error";
                    if (response.errorBody() != null) {
                        try {
                            String errorJson = response.errorBody().string();
                            Log.e(TAG, "Error JSON: " + errorJson);
                            errorMsg = "Error del servidor: " + errorJson;
                        } catch (Exception e) {
                            Log.e(TAG, "Error al convertir respuesta error api: " + e.getMessage());
                        }
                    }
                    data.setValue(BaseResponse.error(errorMsg));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<tarea>>> call, Throwable throwable) {
                Log.e(TAG, "Error de conexión: " + throwable.toString());
                data.setValue(BaseResponse.error("Fallo de conexión: " + throwable.getMessage()));
            }
        });

        return data;
    }

    public LiveData<BaseResponse<tarea>> crearTarea(tarea nuevaTarea) {
        Log.i(TAG, "Creando nueva tarea: " + nuevaTarea.getTitulo());

        MutableLiveData<BaseResponse<tarea>> data = new MutableLiveData<>();

        tareaApi.crearTarea(nuevaTarea).enqueue(new Callback<BaseResponse<tarea>>() {
            @Override
            public void onResponse(Call<BaseResponse<tarea>> call, Response<BaseResponse<tarea>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.i(TAG, "Tarea creada exitosamente");
                    data.setValue(response.body());
                } else {
                    String errorMsg = "Error al crear tarea";
                    if (response.errorBody() != null) {
                        try {
                            String errorJson = response.errorBody().string();
                            Log.e(TAG, "Error JSON al crear tarea: " + errorJson);
                            errorMsg = "Error del servidor: " + errorJson;
                        } catch (Exception e) {
                            Log.e(TAG, "Error al convertir respuesta error api: " + e.getMessage());
                        }
                    }
                    data.setValue(BaseResponse.error(errorMsg));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<tarea>> call, Throwable throwable) {
                Log.e(TAG, "Error de conexión al crear tarea: " + throwable.toString());
                data.setValue(BaseResponse.error("Fallo de conexión: " + throwable.getMessage()));
            }
        });

        return data;
    }

    // Método para actualizar tarea completa
    public void actualizarTareaCompleta(int idTarea, tarea tareaActualizada, TareaCallback callback) {
        Log.i(TAG, "Actualizando tarea completa: " + idTarea);

        tareaApi.actualizarTarea(idTarea, tareaActualizada).enqueue(new Callback<BaseResponse<tarea>>() {
            @Override
            public void onResponse(Call<BaseResponse<tarea>> call, Response<BaseResponse<tarea>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Log.i(TAG, "Tarea actualizada exitosamente");
                    callback.onSuccess(response.body().getData());
                } else {
                    String errorMsg = "Error al actualizar tarea";
                    if (response.body() != null) {
                        errorMsg = response.body().getMessage();
                    }
                    Log.e(TAG, errorMsg);
                    callback.onError(errorMsg);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<tarea>> call, Throwable throwable) {
                Log.e(TAG, "Error de conexión al actualizar tarea: " + throwable.getMessage());
                callback.onError("Error de conexión: " + throwable.getMessage());
            }
        });
    }

    // ================== MÉTODOS ADICIONALES ==================

    public LiveData<BaseResponse<List<tarea>>> obtenerTareasPorCategoria(int idCategoria) {
        Log.i(TAG, "Obteniendo tareas por categoría: " + idCategoria);

        MutableLiveData<BaseResponse<List<tarea>>> data = new MutableLiveData<>();

        tareaApi.getTareasPorCategoria(idCategoria).enqueue(new Callback<BaseResponse<List<tarea>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<tarea>>> call, Response<BaseResponse<List<tarea>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.i(TAG, "Tareas por categoría obtenidas exitosamente");
                    data.setValue(response.body());
                } else {
                    String errorMsg = "Error al obtener tareas por categoría";
                    if (response.errorBody() != null) {
                        try {
                            String errorJson = response.errorBody().string();
                            Log.e(TAG, "Error JSON: " + errorJson);
                            errorMsg = "Error del servidor: " + errorJson;
                        } catch (Exception e) {
                            Log.e(TAG, "Error al convertir respuesta error api: " + e.getMessage());
                        }
                    }
                    data.setValue(BaseResponse.error(errorMsg));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<tarea>>> call, Throwable throwable) {
                Log.e(TAG, "Error de conexión: " + throwable.toString());
                data.setValue(BaseResponse.error("Fallo de conexión: " + throwable.getMessage()));
            }
        });

        return data;
    }

    public LiveData<BaseResponse<List<tarea>>> obtenerTareasPendientes() {
        Log.i(TAG, "Obteniendo tareas pendientes");

        MutableLiveData<BaseResponse<List<tarea>>> data = new MutableLiveData<>();

        tareaApi.getTareasPendientes().enqueue(new Callback<BaseResponse<List<tarea>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<tarea>>> call, Response<BaseResponse<List<tarea>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.i(TAG, "Tareas pendientes obtenidas exitosamente");
                    data.setValue(response.body());
                } else {
                    String errorMsg = "Error al obtener tareas pendientes";
                    data.setValue(BaseResponse.error(errorMsg));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<tarea>>> call, Throwable throwable) {
                Log.e(TAG, "Error de conexión: " + throwable.toString());
                data.setValue(BaseResponse.error("Fallo de conexión: " + throwable.getMessage()));
            }
        });

        return data;
    }

    public LiveData<BaseResponse<List<tarea>>> obtenerTareasCompletadas() {
        Log.i(TAG, "Obteniendo tareas completadas");

        MutableLiveData<BaseResponse<List<tarea>>> data = new MutableLiveData<>();

        tareaApi.getTareasCompletadas().enqueue(new Callback<BaseResponse<List<tarea>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<tarea>>> call, Response<BaseResponse<List<tarea>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.i(TAG, "Tareas completadas obtenidas exitosamente");
                    data.setValue(response.body());
                } else {
                    String errorMsg = "Error al obtener tareas completadas";
                    data.setValue(BaseResponse.error(errorMsg));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<tarea>>> call, Throwable throwable) {
                Log.e(TAG, "Error de conexión: " + throwable.toString());
                data.setValue(BaseResponse.error("Fallo de conexión: " + throwable.getMessage()));
            }
        });

        return data;
    }
}