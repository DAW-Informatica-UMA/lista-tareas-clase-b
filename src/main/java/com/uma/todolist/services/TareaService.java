package com.uma.todolist.services;

import com.uma.todolist.dtos.TareaDTO;
import com.uma.todolist.models.Tarea;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TareaService {
    private List<Tarea> listaDeTareas = new ArrayList<>();
    private Long idCounter = 1L;

    public Optional<TareaDTO> crear (String titulo) {
        if (titulo == null || titulo.isBlank()) {
            return Optional.empty();
        }

        Tarea nueva = new Tarea(idCounter++, titulo, false, "ALTA");
        listaDeTareas.add(nueva);

        // Devolvemos el DTO envuelto en un Optional
        return Optional.of(new TareaDTO(
                nueva.getId(),
                nueva.getTitulo(),
                nueva.isCompletada()
        ));
    }

    public List<TareaDTO> obtenerTodas() {
        return listaDeTareas.stream()
                .map(t -> new TareaDTO(t.getId(),
                                    t.getTitulo(),
                                    t.isCompletada()))
                .toList();
    }

    public List<TareaDTO> obtener(String titulo) {
        return listaDeTareas.stream()
                .filter(t ->
                        t.getTitulo().toLowerCase().contains(titulo.toLowerCase()))
                .map(t -> new TareaDTO(t.getId(), t.getTitulo(),
                                            t.isCompletada()))
                .toList();
    }

    public Optional<TareaDTO> obtener(Long id) {
        return listaDeTareas.stream()
                .filter(t -> t.getId().equals(id))
                .map(t -> new TareaDTO(t.getId(),
                                            t.getTitulo(),
                                            t.isCompletada()))
                .findFirst();
    }

    public void eliminar(Long id) {
        listaDeTareas.removeIf(t -> t.getId().equals(id));
    }

    public Optional<TareaDTO> completar(Long id) {
        return listaDeTareas.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst() // Devuelve Optional<Tarea>
                .map(t -> {  // Si existe la tarea, entra aquí:
                    t.setCompletada(true);
                    return new TareaDTO(t.getId(), t.getTitulo(), t.isCompletada());
                }); // Si no existe, el Optional se mantiene vacío automáticamente
    }
}
