package com.uma.todolist.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="tareas")
public class Tarea {
    @Id @GeneratedValue
    private Long id;
    private String titulo;
    @Column(name="finalizada")
    private boolean completada;
    private String prioridad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="categoria_id")
    private Categoria categoria;

    @ManyToMany
    List<Persona> personas = new ArrayList();

    public Tarea (Long id, String titulo,
                  boolean completada,
                  String prioridad) {
        this.setId(id);
        this.setTitulo(titulo);
        this.setCompletada(completada);
        this.setPrioridad(prioridad);
    }

    public Tarea() {

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

