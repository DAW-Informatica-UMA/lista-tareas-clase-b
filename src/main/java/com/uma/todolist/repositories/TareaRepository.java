package com.uma.todolist.repositories;

import com.uma.todolist.models.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Long> {
    // Spring genera: SELECT * FROM Tarea WHERE LOWER(titulo) LIKE LOWER('%' + :titulo + '%')
    List<Tarea> findByTituloContainingIgnoreCase(String titulo);
}
