package com.audioheaven.audioheaven.models;

import jakarta.persistence.*;

@Entity
@Table(name = "cancion_reactions", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "user_id", "cancion_id" }),

})
public class CancionReaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reaction_id")
    Long reactionId;

    public Long getReactionId() {
        return reactionId;
    }

    public void setReactionId(Long reactionId) {
        this.reactionId = reactionId;
    }

    @Column(name = "user_id")
    Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Column(name = "cancion_id")
    Long cancionId;

    public Long getCancion_id() {
        return cancionId;
    }

    public void setCancion_id(Long cancion_id) {
        this.cancionId = cancion_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.userId = user.getId();
        this.user = user;
    }

    @ManyToOne
    @MapsId("cancionId")
    @JoinColumn(name = "cancion_id")
    Cancion cancion;

    public Cancion getCancion() {
        return cancion;
    }

    public void setCancion(Cancion cancion) {
        this.cancionId = cancion.getId();
        this.cancion = cancion;
    }

    @ManyToOne
    @MapsId("reactionId")
    @JoinColumn(name = "reaction_id")
    Reaction reaction;

    public Reaction getReaction() {
        return reaction;
    }

    public void setReaction(Reaction reaction) {
        this.reactionId = reaction.getId();
        this.reaction = reaction;
    }

}