package com.rafaelsalazar.server.serializer;

public class RequestSerializer {
    public static int getIndex(String path) {
        if (path != null) {
            String[] paths = path.split("/");
            if (paths.length > 1) {
                try {
                    return Integer.parseInt(paths[paths.length - 1]);
                } catch (NumberFormatException e) {
                    return 0;
                }
            }
        }
        return 0;
    }

    public static String getEndpoint(String path) {
        if (path != null) {
            String[] paths = path.split("/");
            if (paths.length > 0) {
                return "/" + paths[1];
            }
        }
        return null;
    }
}
