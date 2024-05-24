package com.example.lab5_20200241.Entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Alumno implements Serializable {

    private String codigo;
    private List<Tarea> tareas;

    public Alumno(String codigo) {
        this.codigo = codigo;
        this.tareas = new ArrayList<>();
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public List<Tarea> getTareas() {
        return tareas;
    }

    public void setTareas(List<Tarea> tareas) {
        this.tareas = tareas;
    }
}
