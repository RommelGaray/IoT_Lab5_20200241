package com.example.lab5_20200241;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.lab5_20200241.Entity.Alumno;
import com.example.lab5_20200241.Entity.TareasAdapter;
import com.example.lab5_20200241.databinding.ActivityMainBinding;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

                Alumno alumno = null;

                String[] alumnosGuardados = fileList();
                boolean alumnoEncontrado = false;

                for(String alumno1: alumnosGuardados){
                    Log.d("msg-datos", alumno1);

                    if (alumno1.equals(codigo)){

                        /** Leer el objeto **/
                        try (FileInputStream fileInputStream = openFileInput(codigo);
                             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {

                            alumno = (Alumno) objectInputStream.readObject();
                            alumnoEncontrado = true; // Marcar que el alumno fue encontrado
                            break;

                        } catch (FileNotFoundException | ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }

                }

                if (!alumnoEncontrado) {
                    alumno = new Alumno(codigo);
                    Log.d("msg-datos", "Nuevo alumno creado");
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