package com.example.lab5_20200241.Entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Tarea implements Serializable {

    private String titulo;
    private String descripcion;
    private Date fechaVencimiento;
    private List<Recordatorio> recordatorios;

    public Tarea(String titulo, String descripcion, Date fechaVencimiento) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaVencimiento = fechaVencimiento;
        this.recordatorios = new ArrayList<>();
    }

    public void agregarRecordatorio(Date fechaRecordatorio) {
        this.recordatorios.add(new Recordatorio(fechaRecordatorio));
    }

    public static class Recordatorio {
        private Date fechaRecordatorio;

        public Recordatorio(Date fechaRecordatorio) {
            this.fechaRecordatorio = fechaRecordatorio;
        }

        // Getters y Setters

        public Date getFechaRecordatorio() {
            return fechaRecordatorio;
        }

        public void setFechaRecordatorio(Date fechaRecordatorio) {
            this.fechaRecordatorio = fechaRecordatorio;
        }
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public List<Recordatorio> getRecordatorios() {
        return recordatorios;
    }

    public void setRecordatorios(List<Recordatorio> recordatorios) {
        this.recordatorios = recordatorios;
    }



}
