package com.exercise.search.fetcher;

import android.content.Context;

import com.exercise.search.task.SearchParams;

public interface ISearchResultsFetcher {

    String fetchSearchResults(SearchParams searchParams);
}
