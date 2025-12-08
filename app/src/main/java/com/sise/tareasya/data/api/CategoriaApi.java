package com.sise.tareasya.data.api;

import com.sise.tareasya.data.common.BaseResponse;
import com.sise.tareasya.data.model.categoria; // Mantenemos "categoria" con c minúscula

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.DELETE;
import retrofit2.http.Path;
import retrofit2.http.Body;
import retrofit2.http.Query;

public interface CategoriaApi {

    // Obtener todas las categorías de un usuario
    @GET("categorias/usuario/{idUsuario}")
    Call<BaseResponse<List<categoria>>> obtenerCategoriasPorUsuario(@Path("idUsuario") int idUsuario);

    // Obtener una categoría específica
    @GET("categorias/{idCategoria}")
    Call<BaseResponse<categoria>> obtenerCategoria(@Path("idCategoria") int idCategoria);

    // Crear nueva categoría
    @POST("categorias")
    Call<BaseResponse<categoria>> crearCategoria(@Body categoria categoria);

    // Actualizar categoría
    @PUT("categorias/{idCategoria}")
    Call<BaseResponse<categoria>> actualizarCategoria(
            @Path("idCategoria") int idCategoria,
            @Body categoria categoria);

    // Eliminar categoría
    @DELETE("categorias/{idCategoria}")
    Call<BaseResponse<Void>> eliminarCategoria(@Path("idCategoria") int idCategoria);

    // Buscar categorías por nombre (opcional)
    @GET("categorias/buscar")
    Call<BaseResponse<List<categoria>>> buscarCategorias(
            @Query("idUsuario") int idUsuario,
            @Query("nombre") String nombre);
}