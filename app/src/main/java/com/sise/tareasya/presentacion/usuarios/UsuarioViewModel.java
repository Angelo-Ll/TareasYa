package com.sise.tareasya.presentacion.usuarios;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sise.tareasya.data.common.BaseResponse;
import com.sise.tareasya.data.model.Usuario;
import com.sise.tareasya.data.repository.UsuarioRepository;
import com.sise.tareasya.data.request.LoginRequest;
import com.sise.tareasya.data.request.RegistroRequest;

public class UsuarioViewModel extends ViewModel {

    private final UsuarioRepository usuarioRepository = new UsuarioRepository();

    // LiveData para login
    private final MutableLiveData<BaseResponse<Usuario>> loginLiveData = new MutableLiveData<>();

    // LiveData para registro
    private final MutableLiveData<BaseResponse<Usuario>> registroLiveData = new MutableLiveData<>();

    // Método para login
    public void loginUsuario(LoginRequest request) {
        usuarioRepository.login(request).observeForever(loginLiveData::postValue);
    }

    // Método para registro
    public void registrarUsuario(RegistroRequest request) {
        usuarioRepository.registrar(request).observeForever(registroLiveData::postValue);
    }

    // Getters para LiveData
    public LiveData<BaseResponse<Usuario>> getLoginResponse() {
        return loginLiveData;
    }

    public LiveData<BaseResponse<Usuario>> getRegistroResponse() {
        return registroLiveData;
    }
}