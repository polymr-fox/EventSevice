package com.mrfox.senyast4745.eventsevice.controller;

import com.google.gson.Gson;
import com.mrfox.senyast4745.eventsevice.dao.EventDAO;
import com.mrfox.senyast4745.eventsevice.form.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class MainController {


    private final EventDAO eventDAO;

    private Gson gson = new Gson();

    @Autowired
    public MainController(EventDAO eventDAO) {
        this.eventDAO = eventDAO;
    }


    @PreAuthorize("@securityService.hasPermission('Role.ADMIN.name(),Role.TEACHER.name(),Role.STUDENT.name(),Role.MODERATOR.name()')")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity create(@RequestBody CreateForm jsonForm) {
        try {
            return ResponseEntity.ok(eventDAO.createEvent(jsonForm.getCreatorId(), jsonForm.getEventName()
                    , jsonForm.getEventDescription(), jsonForm.getAdminIds(), jsonForm.getUserIds()
                    , jsonForm.getTags(), true));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(gson.toJson(new ExceptionModel(400, "Bad Request",
                    "Bad Request with: " + gson.toJson(jsonForm), "/create")));

        }
    }

    @RequestMapping(value = "/read", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity readAll() {
        try {
            return ResponseEntity.ok(new ResponseJsonEventForm(eventDAO.findAll()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(gson.toJson(new ExceptionModel(400, "Bad Request",
                    "DataBase is empty.", "/read")));

        }
    }

    @RequestMapping(value = "/read/user", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity readByFullName(@RequestBody FullNameForm form) {
        try {
            return ResponseEntity.ok(new ResponseJsonEventForm(eventDAO.findAllByCreatorFullName(form.getFullName())));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(gson.toJson(new ExceptionModel(400, "Bad Request",
                    "Bad Request with: " + gson.toJson(form), "/read/user")));

        }
    }

    @RequestMapping(value = "/read/rating", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity readByRating(@RequestBody RatingForm form) {
        try {
            return ResponseEntity.ok(new ResponseJsonEventForm(eventDAO.findAllByRating(form.getRating())));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(gson.toJson(new ExceptionModel(400, "Bad Request",
                    "Bad Request with: " + gson.toJson(form), "/read/rating")));

        }
    }

    @PreAuthorize("@securityService.hasPermission('Role.ADMIN.name(),Role.TEACHER.name(),Role.STUDENT.name(),Role.MODERATOR.name()')")
    @RequestMapping(value = "/subscribe", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity subscribeUser(@RequestBody SubscribeForm form) {
        try {
            return ResponseEntity.ok(eventDAO.subscribeUser(form.getId(), form.getUserId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(gson.toJson(new ExceptionModel(400, "Bad Request",
                    "Bad Request with: " + gson.toJson(form), "/subscribe")));

        }
    }

    @PreAuthorize("@securityService.hasPermission('Role.ADMIN.name(),Role.TEACHER.name(),Role.STUDENT.name(),Role.MODERATOR.name()')")
    @RequestMapping(value = "/unsubscribe", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity unsubscribeUser(@RequestBody SubscribeForm form) {
        try {
            return ResponseEntity.ok(eventDAO.unsubscribeUser(form.getId(), form.getUserId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(gson.toJson(new ExceptionModel(400, "Bad Request",
                    "Bad Request with: " + gson.toJson(form), "/unsubscribe")));

        }
    }

    @PreAuthorize("@securityService.hasPermission('Role.ADMIN.name(),Role.TEACHER.name(),Role.STUDENT.name(),Role.MODERATOR.name()')")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity updateAll(@RequestBody UpdateAllForm form) {
        try {
            return ResponseEntity.ok(eventDAO.updateEvent(form.getId(), form.getUserId(), form.getName(), form.getDescription(),
                    form.getAdminsId(), form.getUsersId(), form.getTags(), form.getOpen()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(403).body(gson.toJson(new ExceptionModel(403, "Forbidden",
                    "Access denied to update", "/update")));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(gson.toJson(new ExceptionModel(400, "Bad Request",
                    "Bad Request with: " + gson.toJson(form), "/update")));
        }
    }

    @PreAuthorize("@securityService.hasPermission('Role.ADMIN.name(),Role.TEACHER.name(),Role.STUDENT.name(),Role.MODERATOR.name()')")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity deleteById(@RequestBody MinimalForm form) {
        try {
            eventDAO.delete(form.getId(), form.getUserId());
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(403).body(gson.toJson(new ExceptionModel(403, "Forbidden",
                    "Access denied to delete", "/delete")));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(gson.toJson(new ExceptionModel(400, "Bad Request",
                    "Bad Request with: " + gson.toJson(form), "/delete")));
        }
    }


    @PreAuthorize("@securityService.hasPermission('Role.ADMIN.name(),Role.TEACHER.name(),Role.STUDENT.name(),Role.MODERATOR.name()')")
    @RequestMapping(value = "/change", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity changeState(@RequestBody ChangeStateForm form) {
        try {
            return ResponseEntity.ok(eventDAO.changeState(form.getId(), form.getCreatorId(), form.getOpen()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(403).body(gson.toJson(new ExceptionModel(403, "Forbidden",
                    "Access denied to change state", "/change")));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(gson.toJson(new ExceptionModel(400, "Bad Request",
                    "Bad Request with: " + gson.toJson(form), "/change")));
        }
    }

    @RequestMapping(value = "/posts", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity readPosts(@RequestBody IdForm form) {
        try {
            return ResponseEntity.ok(new ResponseJsonPostForm(eventDAO.getPosts(form.getId())));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(gson.toJson(new ExceptionModel(400, "Bad Request",
                    "Bad Request with: " + gson.toJson(form), "/posts")));

        }
    }

    @RequestMapping(value = "/notification", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity sendNotification(@RequestBody RequestNotificationForm form, @RequestHeader(value = "Authorization") String token) {
        try {
            eventDAO.sendNotification(form.getUserModels(), form.getId(), form.getRole(), token);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(gson.toJson(new ExceptionModel(400, "Bad Request",
                    "Bad Request with: " + gson.toJson(form), "/posts")));
        }
    }

}