package com.mrfox.senyast4745.eventsevice.model;

import com.mrfox.senyast4745.eventsevice.converters.ListLongConverter;
import com.mrfox.senyast4745.eventsevice.converters.SimpleStringConverter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;

@Entity
@Table(name = "events")
public class EventModel {

    @Id
    @GeneratedValue
    @Column(name = "event_id", nullable = false, unique = true)
    private Long id;

    @Column(name = "creator_id", nullable = false)
    private Long creatorId;

    @Column(name = "event_name", nullable = false, unique = true)
    private String eventName;

    @Column(name = "event_description", nullable = false, unique = true)
    private String eventDescription;

    @Column(name = "admin_ids")
    @Convert(converter = ListLongConverter.class)
    private ArrayList<Long> adminIds;

    @Column(name = "user_ids")
    @Convert(converter = ListLongConverter.class)
    private ArrayList<Long> userIds;

    @Column(name = "tags", nullable = false)
    @Convert(converter = SimpleStringConverter.class)
    private String[] tags;

    @Column(name = "is_open", nullable = false)
    private boolean isOpen;

    @Column(name = "creation_date")
    @Temporal(TemporalType.DATE)
    private Date date;

    @Column(name = "subs_count")
    private int subsCount;

    public EventModel() {
        super();
    }

    public EventModel(Long creatorId, String eventName, String eventDescription, ArrayList<Long> adminIds, ArrayList<Long> userIds, String[] tags, boolean isOpen, Date date, int subsCount) {
        this.creatorId = creatorId;
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.adminIds = adminIds;
        this.userIds = userIds;
        this.tags = tags;
        this.isOpen = isOpen;
        this.date = date;
        this.subsCount = subsCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public ArrayList<Long> getAdminIds() {
        return adminIds;
    }

    public void setAdminIds(ArrayList<Long> adminIds) {
        this.adminIds = adminIds;
    }

    public ArrayList<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(ArrayList<Long> userIds) {
        this.userIds = userIds;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getSubsCount() {
        return subsCount;
    }

    public void setSubsCount(int subsCount) {
        this.subsCount = subsCount;
    }
}
