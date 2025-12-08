package com.sise.tareasya.presentacion.pantallaPrincipal;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sise.tareasya.data.api.CategoriaApi;
import com.sise.tareasya.data.api.RetrofitClient;
import com.sise.tareasya.data.common.BaseResponse;
import com.sise.tareasya.data.model.categoria;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrincipalViewModel extends ViewModel {

    private final CategoriaApi categoriaApi;
    private final MutableLiveData<BaseResponse<List<categoria>>> categoriasLiveData = new MutableLiveData<>();

    public PrincipalViewModel() {
        categoriaApi = RetrofitClient.getCategoriaApi();
    }

    public LiveData<BaseResponse<List<categoria>>> getCategoriasLiveData() {
        return categoriasLiveData;
    }

    public void cargarCategorias(int idUsuario) {
        categoriaApi.obtenerCategoriasPorUsuario(idUsuario).enqueue(new Callback<BaseResponse<List<categoria>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<categoria>>> call, Response<BaseResponse<List<categoria>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categoriasLiveData.postValue(response.body());
                } else {
                    // USANDO EL MÉTODO ESTÁTICO error()
                    categoriasLiveData.postValue(BaseResponse.error("Error en la respuesta"));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<categoria>>> call, Throwable t) {
                // USANDO EL MÉTODO ESTÁTICO error()
                categoriasLiveData.postValue(BaseResponse.error(t.getMessage()));
            }
        });
    }
}