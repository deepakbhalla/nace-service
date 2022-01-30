package com.nace.filters;

/**
 * Holds the correlation id for a given request.
 * 
 * @author Deepak Bhalla
 *
 */
public class RequestCorrelation {

    public static final String CORRELATION_ID_HEADER = "CORRELATION_ID";

    private static final ThreadLocal<String> id = new ThreadLocal<String>();

    public static void setId(String correlationId) {
        id.set(correlationId);
    }

    public static String getId() {
        return id.get();
    }
}
