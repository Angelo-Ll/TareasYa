package com.sise.tareasya.presentacion.detallestareas;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sise.tareasya.data.common.BaseResponse; // Cambia esta importación
import com.sise.tareasya.data.model.tarea;
import com.sise.tareasya.data.repository.TareaRepository;

public class DetalleTareasViewModel extends ViewModel {

    private final TareaRepository tareaRepository;
    private final MutableLiveData<BaseResponse<tarea>> tareaLiveData = new MutableLiveData<>(); // Cambia aquí

    public DetalleTareasViewModel() {
        tareaRepository = new TareaRepository();
    }

    public LiveData<BaseResponse<tarea>> obtenerTareaPorId(int idTarea) { // Cambia el tipo de retorno
        tareaRepository.obtenerTareaPorId(idTarea, new TareaRepository.TareaCallback() {
            @Override
            public void onSuccess(tarea tarea) {
                tareaLiveData.setValue(BaseResponse.success(tarea)); // Cambia aquí
            }

            @Override
            public void onError(String mensaje) {
                tareaLiveData.setValue(BaseResponse.error(mensaje)); // Cambia aquí
            }
        });

        return tareaLiveData;
    }

    public void actualizarEstadoTarea(int idTarea, boolean completada) {
        tareaRepository.actualizarEstadoTarea(idTarea, completada, new TareaRepository.SimpleCallback() {
            @Override
            public void onSuccess() {
                // Estado actualizado
            }

            @Override
            public void onError(String mensaje) {
                // Manejar error
            }
        });
    }

    public void eliminarTarea(int idTarea) {
        tareaRepository.eliminarTarea(idTarea, new TareaRepository.SimpleCallback() {
            @Override
            public void onSuccess() {
                // Tarea eliminada
            }

            @Override
            public void onError(String mensaje) {
                // Manejar error
            }
        });
    }
}