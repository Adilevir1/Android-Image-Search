package com.exercise.search.task;

import android.content.Context;

import com.exercise.search.cache.ISearchResultsCache;
import com.exercise.search.fetcher.ISearchResultsFetcher;
import com.exercise.search.parser.ISearchResultsParser;

public interface ISearchLoaderFactory {

    ISearchResultsCache getSearchResultsCache(Context context);

    ISearchResultsFetcher getSearchResultsFetcher(Context context);

    ISearchResultsParser getSearchResultsParser();
}
