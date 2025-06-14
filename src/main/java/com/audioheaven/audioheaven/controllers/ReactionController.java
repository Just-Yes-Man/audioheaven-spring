package com.audioheaven.audioheaven.controllers;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import com.audioheaven.audioheaven.models.CancionReaction;
import com.audioheaven.audioheaven.models.Reaction;
import com.audioheaven.audioheaven.models.User;
import com.audioheaven.audioheaven.payload.request.CancionReactionRequest;
import com.audioheaven.audioheaven.payload.response.ReactionCountResponse;
import com.audioheaven.audioheaven.repository.CancionReactionRepository;
import com.audioheaven.audioheaven.repository.CancionRepository;
import com.audioheaven.audioheaven.repository.ReactionRepository;
import com.audioheaven.audioheaven.repository.UserRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/reactions")

public class ReactionController {

    @Autowired
    private CancionReactionRepository cancionReactionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CancionRepository cancionRepository;
    @Autowired
    private ReactionRepository reactionRepository;

    @GetMapping("/all")
    public Page<CancionReaction> getReaction(Pageable pageable) {
        return cancionReactionRepository.findAll(pageable);
    }

    @DeleteMapping("/borrar-todo")
    public ResponseEntity<String> borrarTodasLasCancionesYReacciones() {
        // Borra todas las reacciones primero
        cancionReactionRepository.deleteAll();

        // Luego borra todas las canciones
        cancionRepository.deleteAll();

        return ResponseEntity.ok("Todas las canciones y sus reacciones han sido borradas.");
    }

    @GetMapping("/most-voted/{cancionId}")
    public ResponseEntity<String> getMostVotedReaction(@PathVariable Long cancionId) {
        List<Object[]> results = cancionReactionRepository.findMostVotedReactionByCancionId(cancionId);

        if (results.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        String mostVotedReaction = results.get(0)[0].toString(); // EReaction name
        return ResponseEntity.ok(mostVotedReaction);
    }

    @PostMapping("/create")
    public CancionReaction createReaction(@Valid @RequestBody CancionReactionRequest cancionReaction) {
        System.out.println("cancionid : " + cancionReaction.getCancionId());
        System.out.println("reactiontid : " + cancionReaction.getReactionId());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        System.out.println("userid : " + userId);

        User user = getValidUser(userId);
        System.out.println("user");

        System.out.println(user);

        CancionReaction myCancionReaction = new CancionReaction();
        Cancion myCancion = getValidCancion(cancionReaction.getCancionId());
        System.out.println("object cancion : ");
        System.out.println(myCancion);

        Reaction myReaction = getValidReaction(cancionReaction.getReactionId());
        System.out.println("object reaction : ");
        System.out.println(myReaction);

        // myTweetReaction.setUserId(user.getId());
        // myTweetReaction.setTweetId(myTweet.getId());
        // myTweetReaction.setReactionId(myReaction.getId());

        myCancionReaction.setUser(user);
        myCancionReaction.setCancion(myCancion);
        myCancionReaction.setReaction(myReaction);

        System.out.println("cancion reaction : ");
        System.out.println(myCancionReaction.getReactionId());
        System.out.println(myCancionReaction.getCancion_id());

        System.out.println(myCancionReaction.getUserId());

        cancionReactionRepository.save(myCancionReaction);

        return myCancionReaction;
    }

    private User getValidUser(String userId) {
        Optional<User> userOpt = userRepository.findByUsername(userId);
        if (!userOpt.isPresent()) {
            throw new RuntimeException("User not found");
        }
        return userOpt.get();
    }

    private Cancion getValidCancion(Long cancionId) {
        Optional<Cancion> cancionOpt = cancionRepository.findById(cancionId);
        if (!cancionOpt.isPresent()) {
            throw new RuntimeException("Tweet not found");
        }
        return cancionOpt.get();
    }

    private Reaction getValidReaction(Long reactionId) {
        Optional<Reaction> reactionOpt = reactionRepository.findById(reactionId);
        if (!reactionOpt.isPresent()) {
            throw new RuntimeException("Reaction not found");
        }
        return reactionOpt.get();
    }

}