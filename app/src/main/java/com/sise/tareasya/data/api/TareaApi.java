// app/src/main/java/com/sise/tareasya/data/api/TareaApi.java
package com.sise.tareasya.data.api;

import com.sise.tareasya.data.common.BaseResponse;
import com.sise.tareasya.data.model.tarea;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TareaApi {

    // Obtener todas las tareas
    @GET("tareas")
    Call<BaseResponse<List<tarea>>> getAllTareas();

    // Obtener tarea por ID
    @GET("tareas/{id}")
    Call<BaseResponse<tarea>> getTareaById(@Path("id") int id);

    // Obtener tareas por usuario
    @GET("tareas/usuario/{idUsuario}")
    Call<BaseResponse<List<tarea>>> getTareasPorUsuario(@Path("idUsuario") int idUsuario);

    // Obtener tareas por categoría
    @GET("tareas/categoria/{idCategoria}")
    Call<BaseResponse<List<tarea>>> getTareasPorCategoria(@Path("idCategoria") int idCategoria);

    // Obtener tareas pendientes
    @GET("tareas/estado/pendientes")
    Call<BaseResponse<List<tarea>>> getTareasPendientes();

    // Obtener tareas completadas
    @GET("tareas/estado/completadas")
    Call<BaseResponse<List<tarea>>> getTareasCompletadas();

    // Obtener tareas por prioridad
    @GET("tareas/prioridad/{prioridad}")
    Call<BaseResponse<List<tarea>>> getTareasPorPrioridad(@Path("prioridad") String prioridad);

    // Obtener tareas vencidas
    @GET("tareas/vencidas")
    Call<BaseResponse<List<tarea>>> getTareasVencidas();

    @POST("tareas")
    Call<BaseResponse<tarea>> crearTarea(@Body tarea tarea);
}