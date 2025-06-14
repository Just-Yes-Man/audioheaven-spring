package com.audioheaven.audioheaven.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.audioheaven.audioheaven.models.CancionReaction;
import com.audioheaven.audioheaven.payload.response.ReactionCountResponse;

@Repository
public interface CancionReactionRepository extends JpaRepository<CancionReaction, Long> {
    @Query("SELECT cr.reaction.description, COUNT(cr) as total " +
            "FROM CancionReaction cr " +
            "WHERE cr.cancion.id = :cancionId " +
            "GROUP BY cr.reaction.description " +
            "ORDER BY total DESC")
    List<Object[]> findMostVotedReactionByCancionId(@Param("cancionId") Long cancionId);

    void deleteByCancionId(Long cancionId);
}