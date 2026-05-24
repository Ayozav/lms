package ru.ayozav.javalin.responses;

import io.javalin.http.Context;


public class Response {
    public Response(Context ctx, int status, Object json) {
        ctx.status(status).json(json);
    }

    public Response(Context ctx, int status) {
        ctx.status(status);
    }
}
