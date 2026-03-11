package com.uma.todolist.dtos;

public class TareaDTO {
    private Long id;
    private String titulo;
    private boolean completada;

    public TareaDTO (Long id, String titulo,
                  boolean completada) {
        this.setId(id);
        this.setTitulo(titulo);
        this.setCompletada(completada);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public boolean isCompletada() {
        return completada;
    }

    public void setCompletada(boolean completada) {
        this.completada = completada;
    }
}
