package de.itbw18.istream.user;

public record User(String id, String username, String email, String password, long created_at, String[] following, String[] followers) {

}
