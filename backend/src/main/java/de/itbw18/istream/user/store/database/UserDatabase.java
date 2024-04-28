package de.itbw18.istream.user.store.database;

import de.itbw18.istream.user.User;

public interface UserDatabase {

    void setup();

    User getUserByID(String id);

    User getUserByUsername(String username);

    User getUserByEmail(String email);

    boolean addUser(User user);

    boolean updateUser(User user);

    boolean deleteUser(User user);

}
