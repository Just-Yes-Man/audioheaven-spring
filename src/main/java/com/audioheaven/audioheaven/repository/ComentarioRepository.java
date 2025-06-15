package com.audioheaven.audioheaven.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.audioheaven.audioheaven.models.Comentario;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
    List<Comentario> findByCancionId(Long cancionId);

    void deleteByCancionId(Long cancionId);

}