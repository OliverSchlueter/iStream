package de.itbw18.istream.user;

public record User(String id, String username, String email, String password, long createdAt, String[] following, String[] followers) {

}
