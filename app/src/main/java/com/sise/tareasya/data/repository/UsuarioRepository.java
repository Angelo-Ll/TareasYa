package com.sise.tareasya.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.sise.tareasya.data.api.UsuarioApi;
import com.sise.tareasya.data.api.RetrofitClient;
import com.sise.tareasya.data.common.BaseResponse;
import com.sise.tareasya.data.model.Usuario;
import com.sise.tareasya.data.request.LoginRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsuarioRepository {

    private UsuarioApi usuarioApi;

    public UsuarioRepository() {
        usuarioApi = RetrofitClient.getUsuarioApi();
    }

    // ¡ESTE MÉTODO DEBE ACEPTAR LoginRequest!
    public LiveData<BaseResponse<Usuario>> login(LoginRequest request) {
        MutableLiveData<BaseResponse<Usuario>> liveData = new MutableLiveData<>();

        Call<BaseResponse<Usuario>> call = usuarioApi.login(request);

        call.enqueue(new Callback<BaseResponse<Usuario>>() {
            @Override
            public void onResponse(Call<BaseResponse<Usuario>> call, Response<BaseResponse<Usuario>> response) {
                BaseResponse<Usuario> apiResponse;

                if (response.isSuccessful() && response.body() != null) {
                    apiResponse = response.body();
                } else {
                    apiResponse = new BaseResponse<>();
                    apiResponse.setSuccess(false);

                    if (response.code() == 404) {
                        apiResponse.setMessage("Usuario no encontrado");
                    } else if (response.code() == 401) {
                        apiResponse.setMessage("Credenciales incorrectas");
                    } else {
                        apiResponse.setMessage("Error del servidor: " + response.code());
                    }
                }
                liveData.setValue(apiResponse);
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