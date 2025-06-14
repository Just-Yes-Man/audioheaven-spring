package com.audioheaven.audioheaven.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.audioheaven.audioheaven.models.Reaction;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {

}