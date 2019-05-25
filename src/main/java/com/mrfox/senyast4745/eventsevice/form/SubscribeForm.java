package com.mrfox.senyast4745.eventsevice.form;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SubscribeForm {
    private Long id;
    private Long userId;

    @JsonCreator
    public SubscribeForm(@JsonProperty("id")Long id, @JsonProperty("userId")Long userId) {
        this.id = id;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
