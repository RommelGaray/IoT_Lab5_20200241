package com.example.lab5_20200241;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.lab5_20200241.Entity.Alumno;
import com.example.lab5_20200241.Entity.Tarea;
import com.example.lab5_20200241.Entity.TareasAdapter;
import com.example.lab5_20200241.databinding.ActivityTareasBinding;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Tareas extends AppCompatActivity {

    ActivityTareasBinding binding;
    Alumno alumno;
    TareasAdapter tareasAdapter;
    String canal1 = "importanteDefault";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTareasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        /** OBTENER EL ALUMNO Y CODIGO **/
        Intent intent = getIntent();
        alumno = (Alumno) intent.getSerializableExtra("alumno");
        String codigo = intent.getStringExtra("codigo");


        /** NOTIFICACIÓN **/
        crearCanalesNotificacion();

        lanzarNotificacion();


        /** CONFIGURAR EL RECYCLERVIEW **/
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        tareasAdapter = new TareasAdapter(alumno.getTareas());
        binding.recyclerView.setAdapter(tareasAdapter);



        /** LECTURA DEL OBJETO **/
        String fileName = codigo;
        try (FileInputStream fileInputStream = openFileInput(fileName);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            Alumno alumno1 = (Alumno) objectInputStream.readObject();
            tareasAdapter.notifyDataSetChanged();
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

    /** CREAR NOTIFICACIONES **/
    public void crearCanalesNotificacion() {

        NotificationChannel channel = new NotificationChannel(canal1,
                "Canal notificaciones default",
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("Canal para notificaciones con prioridad default");
        channel.enableVibration(true);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

        pedirPermisos();
    }
    public void pedirPermisos() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ActivityCompat.checkSelfPermission(this, POST_NOTIFICATIONS) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{POST_NOTIFICATIONS}, 101);
        }
    }


    public void lanzarNotificacion () {
        Intent intent = new Intent(this, Tareas.class);
        PendingIntent pendingIntent = PendingIntent. getActivity(this, 0, intent,
                PendingIntent. FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder( this, canal1)
                .setSmallIcon(R.drawable.ic_android)
                .setContentTitle( "Bienvenido " + alumno.getCodigo())
                .setContentText( "Esta es mi primera notificación en Android :D" )
                .setPriority(NotificationCompat. PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel( true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat. from(this);
        if (ActivityCompat. checkSelfPermission(this, POST_NOTIFICATIONS) == PackageManager. PERMISSION_GRANTED)
        {
            notificationManager.notify( 1, builder.build()) ;
        }
    }


    /** DIFERENCIA DE HORAS ENTRE 2 FECHAS **/
    private long calcularDiferenciaHoras(Date fecha1, Date fecha2) {
        long diferenciaMilisegundos = fecha2.getTime() - fecha1.getTime();
        return TimeUnit.MILLISECONDS.toHours(diferenciaMilisegundos);
    }





}