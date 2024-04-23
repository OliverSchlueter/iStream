package de.itbw18.istream.cmd;

public class Main {
    public static void main(String[] args) {
        Backend backend = new Backend();
        backend.init();
        backend.start();
    }
}