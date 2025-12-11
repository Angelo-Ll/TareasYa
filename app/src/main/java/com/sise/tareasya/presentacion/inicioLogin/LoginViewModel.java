package com.sise.tareasya.presentacion.inicioLogin;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.sise.tareasya.data.common.BaseResponse;
import com.sise.tareasya.data.model.Usuario;
import com.sise.tareasya.data.repository.UsuarioRepository;
import com.sise.tareasya.data.request.LoginRequest;


// Gestiona la lógica de autenticación separada de la UI
public class LoginViewModel extends ViewModel {

    private final UsuarioRepository usuarioRepository;

    public LoginViewModel() {
        usuarioRepository = new UsuarioRepository();
    }
    // login: Envía petición de login al repositorio
    public LiveData<BaseResponse<Usuario>> login(String email, String password) {
        LoginRequest request = new LoginRequest(email, password);
        return usuarioRepository.login(request);
    }
}