package de.itbw18.istream.helpers.database;

import java.sql.Connection;

public interface SQLConnector {

    boolean connect();

    boolean close();

    boolean isConnected();

    Connection getConnection();

}
