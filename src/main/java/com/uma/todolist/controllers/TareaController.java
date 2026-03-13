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

    // http://localhost:8080/api/tareas/buscar?titulo=hola
    @GetMapping("/buscar")
    public List<TareaDTO> buscar (@RequestParam(
            name="titulo",
            required=false,
            defaultValue = "") String titulo){
        return tareaService.obtener(titulo);
    }

    @GetMapping("/{id}")
    public TareaDTO buscar(@PathVariable Long id) {
        return tareaService.obtener(id);
    }

    @PostMapping
    public TareaDTO añadir (@RequestBody TareaDTO tarea) {
        return tareaService.crear(tarea.getTitulo());
    }

    @DeleteMapping("/{id}")
    public void borrar(@PathVariable Long id) {
        tareaService.eliminar(id);
    }

    @PutMapping("/{id}")
    public TareaDTO completar(@PathVariable Long id) {
        return tareaService.completar(id);
    }
}
