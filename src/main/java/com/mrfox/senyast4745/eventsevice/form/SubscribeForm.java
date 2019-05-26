package com.mrfox.senyast4745.eventsevice.form;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SubscribeForm {
    private Long id;


    @JsonCreator
    public SubscribeForm(@JsonProperty("id")Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
