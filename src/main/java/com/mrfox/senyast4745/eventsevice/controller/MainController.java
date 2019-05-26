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


    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MODERATOR') or hasAuthority('STUDENT')")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity create(@RequestBody CreateForm form, @RequestHeader(value = "Authorization") String token) {
        try {
            return ResponseEntity.ok(eventDAO.createEvent(token, form.getEventName()
                    , form.getEventDescription(), form.getAdminIds(), form.getUserIds()
                    , form.getTags(), true));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(gson.toJson(new ExceptionModel(400, "Bad Request",
                    "Bad Request with: " + gson.toJson(form), "/create")));

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

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MODERATOR') or hasAuthority('STUDENT')")
    @RequestMapping(value = "/subscribe", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity subscribeUser(@RequestBody SubscribeForm form, @RequestHeader(value = "Authorization") String token) {
        try {
            return ResponseEntity.ok(eventDAO.subscribeUser(form.getId(), token));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(406).body(gson.toJson(new ExceptionModel(406, "Not Acceptable",
                    e.getMessage(), "/subscribe")));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(gson.toJson(new ExceptionModel(400, "Bad Request",
                    "Bad Request with: " + gson.toJson(form), "/subscribe")));

        }
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MODERATOR') or hasAuthority('STUDENT')")
    @RequestMapping(value = "/unsubscribe", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity unsubscribeUser(@RequestBody SubscribeForm form, @RequestHeader(value = "Authorization") String token) {
        try {
            return ResponseEntity.ok(eventDAO.unsubscribeUser(form.getId(), token));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(406).body(gson.toJson(new ExceptionModel(406, "Not Acceptable",
                    e.getMessage(), "/unsubscribe")));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(gson.toJson(new ExceptionModel(400, "Bad Request",
                    "Bad Request with: " + gson.toJson(form), "/unsubscribe")));

        }
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MODERATOR') or hasAuthority('STUDENT')")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity updateAll(@RequestBody UpdateAllForm form, @RequestHeader(value = "Authorization") String token) {
        try {
            return ResponseEntity.ok(eventDAO.updateEvent(token, form.getId(), form.getName(), form.getDescription(),
                    form.getAdminsId(), form.getUsersId(), form.getTags(), form.getOpen()));
        } catch (IllegalAccessException e) {
            return ResponseEntity.status(403).body(gson.toJson(new ExceptionModel(403, "Forbidden", e.getMessage(), "/change")));
        }catch (IllegalArgumentException e) {
            return ResponseEntity.status(403).body(gson.toJson(new ExceptionModel(403, "Forbidden",
                    "Access denied to update", "/update")));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(gson.toJson(new ExceptionModel(400, "Bad Request",
                    "Bad Request with: " + gson.toJson(form), "/update")));
        }
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MODERATOR') or hasAuthority('STUDENT')")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity deleteById(@RequestBody IdForm form, @RequestHeader(value = "Authorization") String token) {
        try {
            eventDAO.delete(form.getId(), token);
            return ResponseEntity.ok().build();
        } catch (IllegalAccessException e) {
            return ResponseEntity.status(403).body(gson.toJson(new ExceptionModel(403, "Forbidden", e.getMessage(), "/change")));
        }catch (IllegalArgumentException e) {
            return ResponseEntity.status(403).body(gson.toJson(new ExceptionModel(403, "Forbidden",
                    "Access denied to delete", "/delete")));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(gson.toJson(new ExceptionModel(400, "Bad Request",
                    "Bad Request with: " + gson.toJson(form), "/delete")));
        }
    }


    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MODERATOR') or hasAuthority('STUDENT')")
    @RequestMapping(value = "/change", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity changeState(@RequestBody ChangeStateForm form, @RequestHeader(value = "Authorization") String token) {
        try {
            return ResponseEntity.ok(eventDAO.changeState(token, form.getId(), form.getOpen()));
        } catch (IllegalAccessException e) {
            return ResponseEntity.status(403).body(gson.toJson(new ExceptionModel(403, "Forbidden", e.getMessage(), "/change")));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(403).body(gson.toJson(new ExceptionModel(403, "Forbidden",
                    "Access denied to change state", "/change")));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(gson.toJson(new ExceptionModel(400, "Bad Request",
                    "Bad Request with: " + gson.toJson(form), "/change")));
        }
    }

    @RequestMapping(value = "/read/posts", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity readPosts(@RequestBody IdForm form) {
        try {
            return ResponseEntity.ok(new ResponseJsonPostForm(eventDAO.getPosts(form.getId())));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(gson.toJson(new ExceptionModel(400, "Bad Request",
                    "Bad Request with: " + gson.toJson(form), "/read/posts")));

        }
    }

    @RequestMapping(value = "/create/post", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity createPost(@RequestBody CreatePostForm form, @RequestHeader(value = "Authorization") String token) {
        try {
            return ResponseEntity.ok((eventDAO.createPost(token, form.getPostName(), form.getPostDescription(),
                    form.getParentId(), form.getTags())));
        } catch (IllegalAccessException e) {
            return ResponseEntity.status(403).body(gson.toJson(new ExceptionModel(403, "Forbidden", e.getMessage(), "/create/post")));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(gson.toJson(new ExceptionModel(400, "Bad Request",
                    "Bad Request with: " + gson.toJson(form), "/create/post")));

        }
    }

    //TODO @RequestMapping(value="create/post", method =  RequestMethod.POST)

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MODERATOR') or hasAuthority('STUDENT')")
    @RequestMapping(value = "/notification", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity sendNotification(@RequestBody RequestNotificationForm form, @RequestHeader(value = "Authorization") String token) {
        try {
            eventDAO.sendNotification(form.getUserModels(), form.getId(), form.getRole(), token);
            return ResponseEntity.ok().build();
        } catch (IllegalAccessException e) {
            return ResponseEntity.status(403).body(gson.toJson(new ExceptionModel(403, "Forbidden", e.getMessage(), "/notification")));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(403).body(gson.toJson(new ExceptionModel(403, "Forbidden",
                    "Access denied to send notifications", "/notification")));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(gson.toJson(new ExceptionModel(400, "Bad Request",
                    "Bad Request with: " + gson.toJson(form), "/notification")));
        }
    }

}
