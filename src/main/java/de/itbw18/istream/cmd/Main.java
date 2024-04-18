package de.itbw18.istream.cmd;

import de.itbw18.istream.server.HttpServer;

public class Main {
    public static void main(String[] args) {
        new HttpServer().start(8080);
    }
}