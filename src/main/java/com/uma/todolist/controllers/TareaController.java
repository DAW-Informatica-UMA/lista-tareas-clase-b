package com.uma.todolist.controllers;

import com.uma.todolist.dtos.TareaDTO;
import com.uma.todolist.services.TareaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tareas")
@Tag(name = "Tareas", description = "Operaciones para la gestión de tareas y sus colaboradores")
// Esto hace que aparezca el candado en todos los métodos de esta clase en Swagger
@SecurityRequirement(name = "bearer-key")
// CAPA 1: Solo usuarios autenticados con rol USER o superior pueden entrar al controlador
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class TareaController {
    @Autowired
    private TareaService tareaService;

    // Ejemplo de llamada -> GET http://localhost:8080/api/tareas/
    @Operation(
            summary = "Obtener todas las tareas",
            description = "Devuelve una lista completa de tareas con sus categorías y personas asignadas"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista recuperada con éxito"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<TareaDTO>> listaTareas() {
        List<TareaDTO> tareas = tareaService.obtenerTodas();
        // Usamos .ok() para devolver un 200 OK.
        // En listados, aunque esté vacía, la petición ha tenido éxito.
        return ResponseEntity.ok(tareas);
    }

    // Ejemplo de llamada -> GET http://localhost:8080/api/tareas/buscar?titulo=hola
    @Operation(
            summary = "Buscar tareas por título",
            description = "Permite filtrar las tareas cuyo título contenga la cadena buscada. Si no se envía el parámetro, devuelve todas las tareas."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Búsqueda realizada con éxito (puede devolver una lista vacía si no hay coincidencias)",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TareaDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Parámetros de búsqueda inválidos", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno al procesar la búsqueda", content = @Content)
    })
    @GetMapping("/buscar")
    public ResponseEntity<List<TareaDTO>> buscar(@RequestParam(
            name = "titulo",
            required = false,
            defaultValue = "") String titulo) {
        // 200 OK: El cliente pidió filtrar y le devolvemos el resultado (sea cual sea).
        return ResponseEntity.ok(tareaService.obtener(titulo));
    }

    // Ejemplo de llamada -> GET http://localhost:8080/api/tareas/1
    @Operation(
            summary = "Obtener una tarea por su ID",
            description = "Busca una tarea específica en la base de datos utilizando su identificador único."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Tarea encontrada con éxito",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TareaDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se encontró ninguna tarea con el ID proporcionado",
                    content = @Content // Cuerpo vacío para el 404
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<TareaDTO> buscar(@PathVariable Long id) {
        // .map() transforma el Optional en un 200 OK si existe.
        // .orElse() devuelve un 404 Not Found si el ID no está en el sistema.
        return tareaService.obtener(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Ejemplo de llamada -> POST http://localhost:8080/api/tareas pasando JSON de DTO n body
    @Operation(
            summary = "Crear una nueva tarea",
            description = "Añade una tarea al sistema proporcionando su título. Devuelve el objeto creado con su ID generado."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Tarea creada exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TareaDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Petición inválida (por ejemplo, título vacío o nulo)",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno al intentar guardar la tarea",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autenticado (falta token o es inválido)",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Prohibido (no tienes el rol necesario)",
                    content = @Content
            )
    })
    @PostMapping
    // CAPA 2: Solo los ADMIN pueden crear tareas
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TareaDTO> añadir(@RequestBody TareaDTO tarea) {
        return tareaService.crear(tarea.getTitulo())
                .map(t -> ResponseEntity.status(HttpStatus.CREATED).body(t)) // 201 Created: Estándar REST para creación.
                .orElse(ResponseEntity.badRequest().build()); // 400 Bad Request: Si el título era null o vacío.
    }

    // Ejemplo de llamada -> DELETE http://localhost:8080/api/tareas/1
    @Operation(
            summary = "Eliminar una tarea por ID",
            description = "Elimina permanentemente una tarea de la base de datos. Si el ID no existe, la operación se considera completada (idempotencia)."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Tarea eliminada con éxito (No hay contenido que devolver)",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "ID de tarea inválido",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno al intentar eliminar la tarea",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autenticado (falta token o es inválido)",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Prohibido (no tienes el rol necesario)",
                    content = @Content
            )
    })
    @DeleteMapping("/{id}")
    // CAPA 2: Solo los ADMIN pueden crear tareas
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> borrar(@PathVariable Long id) {
        tareaService.eliminar(id);
        // 204 No Content: La acción se realizó con éxito pero no hay nada que devolver en el cuerpo.
        return ResponseEntity.noContent().build();
    }

    // Ejemplo de llamada -> PUT http://localhost:8080/api/tareas/1
    @Operation(
            summary = "Marcar tarea como completada",
            description = "Cambia el estado de una tarea específica a 'completada' mediante su ID. Si ya estaba completada, no hace cambios pero confirma el éxito."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Tarea actualizada con éxito",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TareaDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se pudo completar porque la tarea no existe",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno al procesar la actualización",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autenticado (falta token o es inválido)",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Prohibido (no tienes el rol necesario)",
                    content = @Content
            )
    })
    @PutMapping("/{id}")
    // CAPA 2: Solo los ADMIN pueden crear tareas
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TareaDTO> completar(@PathVariable Long id) {
        // Si el servicio logra completar la tarea (existe), devuelve 200 OK con los datos actualizados.
        // Si el ID no existe, devuelve 404 Not Found.
        return tareaService.completar(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
