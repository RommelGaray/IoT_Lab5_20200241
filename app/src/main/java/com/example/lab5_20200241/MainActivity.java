package com.example.lab5_20200241;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lab5_20200241.Entity.Alumno;
import com.example.lab5_20200241.databinding.ActivityMainBinding;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /** INGRESAR A LA APP **/
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codigo = binding.codigo.getText().toString().trim();
                if (codigo.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Por favor, ingrese un código", Toast.LENGTH_SHORT).show();
                    return;
                }

                Alumno alumno;
                try {
                    alumno = cargarAlumno(codigo);
                } catch (IOException | ClassNotFoundException e) {
                    // Si no se puede cargar el alumno,e se asum que es un nuevo alumno
                    alumno = new Alumno(codigo);
                }

                Intent intent = new Intent(MainActivity.this, Tareas.class);
                intent.putExtra("alumno", alumno);
                intent.putExtra("codigo", codigo);
                startActivity(intent);
            }
        });

    }


    public void guardarAlumno(Alumno alumno) throws IOException {
        String fileName = alumno.getCodigo() + ".dat";
        try (FileOutputStream fileOutputStream = this.openFileOutput(fileName, Context.MODE_PRIVATE);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(alumno);
        }
    }


    public Alumno cargarAlumno(String codigo) throws IOException, ClassNotFoundException {
        String fileName = codigo + ".dat";
        try (FileInputStream fileInputStream = this.openFileInput(fileName);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            return (Alumno) objectInputStream.readObject();
        }
    }



}