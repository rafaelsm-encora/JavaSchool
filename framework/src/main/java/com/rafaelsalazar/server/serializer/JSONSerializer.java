package com.rafaelsalazar.server.serializer;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class JSONSerializer {
    public static <T> String serialize(T object) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        if (object instanceof Collection) {
            return serialize(((Collection) object).toArray());
        } else if (object.getClass().isArray()) {
            List<String> values = new ArrayList<>();
            int last = Array.getLength(object) ;
            for (int i = 0; i < last; i++) {
                Object value = Array.get(object, i);
                values.add(serialize(value));
            }
            return Arrays.toString(values.toArray());
        } else {
            Class objectClass = object.getClass();
            Field[] fields = objectClass.getDeclaredFields();
            List<String> values = new ArrayList<>(fields.length);
            for (final Field field : fields) {
                Method fieldGetter = objectClass.getMethod(getGetterMethodName(field));
                Object result = fieldGetter.invoke(object);
                String resultAsString = result != null ? result.toString() : "null";
                if (field.getType().getSimpleName().toLowerCase().equals("string")) {
                    values.add("\"" + field.getName() + "\" : \"" + resultAsString + "\"");
                } else {
                    values.add("\"" + field.getName() + "\" : " + resultAsString);
                }
            }

            String jsonValue = Arrays.toString(values.toArray());
            return "{" + jsonValue.substring(1, jsonValue.length() - 1) + "}";
        }
    }

    public static <T> T deserialize(Class<T> target, String json) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, NoSuchFieldException {
        T object = target.getDeclaredConstructor().newInstance();

        String[] values = json.substring(1).split(",\\s");

        for (String value : values) {
            String fieldName = value.trim().substring(1, value.trim().indexOf(":") - 1);

            final Field field = target.getDeclaredField(fieldName);

            Method fieldSetter = target.getMethod(getSetterMethodName(field), field.getType());

            Object fieldValue = value.substring(value.indexOf(":") + 3, value.lastIndexOf("\""));

            if (field.getType().getSimpleName().toLowerCase().contains("boolean")) {
                fieldValue = Boolean.parseBoolean(fieldValue.toString());
            } else if (field.getType().getSimpleName().toLowerCase().contains("int")) {
                fieldValue = Integer.parseInt(fieldValue.toString());
            } else if (field.getType().getSimpleName().toLowerCase().contains("double")) {
                fieldValue = Double.parseDouble(fieldValue.toString());
            }

            fieldSetter.invoke(object, fieldValue);
        }

        return object;
    }

    private static String getGetterMethodName(Field field) {
        String fieldName = field.getName();
        String camelFieldName = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
        String prefix = "get";
        if (field.getType().getSimpleName().toLowerCase().contains("boolean")) {
            prefix = "is";
        }

        return prefix + camelFieldName;
    }

    private static String getSetterMethodName(Field field) {
        String fieldName = field.getName();
        String camelFieldName = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
        return "set" + camelFieldName;
    }
}
