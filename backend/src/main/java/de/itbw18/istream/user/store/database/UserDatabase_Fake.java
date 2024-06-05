package de.itbw18.istream.user.store.database;

import de.itbw18.istream.user.User;

import java.util.ArrayList;
import java.util.List;

public class UserDatabase_Fake implements UserDatabase {

    List<User> users;

    @Override
    public void setup() {
        users = new ArrayList<>();
    }

    @Override
    public User getUserByID(String id) {
        for (User user : users) {
            if (user.id().equals(id)) {
                return user;
            }
        }

        return null;
    }

    @Override
    public User getUserByUsername(String username) {
        for (User user : users) {
            if (user.username().equals(username)) {
                return user;
            }
        }

        return null;
    }

    @Override
    public User getUserByEmail(String email) {
        for (User user : users) {
            if (user.email().equals(email)) {
                return user;
            }
        }

        return null;
    }

    @Override
    public List<User> getUsers() {
        return users;
    }

    @Override
    public boolean addUser(User user) {
        return users.add(user);
    }

    @Override
    public boolean updateUser(User user) {
        users.removeIf(u -> u.id().equals(user.id()));
        users.add(user);
        return true;
    }

    @Override
    public boolean deleteUser(User user) {
        users.removeIf(u -> u.id().equals(user.id()));
        return true;
    }
}
