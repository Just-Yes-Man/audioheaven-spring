package com.audioheaven.audioheaven.payload.request;

import jakarta.validation.constraints.NotBlank;

public class CancionReactionRequest {
    private Long cancionId;

    public Long getCancionId() {
        return cancionId;
    }

    public void setCancionId(Long cancionId) {
        this.cancionId = cancionId;
    }

    //
    private Long reactionId;

    public Long getReactionId() {
        return reactionId;
    }

    public void setReactionId(Long reactionId) {
        this.reactionId = reactionId;
    }

}