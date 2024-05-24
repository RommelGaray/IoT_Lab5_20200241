package com.example.lab5_20200241;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.lab5_20200241.Entity.Alumno;
import com.example.lab5_20200241.Entity.TareasAdapter;
import com.example.lab5_20200241.databinding.ActivityTareasBinding;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Tareas extends AppCompatActivity {

    ActivityTareasBinding binding;
    Alumno alumno;
    TareasAdapter tareasAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTareasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /** OBTENER EL ALUMNO Y CODIGO **/
        Intent intent = getIntent();
        alumno = (Alumno) intent.getSerializableExtra("alumno");
        String codigo = intent.getStringExtra("codigo");


        /** IMPLEMENTAR EL RECYCLE VIEW **/
        tareasAdapter = new TareasAdapter(alumno.getTareas());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(tareasAdapter);


        /** AGREGAR NUEVA TAREA **/
        binding.agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Tareas.this, Formulario.class);
                intent.putExtra("alumno", alumno);
                intent.putExtra("codigo", codigo);
                startActivity(intent);
            }
        });


    }

    public void guardarAlumno(Alumno alumno) throws IOException {
        String fileName = alumno.getCodigo();
        try (FileOutputStream fileOutputStream = this.openFileOutput(fileName, Context.MODE_PRIVATE);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(alumno);
        }
    }
}