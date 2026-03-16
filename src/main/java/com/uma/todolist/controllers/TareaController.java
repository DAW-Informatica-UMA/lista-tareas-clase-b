package com.uma.todolist.controllers;

import com.uma.todolist.dtos.TareaDTO;
import com.uma.todolist.services.TareaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tareas")
public class TareaController {
    @Autowired
    private TareaService tareaService;

    // Ejemplo de llamada -> GET http://localhost:8080/api/tareas/
    @GetMapping
    public ResponseEntity<List<TareaDTO>> listaTareas(){
        List<TareaDTO> tareas = tareaService.obtenerTodas();
        // Usamos .ok() para devolver un 200 OK.
        // En listados, aunque esté vacía, la petición ha tenido éxito.
        return ResponseEntity.ok(tareas);
    }

    // Ejemplo de llamada -> GET http://localhost:8080/api/tareas/buscar?titulo=hola
    @GetMapping("/buscar")
    public ResponseEntity<List<TareaDTO>> buscar (@RequestParam(
            name="titulo",
            required=false,
            defaultValue = "") String titulo){
        // 200 OK: El cliente pidió filtrar y le devolvemos el resultado (sea cual sea).
        return ResponseEntity.ok(tareaService.obtener(titulo));
    }

    // Ejemplo de llamada -> GET http://localhost:8080/api/tareas/1
    @GetMapping("/{id}")
    public ResponseEntity<TareaDTO> buscar(@PathVariable Long id) {
        // .map() transforma el Optional en un 200 OK si existe.
        // .orElse() devuelve un 404 Not Found si el ID no está en el sistema.
        return tareaService.obtener(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Ejemplo de llamada -> POST http://localhost:8080/api/tareas pasando JSON de DTO n body
    @PostMapping
    public ResponseEntity<TareaDTO> añadir (@RequestBody TareaDTO tarea) {
        return tareaService.crear(tarea.getTitulo())
                .map(t -> ResponseEntity.status(HttpStatus.CREATED).body(t)) // 201 Created: Estándar REST para creación.
                .orElse(ResponseEntity.badRequest().build()); // 400 Bad Request: Si el título era null o vacío.
    }

    // Ejemplo de llamada -> DELETE http://localhost:8080/api/tareas/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrar(@PathVariable Long id) {
        tareaService.eliminar(id);
        // 204 No Content: La acción se realizó con éxito pero no hay nada que devolver en el cuerpo.
        return ResponseEntity.noContent().build();
    }

    // Ejemplo de llamada -> PUT http://localhost:8080/api/tareas/1
    @PutMapping("/{id}")
    public ResponseEntity<TareaDTO> completar(@PathVariable Long id) {
        // Si el servicio logra completar la tarea (existe), devuelve 200 OK con los datos actualizados.
        // Si el ID no existe, devuelve 404 Not Found.
        return tareaService.completar(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
