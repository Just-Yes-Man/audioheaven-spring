package com.audioheaven.audioheaven.controllers;

import java.util.Optional;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.audioheaven.audioheaven.models.Cancion;
import com.audioheaven.audioheaven.models.User;
import com.audioheaven.audioheaven.repository.CancionRepository;
import com.audioheaven.audioheaven.repository.UserRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
}