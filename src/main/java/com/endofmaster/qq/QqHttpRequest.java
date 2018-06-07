package com.endofmaster.qq;

import java.util.*;

/**
 * @author YQ.Huang
 */
public class QqHttpRequest {

    private final List<Arg> args = new ArrayList<>();
    private final Map<String, String> headers = new LinkedHashMap<>();

    private String method = "GET";
    private String dataType = "json";
    private String url;

    public QqHttpRequest(String url) {
        this.url = url;
    }

    public void setHeader(String name, String value) {
        headers.put(name, value);
    }

    public QqHttpRequest withHeader(String name, String value) {
        setHeader(name, value);
        return this;
    }

    public QqHttpRequest withArg(String key, Object value) {
        if (value != null) {
            args.add(new Arg(key, value));
        }
        return this;
    }

    /**
     * Unlike {@link #withArg(String, Object)}, overrides the existing value
     */
    public QqHttpRequest setArg(String key, Object value) {
        for (Arg e : args) {
            if (e.key.equals(key)) {
                e.value = value;
                return this;
            }
        }
        return withArg(key, value);
    }

    public QqHttpRequest withMethod(String method) {
        this.method = method.toUpperCase();
        return this;
    }

    public String getDataType() {
        return dataType;
    }

    public QqHttpRequest withDataType(String dataType) {
        this.dataType = dataType.toUpperCase();
        return this;
    }

    public List<Arg> getArgs() {
        return Collections.unmodifiableList(args);
    }

    public Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public static class Arg {
        String key;
        Object value;

        private Arg(String key, Object value) {
            this.key = key;
            this.value = value;
        }
    }
}
