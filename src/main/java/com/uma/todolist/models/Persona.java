package com.uma.todolist.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "personas")
public class Persona {
    @Id
    @GeneratedValue
    Long id;

    private String nombre;
    private String apellidos;

    @ManyToMany(mappedBy = "personas")
    List<Tarea> tareas = new ArrayList();

    public Persona(){}
}
