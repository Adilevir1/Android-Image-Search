package com.exercise.search.fetcher;


import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.exercise.search.task.SearchParams;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.CountDownLatch;

public class GoogleImageSearchFetcher implements ISearchResultsFetcher {

    private static final String TAG = "GoogleImageFetcher";
    private static final int PAGE_ITEMS_COUNT = 10;

    private static final String URL_BASE = "https://www.googleapis.com/customsearch/v1" +
            "?key=AIzaSyBW8wxhgNKU36Q_WPqJciAa9no2mqtcng4" +
            "&cx=005800383728131713214:5jocmduwqum" +
            "&searchType=image" +
            "&num=" + PAGE_ITEMS_COUNT +
            "&start=%s" +
            "&q=%s";

    private Context context;

    public GoogleImageSearchFetcher(Context context) {
        this.context = context;
    }

    @Override
    public String fetchSearchResults(SearchParams searchParams) {
        final StringBuilder sb = new StringBuilder();
        final CountDownLatch latch = new CountDownLatch(1);
        final int start = 1 + searchParams.page * PAGE_ITEMS_COUNT;
        final String urlString = String.format(URL_BASE, start, getEncodedQuery(searchParams));

        Volley.newRequestQueue(context).add(new StringRequest(Request.Method.GET, urlString,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        sb.append(response);
                        latch.countDown();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error response for url - " + urlString, error);
                        latch.countDown();
                    }
                }));

        try {
            latch.await();
        } catch (InterruptedException e) {
            Log.e(TAG, "Error fetching results for url - " + urlString, e);
        }

        return sb.toString();
    }

    private String getEncodedQuery(SearchParams searchParams) {
        String encodeQuery = searchParams.query;
        try {
            encodeQuery = URLEncoder.encode(searchParams.query, "utf-8");
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "error getEncodedQuery ", e);
        }
        return encodeQuery;
    }
}
