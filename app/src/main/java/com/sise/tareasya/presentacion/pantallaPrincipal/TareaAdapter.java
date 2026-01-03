// app/src/main/java/com/sise/tareasya/presentacion/adapters/TareaAdapter.java
package com.sise.tareasya.presentacion.pantallaPrincipal;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sise.tareasya.R;
import com.sise.tareasya.data.model.tarea;
import com.sise.tareasya.presentacion.ColorUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TareaAdapter extends RecyclerView.Adapter<TareaAdapter.TareaViewHolder> {

    private List<tarea> tareas;
    private OnTareaClickListener listener;

    public interface OnTareaClickListener {
        void onTareaClick(tarea tarea);
    }

    public TareaAdapter(List<tarea> tareas, OnTareaClickListener listener) {
        this.tareas = tareas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TareaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tarea_chat, parent, false);
        return new TareaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TareaViewHolder holder, int position) {
        tarea tarea = tareas.get(position);
        holder.bind(tarea);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTareaClick(tarea);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tareas != null ? tareas.size() : 0;
    }

    public void setTareas(List<tarea> nuevasTareas) {
        this.tareas = nuevasTareas;
        notifyDataSetChanged();
    }

    static class TareaViewHolder extends RecyclerView.ViewHolder {

        private TextView tvNombreTarea;
        private TextView tvHora;
        private TextView tvDescripcionBreve;
        private TextView tvCategoria;
        private TextView tvContacto;

        public TareaViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNombreTarea = itemView.findViewById(R.id.tvNombreTarea);
            tvHora = itemView.findViewById(R.id.tvHora);
            tvDescripcionBreve = itemView.findViewById(R.id.tvDescripcionBreve);
            tvCategoria = itemView.findViewById(R.id.tvCategoria);
            tvContacto = itemView.findViewById(R.id.tvContacto);
        }

        public void bind(tarea tarea) {
            // Nombre de la tarea
            tvNombreTarea.setText(tarea.getTitulo());

            // Hora (de recordatorio o fecha creación)
            String hora = obtenerHoraFormateada(tarea);
            tvHora.setText(hora);

            // Descripción (truncada si es muy larga)
            String descripcion = tarea.getDescripcion();
            if (descripcion == null || descripcion.isEmpty()) {
                tvDescripcionBreve.setText("Sin descripción");
            } else {
                // Limitar a 40 caracteres
                String descCorta = descripcion.length() > 40 ?
                        descripcion.substring(0, 40) + "..." : descripcion;
                tvDescripcionBreve.setText(descCorta);
            }

            // Categoría
            if (tarea.getCategoria() != null) {
                String nombreCat = tarea.getCategoria().getNombreCat();
                tvCategoria.setText(nombreCat);

                // USAR LA MISMA LÓGICA QUE PrincipalActivity
                int color = ColorUtils.getColorIntForCategory(nombreCat);
                tvCategoria.setBackgroundColor(color);
                tvCategoria.setTextColor(Color.WHITE);

                // Si necesitas redondeo, usa un GradientDrawable
                GradientDrawable bg = new GradientDrawable();
                bg.setColor(color);
                bg.setCornerRadius(16); // px o dp según necesites
                tvCategoria.setBackground(bg);

            } else {
                tvCategoria.setText("Sin categoría");
                int defaultColor = ColorUtils.getColorIntForCategory("Sin categoría");
                tvCategoria.setBackgroundColor(defaultColor);
                tvCategoria.setTextColor(Color.WHITE);
            }

            // Contacto/Info adicional (puedes personalizar)
            String infoAdicional = obtenerInfoAdicional(tarea);
            tvContacto.setText(infoAdicional);
        }

        private String obtenerHoraFormateada(tarea tarea) {
            try {
                if (tarea.getRecordatorioHora() != null) {
                    // Si tiene hora de recordatorio
                    return formatearHora(tarea.getRecordatorioHora());
                }

                if (tarea.getFechaCreacion() != null) {
                    // Si no, usar fecha de creación
                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                    return sdf.format(new Date());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "Sin hora";
        }

        private String formatearHora(String hora24) {
            try {
                SimpleDateFormat sdf24 = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                SimpleDateFormat sdf12 = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                Date date = sdf24.parse(hora24);
                return sdf12.format(date);
            } catch (Exception e) {
                return hora24; // Devolver tal cual si hay error
            }
        }

        private void cambiarColorCategoria(String nombreCategoria) {
            // Usar el mismo ColorUtils que PrincipalActivity
            int color = ColorUtils.getColorIntForCategory(nombreCategoria);

            GradientDrawable bg = new GradientDrawable();
            bg.setColor(color);
            bg.setCornerRadius(16); // Ajusta según tu diseño

            tvCategoria.setBackground(bg);
            tvCategoria.setTextColor(Color.WHITE);

            // Opcional: ajustar tamaño del texto si es largo
            if (nombreCategoria.length() > 10) {
                tvCategoria.setTextSize(10);
            }
        }

        private String obtenerInfoAdicional(tarea tarea) {
            // Prioridad
            String prioridad = tarea.getPrioridadTexto();

            // Subtareas
            int subtareasCompletadas = tarea.getSubtareasCompletadas();
            int totalSubtareas = tarea.getTotalSubtareas();

            // Fecha límite
            String fechaLimite = tarea.getFechaLimiteFormateada();

            // Combinar información
            StringBuilder info = new StringBuilder();

            if (!"Sin prioridad".equals(prioridad)) {
                info.append(prioridad);
            }

            if (totalSubtareas > 0) {
                if (info.length() > 0) info.append(" • ");
                info.append(subtareasCompletadas).append("/").append(totalSubtareas);
            }

            if (!"Sin fecha".equals(fechaLimite)) {
                if (info.length() > 0) info.append(" • ");
                info.append(fechaLimite);
            }

            return info.length() > 0 ? info.toString() : "Sin detalles";
        }
    }
}