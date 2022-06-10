package com.rafaelsalazar.server.context;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class CustomClassLoader {

    private CustomClassLoader(){}

    public static Map<String, String> readPropertiesFile() throws IOException, URISyntaxException {
        Map<String,String> values = new HashMap<>();
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        Properties properties = new Properties();
        URL resource = contextClassLoader.getResource("application.properties");
        FileReader fileReader = new FileReader(new File(resource.toURI()));
        properties.load(fileReader);
        for(Map.Entry<Object, Object> entry: properties.entrySet()) {
            values.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
        }
        return values;
    }

    public static List<String> getAllPackages(String packageName) {
        InputStream stream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream(packageName.replaceAll("[.]", "/"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        return reader.lines()
                .map(line -> packageName + "."+ line)
                .filter(line -> !line.endsWith(".class"))
                .collect(toList());
    }

    public static Set<Class> getAllClasses(String packageName) {
        InputStream stream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream(packageName.replaceAll("[.]", "/"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        return reader.lines()
                .filter(line -> line.endsWith(".class"))
                .map(line -> getClass(line, packageName))
                .collect(Collectors.toSet());
    }

    private static Class getClass(String className, String packageName) {
        try {
            return Class.forName(packageName + "."
                    + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
