package ru.ayozav.javalin.responses;

import io.javalin.http.Context;


public class OkResponse extends Response {
    public OkResponse(Context ctx) {
        super(ctx, 200);
    }
}
