package com.sise.tareasya.data.api;

import com.sise.tareasya.data.common.BaseResponse;
import com.sise.tareasya.data.model.Usuario;

import com.sise.tareasya.data.request.LoginRequest;
import com.sise.tareasya.data.request.RegistroRequest;


import retrofit2.Call;
import retrofit2.http.Body;

import retrofit2.http.POST;


public interface UsuarioApi {

    @POST("usuarios/verificar")
    Call<BaseResponse<Usuario>> login(@Body LoginRequest request);

    @POST("usuarios/crear")
    Call<BaseResponse<Usuario>> registrar(@Body RegistroRequest request);

}