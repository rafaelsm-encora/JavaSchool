package com.rafaelsalazar.app.service;

import com.rafaelsalazar.app.bean.User;
import com.rafaelsalazar.server.annotation.Autowire;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    private List<User> users = new ArrayList<>();
    private static int index = 1;

    public List<User> getUsers() {
        return users;
    }

    public User getUser(int id) {
        for (User user : users) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

    public User addUser(User user) {
        user.setId(index);
        index++;
        users.add(user);
        return user;
    }

    public User editUser(User user, int id) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(id)) {
                users.get(i).setName(user.getName());
                users.get(i).setLastName(user.getLastName());
                users.get(i).setEmail(user.getEmail());
                return users.get(i);
            }
        }
        return null;
    }

    public User deleteUser(int id) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(id)) {
                User user = users.get(i);
                users.remove(i);
                return user;
            }
        }
        return null;
    }
}
