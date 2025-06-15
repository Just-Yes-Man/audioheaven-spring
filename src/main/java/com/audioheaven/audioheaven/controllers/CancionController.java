package com.audioheaven.audioheaven.controllers;

import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.audioheaven.audioheaven.models.Cancion;
import com.audioheaven.audioheaven.models.User;
import com.audioheaven.audioheaven.repository.CancionReactionRepository;
import com.audioheaven.audioheaven.repository.CancionRepository;
import com.audioheaven.audioheaven.repository.ComentarioRepository;
import com.audioheaven.audioheaven.repository.UserRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/cancion")

public class CancionController {

    @Autowired
    private CancionRepository cancionRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/all")
    public Page<Cancion> getTweet(Pageable pageable) {
        return cancionRepository.findAll(pageable);
    }

    @PostMapping("/create")
    public Cancion createTweet(@Valid @RequestBody Cancion cancion) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        System.out.println("userid : " + userId);

        User user = getValidUser(userId);
        System.out.println("user");

        System.out.println(user);
        Cancion myCancion = new Cancion(cancion.getNombre(), cancion.getAutor(), cancion.getLink());
        myCancion.setPostedBy(user);
        cancionRepository.save(myCancion);

        return myCancion;
    }

    private User getValidUser(String userId) {
        Optional<User> userOpt = userRepository.findByUsername(userId);
        if (!userOpt.isPresent()) {
            throw new RuntimeException("User not found");
        }
        return userOpt.get();
    }

    @DeleteMapping("/borrar-todo")
    public ResponseEntity<String> borrarTodasLasCanciones() {
        cancionRepository.deleteAll();
        return ResponseEntity.ok("Todas las canciones han sido borradas.");
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Cancion>> buscarCanciones(@RequestParam String termino) {
        List<Cancion> resultados = cancionRepository
                .findByNombreContainingIgnoreCaseOrAutorContainingIgnoreCase(termino, termino);
        return ResponseEntity.ok(resultados);
    }

    // Suponiendo que tienes repos para ambos
    @Autowired
    private CancionReactionRepository cancionReactionRepository;

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Transactional
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> borrarCancion(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no encontrado.");
        User user = userOpt.get();

        Optional<Cancion> cancionOpt = cancionRepository.findById(id);
        if (cancionOpt.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Canción no encontrada.");
        Cancion cancion = cancionOpt.get();

        if (!cancion.getPostedBy().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permiso para borrar esta canción.");
        }

        // Primero borramos las relaciones dependientes
        comentarioRepository.deleteByCancionId(cancion.getId());
        cancionReactionRepository.deleteByCancionId(cancion.getId());

        // Luego borramos la canción
        cancionRepository.deleteById(id);

        return ResponseEntity.ok("Canción eliminada con éxito.");
    }

}