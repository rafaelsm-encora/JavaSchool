package com.rafaelsalazar.server.context;

import com.rafaelsalazar.server.annotation.Application;
import com.rafaelsalazar.server.annotation.Autowire;
import com.rafaelsalazar.server.annotation.RestController;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.*;

public class ApplicationContext {

    private static ApplicationContext INSTANCE;

    private ApplicationContext(){}

    public static ApplicationContext getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ApplicationContext();
        }
        return INSTANCE;
    }

    private final Map<Class<?>,Object> beans = new HashMap<>();
    private final Map<String,Class<?>> controllers = new HashMap<>();
    private final Map<String,String> propertyValues = new HashMap<>();

    public void setup(Class<?> clazz) throws IOException, URISyntaxException {
        if(clazz.isAnnotationPresent(Application.class)) {
            String packageName = clazz.getPackageName();
            System.out.println(packageName);
            loadControllers(packageName);
            loadBeans(packageName);
            loadProperties();
        }
    }

    private void loadControllers(String packageName) {
        List<String> packageNames = CustomClassLoader.getAllPackages(packageName);
        Set<Class> classes = new HashSet<>();
        for (String name: packageNames) {
            classes.addAll(CustomClassLoader.getAllClasses(name));
        }
        for(Class<?> clazz: classes) {
            if (clazz.isAnnotationPresent(RestController.class)) {
                RestController controller = clazz.getAnnotation(RestController.class);
                controllers.put(controller.value(), clazz);
            }
        }
    }

    private void loadBeans(String packageName) {
        List<String> packageNames = CustomClassLoader.getAllPackages(packageName);
        Set<Class> classes = new HashSet<>();
        for (String name: packageNames) {
            classes.addAll(CustomClassLoader.getAllClasses(name));
        }

        for(Class clazz : classes) {
            try {
                Object object = clazz.getConstructor().newInstance();
                Field[] fields = object.getClass().getDeclaredFields();
                for(Field field: fields) {
                    if (field.isAnnotationPresent(Autowire.class)) {
                        object = injectDependencies(object, field);
                        beans.put(clazz, object);
                    }
                }
            } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private Object injectDependencies(Object object, Field field) {
        field.setAccessible(true);
        Class<?> fieldClass = field.getType();
        try {
            Object objectField = fieldClass.getConstructor().newInstance();
            field.set(object, objectField);
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return object;
    }

    private void loadProperties() throws IOException, URISyntaxException {
        //Add default properties
        propertyValues.put("server.port", "8080");
        propertyValues.put("server.context-path", "/");
        propertyValues.putAll(CustomClassLoader.readPropertiesFile());
    }

    public String getProperty(String key) {
        return propertyValues.get(key);
    }

    public Class<?> getController(String endpoint) {
        return controllers.get(endpoint);
    }

    public Object getBean(Class<?> clazz) {
        return beans.get(clazz);
    }

    public List<Method> getMethods(Class<?> clazz, Class annotation) {
        Method[] allMethods = clazz.getDeclaredMethods();
        List<Method> methods = new ArrayList<>();
        for(Method method : allMethods) {
            if (method.isAnnotationPresent(annotation)) {
                methods.add(method);
            }
        }
        return methods;
    }
}
