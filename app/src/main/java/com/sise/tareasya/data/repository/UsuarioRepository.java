package com.sise.tareasya.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.sise.tareasya.data.api.RetrofitClient;
import com.sise.tareasya.data.api.UsuarioApi;
import com.sise.tareasya.data.common.BaseResponse;
import com.sise.tareasya.data.model.Usuario;
import com.sise.tareasya.data.request.LoginRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsuarioRepository {

    // Tu ApiService (Retrofit interface)
    private UsuarioApi usuarioApi;

    public UsuarioRepository() {
        // Inicializar Retrofit aquí
        usuarioApi = RetrofitClient.getClient().create(UsuarioApi.class);
    }

    public LiveData<BaseResponse<Usuario>> login(LoginRequest request) {
        MutableLiveData<BaseResponse<Usuario>> liveData = new MutableLiveData<>();

        // Llamada REAL a tu API
        Call<BaseResponse<Usuario>> call = usuarioApi.login(request);

        call.enqueue(new Callback<BaseResponse<Usuario>>() {
            @Override
            public void onResponse(Call<BaseResponse<Usuario>> call, Response<BaseResponse<Usuario>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.setValue(response.body());
                } else {
                    BaseResponse<Usuario> errorResponse = new BaseResponse<>();
                    errorResponse.setSuccess(false);
                    errorResponse.setMessage("Error en el servidor");
                    liveData.setValue(errorResponse);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Usuario>> call, Throwable t) {
                BaseResponse<Usuario> errorResponse = new BaseResponse<>();
                errorResponse.setSuccess(false);
                errorResponse.setMessage("Error de conexión: " + t.getMessage());
                liveData.setValue(errorResponse);
            }
        });

        return liveData;
    }
}