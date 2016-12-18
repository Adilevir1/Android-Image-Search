package com.exercise.search.cache;


public interface ISearchResultsCache {

    String getSearchResults(String key);

    void putSearchResults(String key, String value);
}
