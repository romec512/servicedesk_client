package com.example.roman.service_desk_client;

public class Globals {
    public static final String ServerAddress = "http://192.168.8.100:8000";
    private static final Globals ourInstance = new Globals();

    public static Globals getInstance() {
        return ourInstance;
    }

    private Globals() {
    }
}
