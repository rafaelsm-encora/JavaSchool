package com.rafaelsalazar.server.request;

import com.rafaelsalazar.server.annotation.*;
import com.rafaelsalazar.server.context.ApplicationContext;
import com.rafaelsalazar.server.model.ErrorResponse;
import com.rafaelsalazar.server.serializer.JSONSerializer;
import com.rafaelsalazar.server.serializer.RequestSerializer;
import com.rafaelsalazar.server.utils.Utils;
import com.sun.net.httpserver.HttpExchange;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

public class Request {

    public static void post(HttpExchange exchange) {
        String body = Utils.getBody(exchange);
        String path = exchange.getRequestURI().getPath();
        String endpoint = RequestSerializer.getEndpoint(path);

        if (endpoint != null) {
            Class<?> clazz = ApplicationContext.getInstance().getController(endpoint);
            Object bean = ApplicationContext.getInstance().getBean(clazz);
            List<Method> methods = ApplicationContext.getInstance().getMethods(clazz, POST.class);
            Method method = null;

            if (methods.size() == 1) {
                method = methods.get(0);
            }

            if (method != null) {
                Parameter[] parameters = method.getParameters();
                Parameter parameter = null;

                for (Parameter parameter1 : parameters) {
                    if (parameter1.isAnnotationPresent(Body.class)) {
                        parameter = parameter1;
                        break;
                    }
                }

                if (parameter != null) {
                    try {
                        Object object = JSONSerializer.deserialize(parameter.getType(), body);
                        method.setAccessible(true);
                        method.invoke(bean, object);
                        Utils.manageResponse(exchange, object, 200);
                    } catch (NoSuchFieldException | InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void get(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();
        String endpoint = RequestSerializer.getEndpoint(path);
        int index = RequestSerializer.getIndex(path);

        if (endpoint != null) {
            Class<?> clazz = ApplicationContext.getInstance().getController(endpoint);
            Object bean = ApplicationContext.getInstance().getBean(clazz);
            List<Method> methods = ApplicationContext.getInstance().getMethods(clazz, GET.class);
            Method method = null;

            for (Method method1 : methods) {

                if (method1.isAnnotationPresent(GET.class)) {
                    GET get = method1.getAnnotation(GET.class);
                    String value = get.value();

                    if (index == 0 && value.isEmpty()) {
                        method = method1;
                        break;
                    } else if (index > 0 && !value.isEmpty()) {
                        method = method1;
                        break;
                    }
                }
            }

            if (method != null) {
                method.setAccessible(true);
                try {

                    if (index > 0) {
                        Object response = method.invoke(bean, index);

                        if (response != null) {
                            Utils.manageResponse(exchange, response, 200);
                        } else {
                            Utils.manageResponse(exchange, new ErrorResponse("Element not found"), 404);
                        }
                    } else {
                        List<Object> response = (List<Object>) method.invoke(bean);
                        Utils.manageResponse(exchange, response, 200);
                    }
                } catch (InvocationTargetException | IllegalAccessException e) {
                    Utils.manageResponse(exchange, new ErrorResponse("Server error"), 500);
                }
            }
        }
    }

    public static void put(HttpExchange exchange) {
        String body = Utils.getBody(exchange);
        String path = exchange.getRequestURI().getPath();
        String endpoint = RequestSerializer.getEndpoint(path);
        int index = RequestSerializer.getIndex(path);

        if (endpoint != null) {
            Class<?> clazz = ApplicationContext.getInstance().getController(endpoint);
            Object bean = ApplicationContext.getInstance().getBean(clazz);
            List<Method> methods = ApplicationContext.getInstance().getMethods(clazz, PUT.class);
            Method method = null;

            for(Method method1 : methods) {
                if (method1.isAnnotationPresent(PUT.class)) {
                    method = method1;
                }
            }

            if (method != null) {
                Parameter[] parameters = method.getParameters();
                Parameter parameter = null;
                for (Parameter parameter1 : parameters) {

                    if (parameter1.isAnnotationPresent(Body.class)) {
                        parameter = parameter1;
                        break;
                    }
                }


                if (parameter != null) {
                    try {
                        Object object = JSONSerializer.deserialize(parameter.getType(), body);
                        method.setAccessible(true);
                        Object response = method.invoke(bean, object, index);

                        if (response != null) {
                            Utils.manageResponse(exchange, response, 200);
                        } else {
                            Utils.manageResponse(exchange, new ErrorResponse("Element not found"), 404);
                        }
                    } catch (NoSuchFieldException | InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                        Utils.manageResponse(exchange, new ErrorResponse("Server error"), 500);
                    }
                }
            }
        }
    }

    public static void delete(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();
        String endpoint = RequestSerializer.getEndpoint(path);
        int index = RequestSerializer.getIndex(path);

        if (endpoint != null) {
            Class<?> clazz = ApplicationContext.getInstance().getController(endpoint);
            Object bean = ApplicationContext.getInstance().getBean(clazz);
            List<Method> methods = ApplicationContext.getInstance().getMethods(clazz, DELETE.class);
            Method method = null;

            for(Method method1 : methods) {
                if (method1.isAnnotationPresent(DELETE.class)) {
                    method = method1;
                }
            }

            if (method != null) {
                try {
                    method.setAccessible(true);
                    Object response = method.invoke(bean, index);
                    if (response != null) {
                        Utils.manageResponse(exchange, response, 200);
                    } else {
                        Utils.manageResponse(exchange, new ErrorResponse("Element not found"), 404);
                    }
                } catch (InvocationTargetException | IllegalAccessException e) {
                    Utils.manageResponse(exchange, new ErrorResponse("Server error"), 500);
                }
            }
        }
    }
}
