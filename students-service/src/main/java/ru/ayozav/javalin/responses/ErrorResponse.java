package ru.ayozav.javalin.responses;

import io.javalin.http.Context;
import java.util.Map;


public class ErrorResponse extends Response {
    public ErrorResponse(Context ctx, int status, String message) {
        super(ctx, status, Map.of("message", message));
    }
}
