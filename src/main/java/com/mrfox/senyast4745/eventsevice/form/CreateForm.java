package com.mrfox.senyast4745.eventsevice.form;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateForm {


    private Long creatorId;
    private String eventName;
    private String eventDescription;
    private Long[] adminIds;
    private Long[] userIds;
    private String[] tags;
    private boolean isOpen;

    @JsonCreator
    public CreateForm(@JsonProperty("creatorId") Long creatorId, @JsonProperty("eventName") String courseName,
                      @JsonProperty("eventDescription") String courseDescription,
                      @JsonProperty("adminIds") Long[] adminIds, @JsonProperty("userIds") Long[] userIds,
                      @JsonProperty("tags") String[] tags, @JsonProperty("isOpen") boolean isOpen) {
        this.creatorId = creatorId;
        this.eventName = courseName;
        this.eventDescription = courseDescription;
        this.adminIds = adminIds;
        this.userIds = userIds;
        this.tags = tags;
        this.isOpen = isOpen;
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

    public Long[] getAdminIds() {
        return adminIds;
    }

    public void setAdminIds(Long[] adminIds) {
        this.adminIds = adminIds;
    }

    public Long[] getUserIds() {
        return userIds;
    }

    public void setUserIds(Long[] userIds) {
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
}
