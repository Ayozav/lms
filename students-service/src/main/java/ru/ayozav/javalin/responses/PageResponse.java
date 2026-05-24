package ru.ayozav.javalin.responses;

import io.javalin.http.Context;

import java.util.List;

public class PageResponse <T> extends Response {

    public PageResponse(Context ctx, List<T> list) {
        super(ctx, 200, list);
    }
}
