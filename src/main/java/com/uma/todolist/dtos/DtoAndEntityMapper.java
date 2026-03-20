package com.uma.todolist.dtos;

import com.uma.todolist.models.Tarea;

public class DtoAndEntityMapper {
    public static TareaDTO toDto(Tarea tarea) {
        if (tarea == null) {
            return null;
        }
        return new TareaDTO(
                tarea.getId(),
                tarea.getTitulo(),
                tarea.isCompletada()
        );
    }

    public static Tarea toEntity(TareaDTO tareaDTO) {
        if (tareaDTO == null) {
            return null;
        }

        Tarea tarea = new Tarea(tareaDTO.getId(),
                                   tareaDTO.getTitulo(),
                                   tareaDTO.isCompletada(),
                                   "ALTA");

        return tarea;
    }
}
