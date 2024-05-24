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
        dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Alumno alumno;

        Intent intent = getIntent();
        alumno = (Alumno) intent.getSerializableExtra("alumno");
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
                crearTarea();
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

    /** MOSTRAR RECORDATORIO EN EL LAYOUT **/
    /*
    private void mostrarRecordatorio(Date fechaRecordatorio) {
        TextView textView = new TextView(this);
        textView.setText(dateFormat.format(fechaRecordatorio));
        textView.setPadding(8, 8, 8, 8);
        binding.recordatoriosLayout.addView(textView);
    }
     */


    /** GUARDAR TAREA EN EL STORAGE **/
    private void crearTarea() {
        String titulo = binding.titulo.getText().toString();
        String descripcion = binding.descripcion.getText().toString();
        Date fecha = fechaVencimiento.getTime();

        Log.d("msg-datos", titulo);
        Log.d("msg-datos", descripcion);
        Log.d("msg-datos", String.valueOf(fecha));

        Tarea tarea = new Tarea(titulo, descripcion, fecha);
        for (Date recordatorio : recordatorios) {
            tarea.agregarRecordatorio(recordatorio);
        }

        Log.d("msg-datos", String.valueOf(recordatorios));


        /** QUIERO QUE LA TAREA SE GUARDE EN EL ALMACENAMIENTO INTERNO **/

        try {
            guardarTarea(tarea);
            Toast.makeText(this, "Tarea creada y guardada", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Error al guardar la tarea", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        finish();
    }

    private void guardarTarea(Tarea tarea) throws IOException {
        // Nombre del archivo a guardar
        String fileNameJson = "tareaObjeto";

        // Se utiliza la clase FileOutputStream para poder almacenar en Android
        try (FileOutputStream fileOutputStream = this.openFileOutput(fileNameJson, Context.MODE_PRIVATE);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            // Con objectOutputStream se realiza la escritura como objeto
            objectOutputStream.writeObject(tarea);
        } catch (IOException e) {
            e.printStackTrace();
            throw e; // Lanzamos la excepción para manejarla en el método que llama
        }
    }

}