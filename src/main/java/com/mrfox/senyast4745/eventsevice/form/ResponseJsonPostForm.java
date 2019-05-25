package com.mrfox.senyast4745.eventsevice.form;

import com.mrfox.senyast4745.eventsevice.model.PostModel;

public class ResponseJsonPostForm {
    private Iterable<PostModel> posts;

    public ResponseJsonPostForm(Iterable<PostModel> posts) {
        this.posts = posts;
    }

    public Iterable<PostModel> getEvents() {
        return posts;
    }

    public void setEvents(Iterable<PostModel> posts) {
        this.posts = posts;
    }
}
