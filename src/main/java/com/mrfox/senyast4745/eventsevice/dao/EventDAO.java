package com.mrfox.senyast4745.eventsevice.dao;

import com.mrfox.senyast4745.eventsevice.form.NotificationForm;
import com.mrfox.senyast4745.eventsevice.model.*;
import com.mrfox.senyast4745.eventsevice.repository.EventsRepository;
import com.mrfox.senyast4745.eventsevice.repository.PostRepository;
import com.mrfox.senyast4745.eventsevice.repository.TagsRepository;
import com.mrfox.senyast4745.eventsevice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

@Component
public class EventDAO {
    private final TagsRepository tagsRepository;
    private final UserRepository userRepository;
    private final EventsRepository eventsRepository;
    private final PostRepository postRepository;

    private static final int TYPE = 1;

    @Autowired
    public EventDAO(TagsRepository tagsRepository, UserRepository userRepository, EventsRepository eventsRepository, PostRepository postRepository) {
        this.tagsRepository = tagsRepository;
        this.userRepository = userRepository;
        this.eventsRepository = eventsRepository;
        this.postRepository = postRepository;
    }

    public EventModel createEvent(Long creatorId, String eventName, String eventDescription, Long[] adminsId
            , Long[] usersId, String[] tags, boolean isOpen) {
        if (tags.length == 0) {
            throw new IllegalArgumentException("No tags found");
        }
        for (String tmp : tags) {
            Iterable<TagModel> tmpTag = tagsRepository.findAllByTagName(tmp);
            if (!tmpTag.iterator().hasNext()) {
                tagsRepository.save(new TagModel(tmp));
            }
        }
        return eventsRepository.save(new EventModel(creatorId, eventName, eventDescription, new ArrayList<>(Arrays.asList(adminsId)),
                new ArrayList<>(Arrays.asList(usersId)), tags, isOpen, new Date(), usersId.length));
    }

    public EventModel updateEvent(Long id, Long userId, String name, String description, Long[] adminsId
            , Long[] usersId, String[] tags, Boolean isOpen) throws IllegalAccessException {
        checkAccess(id, userId);
        EventModel eventModels = eventsRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Event with id " + id + " not exist."));
        if (name != null && !name.isEmpty()) {
            eventModels.setEventName(name);
        }
        if (description != null && !description.isEmpty()) {
            eventModels.setEventDescription(description);
        }
        if (usersId != null && usersId.length > 0) {
            eventModels.setUserIds(new ArrayList<>(Arrays.asList(usersId)));
            eventModels.setSubsCount(usersId.length);
        }

        if (adminsId != null && adminsId.length > 0) {
            ArrayList<Long> admins = new ArrayList<>();
            for (Long tmpId : adminsId) {
                userRepository.findById(tmpId).ifPresent(m -> {
                    if (m.getRole() == Role.ADMIN) {
                        admins.add(m.getUserId());
                    }
                });
            }
            eventModels.setAdminIds(admins);
        }

        if (tags != null && tags.length > 0) {
            eventModels.setTags(tags);
        }

        if (isOpen != null) {
            eventModels.setOpen(isOpen);
        }

        return eventsRepository.save(eventModels);
    }

    public EventModel subscibeUser(Long id, Long userId) {
        EventModel eventModel = eventsRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Event with id " + id + " not exist."));
        eventModel.getUserIds().add(userId);
        int tmp = eventModel.getSubsCount();
        eventModel.setSubsCount(++tmp);
        return eventsRepository.save(eventModel);
    }

    public EventModel unsubscibeUser(Long id, Long userId) {
        EventModel eventModel = eventsRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Event with id " + id + " not exist."));
        eventModel.getUserIds().remove(userId);
        int tmp = eventModel.getSubsCount();
        eventModel.setSubsCount(--tmp);
        return eventsRepository.save(eventModel);
    }

    public EventModel changeState(Long id, Long userId, Boolean isOpen) throws IllegalAccessException {
        return updateEvent(id, userId, null, null, null, null, null, isOpen);
    }

    private void checkAccess(Long id, Long creatorId) throws IllegalAccessException {
        EventModel tmp = eventsRepository.findById(id).orElse(null);
        UserModel tmpUser = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Incorrect user with user id " + id));
        if (tmp == null || (!tmp.getCreatorId().equals(creatorId) && tmpUser.getRole() != (Role.MODERATOR))) {
            throw new IllegalAccessException();
        }

    }

    public void delete(Long id, Long userId) throws IllegalAccessException {
        checkAccess(id, userId);
        eventsRepository.deleteById(id);
    }

    public Iterable<EventModel> findAll() {
        Iterable<EventModel> eventModels = eventsRepository.findAll();
        if (!eventModels.iterator().hasNext()) {
            throw new IllegalArgumentException("Now events not exist.");
        }
        return eventModels;
    }

    private Iterable<EventModel> findAllByCreatorId(Long creatorId) {
        Iterable<EventModel> eventModels = eventsRepository.findAllByCreatorId(creatorId);
        if (!eventModels.iterator().hasNext()) {
            throw new IllegalArgumentException("User with id " + creatorId + " has not created an article yet.");
        }
        return eventModels;
    }

    public Iterable<EventModel> findAllByCreatorFullName(String fullName) {
        Iterable<UserModel> userModels = userRepository.findByFullName(fullName);
        ArrayList<EventModel> eventModels = new ArrayList<>();
        for (UserModel u : userModels) {
            findAllByCreatorId(u.getUserId()).forEach(eventModels::add);
        }
        if (eventModels.isEmpty()) {
            throw new IllegalArgumentException("Users with full " + fullName + " has not created an article yet.");
        }
        return eventModels;
    }

    public Iterable<EventModel> findAllByRating(int count) {
        Iterable<EventModel> eventModels = eventsRepository.findAllBySubsCount(count);
        if (!eventModels.iterator().hasNext()) {
            throw new IllegalArgumentException("Events with rating " + count + " not exist.");
        }
        return eventModels;
    }

    public EventModel subscribeUser(Long id, Long userId) {
        EventModel eventModel = eventsRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Event with id " + id + " not exist."));
        eventModel.getUserIds().add(userId);
        int tmp = eventModel.getSubsCount();
        eventModel.setSubsCount(++tmp);
        return eventsRepository.save(eventModel);
    }

    public EventModel unsubscribeUser(Long id, Long userId) {
        EventModel eventModel = eventsRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Event with id " + id + " not exist."));
        eventModel.getUserIds().remove(userId);
        int tmp = eventModel.getSubsCount();
        eventModel.setSubsCount(--tmp);
        return eventsRepository.save(eventModel);
    }

    public Iterable<PostModel> getPosts(Long id) {
        Iterable<PostModel> postModels = postRepository.findAllByTypeAndParentIdOrderByDate(TYPE, id);
        if (!postModels.iterator().hasNext()) {
            throw new IllegalArgumentException("Posts with parent id " + id + " not exist.");
        }
        return postModels;
    }

    public void sendNotification(UserModel[] userModels, Long id, Role role, String token) {

        RestTemplate restTemplate = new RestTemplate();

        final String url = "127.0.0.1:9996/" + "notification" + role.name();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        NotificationForm form = new NotificationForm(userModels, TYPE, id);

        HttpEntity<NotificationForm> request = new HttpEntity<>(form, headers);
        ResponseEntity<Void> response = restTemplate.postForEntity(url, request, Void.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new HttpServerErrorException(response.getStatusCode());
        }


    }
}