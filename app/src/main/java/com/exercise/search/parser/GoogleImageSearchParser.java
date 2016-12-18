package com.exercise.search.parser;

import android.util.Log;

import com.exercise.search.Item;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GoogleImageSearchParser implements ISearchResultsParser {

    private static final String TAG = "GoogleImageSearchParser";

    private static final String NEXT_PAGE = "nextPage";
    private static final String ITEMS = "items";
    private static final String COUNT = "count";
    private static final String QUERIES = "queries";

    private boolean hasNext = false;

    @Override
    public List<Item> parse(String searchResults) {
        List<Item> items = new ArrayList<>();

        try {
            JSONObject head = new JSONObject(searchResults);
            int nextPageCount = head.getJSONObject(QUERIES).getJSONArray(NEXT_PAGE).getJSONObject(0).getInt(COUNT);
            hasNext = nextPageCount > 0;

            Gson gson = new Gson();
            Type type = new TypeToken<List<Item>>() {
            }.getType();
            items = gson.fromJson(head.getString(ITEMS), type);

        } catch (JSONException e) {
            Log.e(TAG, "Error parsing search results", e);
        }

        return items;
    }

    @Override
    public boolean hasNext() {
        return hasNext;
    }
}
