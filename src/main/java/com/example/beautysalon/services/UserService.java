package com.example.beautysalon.services;

import com.example.beautysalon.models.User;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private List<User> users;
    private User currentUser;

    public UserService() {
        users = new ArrayList<>();
        users.add(new User("admin", "admin", true));
        users.add(new User("user", "user", false));
        users.add(new User("user1", "user2", false));
    }

    public boolean register(String username, String password, boolean isAdmin) {
        if (getUserByUsername(username) != null) {
            return false;
        }
        users.add(new User(username, password, isAdmin));
        return true;
    }

    public boolean login(String username, String password) {
        User user = getUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            return true;
        }
        return false;
    }

    public void logout() {
        currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    private User getUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
}