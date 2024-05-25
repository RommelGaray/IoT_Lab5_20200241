package com.example.lab5_20200241.Entity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab5_20200241.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TareasAdapter extends RecyclerView.Adapter<TareasAdapter.TareaViewHolder> {

    private List<Tarea> tareas;

    public TareasAdapter(List<Tarea> tareas) {
        this.tareas = tareas;
    }

    @NonNull
    @Override
    public TareaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tarea, parent, false);
        return new TareaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TareaViewHolder holder, int position) {
        Tarea tarea = tareas.get(position);
        holder.tituloTextView.setText(tarea.getTitulo());
        holder.descripcionTextView.setText(tarea.getDescripcion());
        holder.fechaVencimientoTextView.setText(formatoFecha(tarea.getFechaVencimiento()));
    }
    private String formatoFecha(Date fecha) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd - HH:mm");
        String fechaFormateada = sdf.format(fecha);
        Log.d("msg-datos", fechaFormateada);
        return fechaFormateada;
    }

    @Override
    public int getItemCount() {
        return tareas.size();
    }

    class TareaViewHolder extends RecyclerView.ViewHolder {
        TextView tituloTextView, descripcionTextView, fechaVencimientoTextView;

        public TareaViewHolder(@NonNull View itemView) {
            super(itemView);
            tituloTextView = itemView.findViewById(R.id.titulo);
            descripcionTextView = itemView.findViewById(R.id.descripcion);
            fechaVencimientoTextView = itemView.findViewById(R.id.fechaVencimiento);
        }
    }


}
