package com.sise.tareasya.data.api;

import com.sise.tareasya.data.common.BaseResponse;
import com.sise.tareasya.data.model.categoria;
import com.sise.tareasya.data.request.CrearCategoriaRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

import retrofit2.http.Path;
import retrofit2.http.Body;


public interface CategoriaApi {

    // Obtener todas las categorías de un usuario
    @GET("categorias/usuario/{idUsuario}")
    Call<BaseResponse<List<categoria>>> obtenerCategoriasPorUsuario(@Path("idUsuario") int idUsuario);

    @GET("categorias")
    Call<BaseResponse<List<categoria>>> obtenerTodasLasCategorias();

    @POST("categorias/crear")
    Call<BaseResponse<categoria>> crearCategoria(@Body CrearCategoriaRequest request);
}