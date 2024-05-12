package de.itbw18.istream.user.store;

import de.itbw18.istream.user.User;

import java.util.List;

public interface UserStore {

    void setup();

    User getUserByID(String id);

    User getUserByUsername(String username);

    User getUserByEmail(String email);

    List<User> getUsers();

    boolean addUser(User user);

    boolean updateUser(User user);

    boolean deleteUser(User user);
}
