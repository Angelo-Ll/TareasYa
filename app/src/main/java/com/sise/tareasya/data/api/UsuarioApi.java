package com.sise.tareasya.data.api;

import com.sise.tareasya.data.common.BaseResponse;
import com.sise.tareasya.data.model.Usuario;

import com.sise.tareasya.data.request.LoginRequest;



import retrofit2.Call;
import retrofit2.http.Body;

import retrofit2.http.POST;


public interface UsuarioApi {

    @POST("usuarios/verificar")
    Call<BaseResponse<Usuario>> login(@Body LoginRequest request);



    // Puedes agregar más endpoints después
    // @POST("usuarios/registro")
    // @GET("tareas")
    // etc.
}