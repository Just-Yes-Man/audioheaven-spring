package com.audioheaven.audioheaven.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.audioheaven.audioheaven.models.Cancion;

@Repository
public interface CancionRepository extends JpaRepository<Cancion, Long> {
    List<Cancion> findByNombreContainingIgnoreCaseOrAutorContainingIgnoreCase(String nombre, String autor);
}