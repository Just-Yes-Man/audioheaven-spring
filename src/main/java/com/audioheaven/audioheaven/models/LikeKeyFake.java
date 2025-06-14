package com.audioheaven.audioheaven.models;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
class LikeKeyFake implements Serializable {

    @Column(name = "reaction_id")
    Long reactionId;

    @Column(name = "user_id")
    Long userId;

    @Column(name = "cancion_id")
    Long cancionId;

    public Long getReactionId() {
        return reactionId;
    }

    public void setReactionId(Long reactionId) {
        this.reactionId = reactionId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCancionId() {
        return cancionId;
    }

    public void setCancionId(Long cancionId) {
        this.cancionId = cancionId;
    }

}