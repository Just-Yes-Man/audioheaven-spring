package com.audioheaven.audioheaven.controllers;

import com.audioheaven.audioheaven.models.Cancion;
import com.audioheaven.audioheaven.models.Comentario;
import com.audioheaven.audioheaven.models.User;
import com.audioheaven.audioheaven.payload.request.ComentarioRequest;
import com.audioheaven.audioheaven.payload.response.ComentarioResponse;
import com.audioheaven.audioheaven.repository.CancionRepository;
import com.audioheaven.audioheaven.repository.ComentarioRepository;
import com.audioheaven.audioheaven.repository.UserRepository;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/comentarios")
public class ComentarioController {

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private CancionRepository cancionRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/all")
    public Page<Comentario> getAllComentarios(Pageable pageable) {
        return comentarioRepository.findAll(pageable);
    }

    @GetMapping("/get/{cancionId}")
    public ResponseEntity<List<ComentarioResponse>> getComentariosPorCancion(@PathVariable Long cancionId) {
        List<Comentario> comentarios = comentarioRepository.findByCancionId(cancionId);

        List<ComentarioResponse> respuesta = comentarios.stream().map(c -> new ComentarioResponse(
                c.getId(),
                c.getContenido(),
                c.getUsuario().getUsername() // O el campo que uses como nombre
        )).toList();

        return ResponseEntity.ok(respuesta);
    }

    @PostMapping("/create")
    public Comentario createComentario(@Valid @RequestBody ComentarioRequest comentarioRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = getValidUser(username);
        Cancion cancion = getValidCancion(comentarioRequest.getCancionId());

        Comentario comentario = new Comentario();
        comentario.setContenido(comentarioRequest.getContenido());
        comentario.setCancion(cancion);
        comentario.setUsuario(user);

        return comentarioRepository.save(comentario);
    }

    private User getValidUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private Cancion getValidCancion(Long cancionId) {
        return cancionRepository.findById(cancionId)
                .orElseThrow(() -> new RuntimeException("Canción no encontrada"));
    }
}