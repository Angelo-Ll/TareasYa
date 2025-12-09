package com.sise.tareasya.presentacion.pantallaPrincipal;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sise.tareasya.data.common.BaseResponse;
import com.sise.tareasya.data.model.categoria;
import com.sise.tareasya.data.model.tarea;
import com.sise.tareasya.data.repository.CategoriaRepository;
import com.sise.tareasya.data.repository.TareaRepository;

import java.util.List;

public class PrincipalViewModel extends ViewModel {

    // Repositories (patrón nuevo)
    private final CategoriaRepository categoriaRepository = new CategoriaRepository();
    private final TareaRepository tareaRepository = new TareaRepository();

    // LiveData para categorías
    private final MutableLiveData<BaseResponse<List<categoria>>> categoriasLiveData = new MutableLiveData<>();

    // LiveData para tareas
    private final MutableLiveData<BaseResponse<List<tarea>>> tareasLiveData = new MutableLiveData<>();

    // MÉTODOS NUEVOS - Para tu Activity actualizada
    public LiveData<BaseResponse<List<tarea>>> obtenerTareasPorUsuario(int idUsuario) {
        tareaRepository.obtenerTareasPorUsuario(idUsuario).observeForever(tareasLiveData::postValue);
        return tareasLiveData;
    }

    public LiveData<BaseResponse<List<categoria>>> obtenerCategoriasPorUsuario(int idUsuario) {
        categoriaRepository.obtenerCategoriasPorUsuario(idUsuario).observeForever(categoriasLiveData::postValue);
        return categoriasLiveData;
    }

    // MÉTODOS ANTIGUOS - Para compatibilidad con tu código anterior
    public LiveData<BaseResponse<List<categoria>>> getCategoriasLiveData() {
        return categoriasLiveData;
    }

    public void cargarCategorias(int idUsuario) {
        categoriaRepository.obtenerCategoriasPorUsuario(idUsuario).observeForever(categoriasLiveData::postValue);
    }
}