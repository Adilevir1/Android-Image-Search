package com.exercise.search.task;

import android.content.Context;

import com.exercise.search.cache.ISearchResultsCache;
import com.exercise.search.cache.SearchResultsCache;
import com.exercise.search.fetcher.GoogleImageSearchFetcher;
import com.exercise.search.fetcher.ISearchResultsFetcher;
import com.exercise.search.parser.GoogleImageSearchParser;
import com.exercise.search.parser.ISearchResultsParser;

public class SearchLoaderFactory implements ISearchLoaderFactory {


    @Override
    public ISearchResultsCache getSearchResultsCache(Context context) {
        return new SearchResultsCache(context);
    }

    @Override
    public ISearchResultsFetcher getSearchResultsFetcher(Context context) {
        return new GoogleImageSearchFetcher(context);
    }

    @Override
    public ISearchResultsParser getSearchResultsParser() {
        return new GoogleImageSearchParser();
    }
}
