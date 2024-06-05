package de.itbw18.istream.cmd;

import de.itbw18.istream.cmd.main.Backend;

public class Main {
    public static void main(String[] args) {
        Backend backend = new Backend();
        backend.init();
        backend.start();
    }
}