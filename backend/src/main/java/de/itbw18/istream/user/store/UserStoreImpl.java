package de.itbw18.istream.user.store;

import de.itbw18.istream.user.User;
import de.itbw18.istream.user.store.database.UserDatabase;

import java.util.List;

public class UserStoreImpl implements UserStore {

    private final UserDatabase db;

    public UserStoreImpl(UserDatabase db) {
        this.db = db;
    }

    @Override
    public void setup() {
        db.setup();
    }

    @Override
    public User getUserByID(String id) {
        return db.getUserByID(id);
    }

    @Override
    public User getUserByUsername(String username) {
        return db.getUserByUsername(username);
    }

    @Override
    public User getUserByEmail(String email) {
        return db.getUserByEmail(email);
    }

    @Override
    public List<User> getUsers() {
        return db.getUsers();
    }

    @Override
    public boolean addUser(User user) {
        return db.addUser(user);
    }

    @Override
    public boolean updateUser(User user) {
        return db.updateUser(user);
    }

    @Override
    public boolean deleteUser(User user) {
        return db.deleteUser(user);
    }
}
