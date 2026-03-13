package com.uma.todolist.services;

import com.uma.todolist.dtos.TareaDTO;
import com.uma.todolist.models.Tarea;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TareaService {
    private List<Tarea> listaDeTareas = new ArrayList<>();
    private Long idCounter = 1L;

    public TareaDTO crear (String titulo) {
        Tarea nueva = new Tarea(idCounter++, titulo,
                                false,
                                "ALTA");
        listaDeTareas.add(nueva);

        return new TareaDTO(nueva.getId(),
                            nueva.getTitulo(),
                            nueva.isCompletada());
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

    public TareaDTO obtener(Long id) {
        return listaDeTareas.stream()
                .filter(t -> t.getId().equals(id))
                .map(t -> new TareaDTO(t.getId(),
                                            t.getTitulo(),
                                            t.isCompletada()))
                .findFirst()
                .orElse(null);
    }

    public void eliminar(Long id) {
        listaDeTareas.removeIf(t -> t.getId().equals(id));
    }

    public TareaDTO completar(Long id) {
        return listaDeTareas.stream()
                .filter(t->t.getId().equals(id))
                .findFirst()
                .map(t -> {
                    t.setCompletada(true);
                    return new TareaDTO(t.getId(),
                                        t.getTitulo(),
                                        t.isCompletada());
                })
                .orElse(null);
    }
}
