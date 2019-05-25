package com.mrfox.senyast4745.eventsevice.form;


import com.mrfox.senyast4745.eventsevice.model.EventModel;

public class ResponseJsonEventForm {

    private Iterable<EventModel> events;

    public ResponseJsonEventForm(Iterable<EventModel> events) {
        this.events = events;
    }

    public Iterable<EventModel> getEvents() {
        return events;
    }

    public void setEvents(Iterable<EventModel> events) {
        this.events = events;
    }
}
