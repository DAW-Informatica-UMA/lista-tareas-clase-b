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
}
