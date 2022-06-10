package com.rafaelsalazar.app.controller;

import com.rafaelsalazar.app.bean.User;
import com.rafaelsalazar.app.service.UserService;
import com.rafaelsalazar.server.annotation.*;

import java.util.List;

@RestController("/users")
public class UserController {

    @Autowire
    private UserService userService;

    @POST
    public User create(@Body User user) {
        return userService.addUser(user);
    }

    @GET
    public List<User> read() {
        return userService.getUsers();
    }

    @GET("/{id}")
    public User read(int id) {
        return userService.getUser(id);
    }

    @PUT("/{id}")
    public User update(@Body User user, int id) {
        return userService.editUser(user, id);
    }

    @DELETE("/{id}")
    public User delete(int id) {
        return userService.deleteUser(id);
    }
}
