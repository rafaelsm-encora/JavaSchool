package com.rafaelsalazar.server.handler;

import com.rafaelsalazar.server.request.Request;
import com.rafaelsalazar.server.utils.Utils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class RestHandler implements HttpHandler {
    static final String GET = "GET";
    static final String POST = "POST";
    static final String DELETE = "DELETE";
    static final String PUT = "PUT";

    @Override
    public void handle(HttpExchange exchange) {
        switch (exchange.getRequestMethod()){
            case "POST":
                Request.post(exchange);
                break;
            case "GET":
                Request.get(exchange);
                break;
            case "PUT":
                Request.put(exchange);
                break;
            case "DELETE":
                Request.delete(exchange);
                break;
            default:
                Utils.manageResponse(exchange, "Method not allowed", 405);
                break;
        }
    }
}
