package com.rafaelsalazar.app;

import com.rafaelsalazar.server.HTTPServer;
import com.rafaelsalazar.server.annotation.Application;

import java.io.IOException;
import java.net.URISyntaxException;

@Application
public class Main {
    public static void main(String[] args) {
        try {
            HTTPServer.start(Main.class);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}