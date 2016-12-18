package com.exercise.search.task;

public class SearchParams {

    public final String query;
    public final int page;

    public SearchParams(String query, int page) {
        this.query = query;
        this.page = page;
    }
}
