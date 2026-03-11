package com.uma.todolist.models;

public class Tarea {
    private Long id;
    private String titulo;
    private boolean completada;
    private String prioridad;

    public Tarea (Long id, String titulo,
                  boolean completada,
                  String prioridad) {
        this.setId(id);
        this.setTitulo(titulo);
        this.setCompletada(completada);
        this.setPrioridad(prioridad);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public boolean isCompletada() {
        return completada;
    }

    public void setCompletada(boolean completada) {
        this.completada = completada;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }
}

