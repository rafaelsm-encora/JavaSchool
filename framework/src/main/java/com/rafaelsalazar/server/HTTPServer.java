package com.rafaelsalazar.server;

import com.rafaelsalazar.server.context.ApplicationContext;
import com.rafaelsalazar.server.handler.RestHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;

public class HTTPServer {

    public static void start(Class<?> clazz) throws IOException, URISyntaxException {
        ApplicationContext.getInstance().setup(clazz);

        HttpServer server = HttpServer.create(new InetSocketAddress(Integer.parseInt(ApplicationContext.getInstance().getProperty("server.port"))), 0);
        server.createContext(ApplicationContext.getInstance().getProperty("server.context-path"), new RestHandler());
        server.setExecutor(null);
        server.start();
    }
}
