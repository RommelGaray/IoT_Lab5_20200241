package com.example.lab5_20200241;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lab5_20200241.Entity.Alumno;
import com.example.lab5_20200241.Entity.Tarea;
import com.example.lab5_20200241.databinding.ActivityFormularioBinding;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Formulario extends AppCompatActivity {

    ActivityFormularioBinding binding;
    private Calendar fechaVencimiento;
    private List<Date> recordatorios;
    private SimpleDateFormat dateFormat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFormularioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        fechaVencimiento = Calendar.getInstance();
        recordatorios = new ArrayList<>();

        Intent intent = getIntent();
        Alumno alumno = (Alumno) intent.getSerializableExtra("alumno");
        String codigo = intent.getStringExtra("codigo");

        /** SELECCIONAR FECHA DE VENCIMIENTO **/
        binding.fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionarFecha(fechaVencimiento, true);
            }
        });

        /** AGREGAR NUEVO RECORDATORIO **/
        binding.newRecor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar recordatorio = Calendar.getInstance();
                seleccionarFecha(recordatorio, false);
            }
        });

        /** CREAR LA TAREA **/
        binding.crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearYAgregarTarea(alumno);
                Intent intent = new Intent(Formulario.this, Tareas.class);
                intent.putExtra("alumno", alumno);
                intent.putExtra("codigo", codigo);
                startActivity(intent);
            }
        });


        /** CANCELAR **/
        binding.cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /** FUNCIÓN PARA SELECCIONAR FECHA **/
    private void seleccionarFecha(final Calendar calendario, boolean esFechaVencimiento) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendario.set(Calendar.YEAR, year);
            calendario.set(Calendar.MONTH, month);
            calendario.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view1, hourOfDay, minute) -> {
                calendario.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendario.set(Calendar.MINUTE, minute);

                if (esFechaVencimiento) {
                    fechaVencimiento = calendario;
                    binding.fecha.setText(calendario.getTime().toString());
                } else {
                    recordatorios.add(calendario.getTime());
                    int n = recordatorios.size();
                    Toast.makeText(this, "Recordatorio añadido" + n, Toast.LENGTH_SHORT).show();

                    /*
                    Date fechaRecordatorio = calendario.getTime();
                    recordatorios.add(fechaRecordatorio);
                    mostrarRecordatorio(fechaRecordatorio);

                     */
                }
            }, calendario.get(Calendar.HOUR_OF_DAY), calendario.get(Calendar.MINUTE), true);
            timePickerDialog.show();
        }, calendario.get(Calendar.YEAR), calendario.get(Calendar.MONTH), calendario.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }


    /** Crear y añadir una nueva tarea al alumno */
    private void crearYAgregarTarea(Alumno alumno) {
        String titulo = binding.titulo.getText().toString();
        String descripcion = binding.descripcion.getText().toString();
        Date fecha = fechaVencimiento.getTime();

        Log.d("msg-datos", titulo);
        Log.d("msg-datos", descripcion);
        Log.d("msg-datos", String.valueOf(fecha));

        Tarea tarea = new Tarea(titulo, descripcion, fecha);
        for (Date recordatorio : recordatorios) {
//            int n = recordatorios.size() + 1;
            tarea.agregarRecordatorio(recordatorio);
//            Toast.makeText(this, "Recordatorio añadido" + n, Toast.LENGTH_SHORT).show();

        }

        // Añadir la nueva tarea al alumno
        alumno.agregarTarea(tarea);

        // Guardar el alumno actualizado en el almacenamiento interno
        try {
            guardarAlumnoEnStorage(alumno);
            Toast.makeText(this, "Tarea creada y guardada", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Error al guardar la tarea", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        finish();
    }



    /** Guardar el objeto Alumno en el almacenamiento interno */
    private void guardarAlumnoEnStorage(Alumno alumno) throws IOException {
        String fileName = alumno.getCodigo();
        try (FileOutputStream fileOutputStream = this.openFileOutput(fileName, Context.MODE_PRIVATE);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(alumno);
        }
    }

}