package com.exercise.search.cache;


import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SearchResultsCache implements ISearchResultsCache {

    private static final String TAG = "SearchResultsCache";

    private final File cacheDir;

    public SearchResultsCache(Context context) {
        cacheDir = context.getCacheDir();
    }

    @Override
    public String getSearchResults(String key) {
        String results = "";
        File file = new File(cacheDir, key);
        try {
            if (file.exists()) {
                InputStream inputStream = new FileInputStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder buffer = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                reader.close();
                results = buffer.toString();
            }
        } catch (Exception e) {
            Log.e(TAG, "error reading from cache", e);
        }

        return results;
    }

    @Override
    public void putSearchResults(String key, String value) {
        File file = new File(cacheDir, key);
        try {
            FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
            fileWriter.write(value);
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            Log.e(TAG, "error writing to cache", e);
        }
    }
}
