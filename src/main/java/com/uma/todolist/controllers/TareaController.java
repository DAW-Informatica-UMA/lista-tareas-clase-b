package com.uma.todolist.controllers;

import com.uma.todolist.dtos.TareaDTO;
import com.uma.todolist.services.TareaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tareas")
public class TareaController {
    @Autowired
    private TareaService tareaService;

    @GetMapping
    public List<TareaDTO> listaTareas(){
        return tareaService.obtenerTodas();
    }

    @PostMapping
    public TareaDTO añadir (@RequestBody TareaDTO tarea) {
        return tareaService.crear(tarea.getTitulo());
    }
}
