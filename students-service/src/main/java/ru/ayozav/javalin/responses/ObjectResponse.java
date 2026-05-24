package ru.ayozav.javalin.responses;

import io.javalin.http.Context;

public class ObjectResponse<T> extends Response {
    public ObjectResponse(Context ctx, T json) {
        super(ctx, 200, json);
    }
}
