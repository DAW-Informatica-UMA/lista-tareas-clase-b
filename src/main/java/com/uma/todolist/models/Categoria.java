package com.uma.todolist.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="categorias")
public class Categoria {
    @Id @GeneratedValue
    Long id;

    String nombre;

    @OneToMany (mappedBy = "categoria")
    List<Tarea> tareas = new ArrayList<>();

    public Categoria(){

    }
}
