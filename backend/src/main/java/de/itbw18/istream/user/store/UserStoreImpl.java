package de.itbw18.istream.user.store;

import de.itbw18.istream.streamconfig.StreamConfig;
import de.itbw18.istream.streamconfig.store.StreamConfigStore;
import de.itbw18.istream.user.User;
import de.itbw18.istream.user.store.database.UserDatabase;

import java.util.List;

public class UserStoreImpl implements UserStore {

    private final UserDatabase db;

    private final StreamConfigStore streamConfigStore;

    public UserStoreImpl(UserDatabase db, StreamConfigStore streamConfigStore) {
        this.db = db;
        this.streamConfigStore = streamConfigStore;
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
        streamConfigStore.createStreamConfig(new StreamConfig(user.id(), "My awesome stream", "My Stream Description", StreamConfig.Category.JUST_CHATTING));
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
