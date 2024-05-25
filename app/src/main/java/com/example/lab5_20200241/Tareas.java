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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
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


        // Configurar el RecyclerView
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        tareasAdapter = new TareasAdapter(alumno.getTareas());
        binding.recyclerView.setAdapter(tareasAdapter);



        /** LECTURA DEL OBJETO **/
        String fileName = codigo;
        try (FileInputStream fileInputStream = openFileInput(fileName);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            Alumno alumno1 = (Alumno) objectInputStream.readObject();

//            /** IMPLEMENTAR EL RECYCLE VIEW **/
//            tareasAdapter = new TareasAdapter(alumno1.getTareas());
//            binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
//            binding.recyclerView.setAdapter(tareasAdapter);
            tareasAdapter.notifyDataSetChanged(); // Notificar al adaptador que los datos han cambiado


        } catch (FileNotFoundException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }






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