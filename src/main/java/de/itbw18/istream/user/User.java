package de.itbw18.istream.user;

public record User(String id, String name, String email, String password, Following[] following, String[] followers) {

    public record Following(String user, boolean inAppNotification, boolean emailNotification) {}
}
