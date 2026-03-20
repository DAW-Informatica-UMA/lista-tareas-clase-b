package com.uma.todolist.services;

import com.uma.todolist.dtos.DtoAndEntityMapper;
import com.uma.todolist.dtos.TareaDTO;
import com.uma.todolist.models.Tarea;
import com.uma.todolist.repositories.TareaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TareaService {
    @Autowired
    private TareaRepository repositorio;

    public Optional<TareaDTO> crear (String titulo) {
        if (titulo == null || titulo.isBlank()) {
            return Optional.empty();
        }

        Tarea nueva = new Tarea();
        nueva.setTitulo(titulo);
        nueva.setCompletada(false);
        nueva.setPrioridad("ALTA");

        // Devolvemos el DTO envuelto en un Optional
        return Optional.of(DtoAndEntityMapper.toDto(repositorio.save(nueva)));
    }

    public List<TareaDTO> obtenerTodas() {
        return repositorio.findAll().stream()
                                    .map(DtoAndEntityMapper::toDto)
                                    .toList();
    }

    public List<TareaDTO> obtener(String titulo) {
        return repositorio.findByTituloContainingIgnoreCase(titulo)
                          .stream()
                          .map(DtoAndEntityMapper::toDto)
                          .toList();
    }

    public Optional<TareaDTO> obtener(Long id) {
        return repositorio.findById(id).map(DtoAndEntityMapper::toDto);
    }

    public void eliminar(Long id) {
        repositorio.deleteById(id);
    }

    public Optional<TareaDTO> completar(Long id) {
        return repositorio.findById(id)
                .map(tarea -> {
                    tarea.setCompletada(true);
                    return DtoAndEntityMapper.toDto(repositorio.save(tarea));
                });
    }
}
