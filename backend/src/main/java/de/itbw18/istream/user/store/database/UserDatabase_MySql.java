package de.itbw18.istream.user.store.database;

import de.itbw18.istream.cmd.main.Backend;
import de.itbw18.istream.helpers.database.SQLConnector;
import de.itbw18.istream.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class UserDatabase_MySql implements UserDatabase {

    private final SQLConnector db;

    public UserDatabase_MySql(SQLConnector db) {
        this.db = db;
    }

    @Override
    public void setup() {
        if (!db.isConnected()) {
            throw new IllegalStateException("Database is not connected");
        }

        try {
            db.getConnection().createStatement().execute("""
                    CREATE TABLE IF NOT EXISTS `users`
                    (
                        `id`         varchar(255) NOT NULL,
                        `username`   varchar(255) NOT NULL,
                        `password`   varchar(255) NOT NULL,
                        `email`      varchar(255) NOT NULL,
                        `created_at` bigint       NOT NULL,
                        PRIMARY KEY (`id`)
                    );
                    """);
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to create users table", e);
        }

        try {
            db.getConnection().createStatement().execute("""
                    CREATE TABLE IF NOT EXISTS `followers`
                    (
                        `follower_id` varchar(255) NOT NULL,
                        `followee_id` varchar(255) NOT NULL,
                        `created_at`  bigint       NOT NULL,
                        PRIMARY KEY (`follower_id`, `followee_id`),
                        FOREIGN KEY (`follower_id`) REFERENCES `users` (`id`),
                        FOREIGN KEY (`followee_id`) REFERENCES `users` (`id`)
                    );
                                        """);
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to create followers table", e);
        }
    }

    private String[] getFollowing(String id) {
        List<String> following = new ArrayList<>();
        try {
            ResultSet result = db.getConnection().createStatement().executeQuery("SELECT * FROM followers WHERE follower_id = '" + id + "'");
            while (result.next()) {
                following.add(result.getString("followee_id"));
            }
        } catch (SQLException e) {
            Backend.LOGGER.error("Failed to get following by id", e);
        }
        return following.toArray(new String[0]);
    }

    private String[] getFollowers(String id) {
        List<String> followers = new ArrayList<>();
        try {
            ResultSet result = db.getConnection().createStatement().executeQuery("SELECT * FROM followers WHERE followee_id = '" + id + "'");
            while (result.next()) {
                followers.add(result.getString("follower_id"));
            }
        } catch (SQLException e) {
            Backend.LOGGER.error("Failed to get followers by id", e);
        }
        return followers.toArray(new String[0]);
    }

    @Override
    public User getUserByID(String id) {
        String username = "N/A";
        String email = "N/A";
        String password = "N/A";
        long createdAt = 0;

        try {
            ResultSet result = db.getConnection().createStatement().executeQuery("SELECT * FROM users WHERE id = '" + id + "'");
            if (result.next()) {
                username = result.getString("username");
                email = result.getString("email");
                password = result.getString("password");
                createdAt = result.getLong("created_at");
            }
        } catch (SQLException e) {
            Backend.LOGGER.error("Failed to get user by id", e);
            return null;
        }

        String[] following = getFollowing(id);
        String[] followers = getFollowers(id);

        return new User(
                id,
                username,
                email,
                password,
                createdAt,
                following,
                followers
        );
    }

    @Override
    public User getUserByUsername(String username) {
        String id = "N/A";
        String email = "N/A";
        String password = "N/A";
        long createdAt = 0;

        try {
            ResultSet result = db.getConnection().createStatement().executeQuery("SELECT * FROM users WHERE username = '" + username + "'");
            if (result.next()) {
                id = result.getString("id");
                email = result.getString("email");
                password = result.getString("password");
                createdAt = result.getLong("created_at");
            }
        } catch (SQLException e) {
            Backend.LOGGER.error("Failed to get user by username", e);
            return null;
        }

        String[] following = getFollowing(id);
        String[] followers = getFollowers(id);

        return new User(
                id,
                username,
                email,
                password,
                createdAt,
                following,
                followers
        );
    }

    @Override
    public User getUserByEmail(String email) {
        String id = "N/A";
        String username = "N/A";
        String password = "N/A";
        long createdAt = 0;

        try {
            ResultSet result = db.getConnection().createStatement().executeQuery("SELECT * FROM users WHERE email = '" + email + "'");
            if (result.next()) {
                id = result.getString("id");
                username = result.getString("username");
                password = result.getString("password");
                createdAt = result.getLong("created_at");
            }
        } catch (SQLException e) {
            Backend.LOGGER.error("Failed to get user by username", e);
            return null;
        }

        String[] following = getFollowing(id);
        String[] followers = getFollowers(id);

        return new User(
                id,
                username,
                email,
                password,
                createdAt,
                following,
                followers
        );
    }

    @Override
    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        try {
            ResultSet result = db.getConnection().createStatement().executeQuery("SELECT * FROM users");
            while (result.next()) {
                String id = result.getString("id");
                String username = result.getString("username");
                String email = result.getString("email");
                String password = result.getString("password");
                long createdAt = result.getLong("created_at");

                users.add(new User(
                        id,
                        username,
                        email,
                        password,
                        createdAt,
                        new String[0],
                        new String[0]
                ));
            }
        } catch (SQLException e) {
            Backend.LOGGER.error("Failed to get users", e);
        }
        return users;
    }

    @Override
    public boolean addUser(User user) {
        try {
            db.getConnection()
                    .createStatement()
                    .execute("INSERT INTO users (id, username, email, password, created_at) VALUES ('" + user.id() + "', '" + user.username() + "', '" + user.email() + "', '" + user.password() + "', " + user.createdAt() + ")");
        } catch (SQLException e) {
            Backend.LOGGER.error("Failed to add user", e);
            return false;
        }

        return true;
    }

    @Override
    public boolean updateUser(User user) {
        try {
            db.getConnection()
                    .createStatement()
                    .execute("UPDATE users SET username = '" + user.username() + "', email = '" + user.email() + "', password = '" + user.password() + "' WHERE id = '" + user.id() + "'");
        } catch (SQLException e) {
            Backend.LOGGER.error("Failed to update user", e);
            return false;
        }

        return true;
    }

    @Override
    public boolean deleteUser(User user) {
        try {
            db.getConnection()
                    .createStatement()
                    .execute("DELETE FROM users WHERE id = '" + user.id() + "'");
        } catch (SQLException e) {
            Backend.LOGGER.error("Failed to delete user", e);
            return false;
        }

        return true;
    }
}
