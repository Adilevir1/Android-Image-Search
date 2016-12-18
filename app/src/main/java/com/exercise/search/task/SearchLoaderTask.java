package com.exercise.search.task;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.exercise.search.Item;
import com.exercise.search.cache.ISearchResultsCache;
import com.exercise.search.fetcher.ISearchResultsFetcher;
import com.exercise.search.parser.ISearchResultsParser;

import java.util.List;

public class SearchLoaderTask extends AsyncTask<SearchParams, Integer, List<Item>> {

    private final ISearchResultsCache searchResultsCache;
    private final ISearchResultsFetcher searchResultsFetcher;
    private final ISearchResultsParser searchResultsParser;
    private final OnSearchResultsCallback callback;

    private List<Item> cachedItems;
    private List<Item> fetcherItems;

    public interface OnSearchResultsCallback {
        /**
         * Called when search results are loaded from cache or from fetcher.
         *
         * @param items   the results items
         * @param hasNext true when next results are available
         */
        void onSearchResultsLoaded(List<Item> items, boolean hasNext);

        /**
         * Called when search results are loaded from cache
         * and needs to be updated according to new results from fetcher.
         *
         * @param item     the updated item
         * @param position position of the updated item
         */
        void onSearchResultsUpdated(Item item, int position);
    }

    public SearchLoaderTask(Context context, OnSearchResultsCallback callback) {
        this(context, callback, new SearchLoaderFactory());
    }

    public SearchLoaderTask(Context context, OnSearchResultsCallback callback, ISearchLoaderFactory searchTaskFactory) {
        this.callback = callback;
        searchResultsCache = searchTaskFactory.getSearchResultsCache(context);
        searchResultsFetcher = searchTaskFactory.getSearchResultsFetcher(context);
        searchResultsParser = searchTaskFactory.getSearchResultsParser();

        cachedItems = null;
        fetcherItems = null;
    }

    @Override
    protected List<Item> doInBackground(SearchParams... params) {
        //Retrieve from cache first
        String searchKey = params[0].query + params[0].page;
        String cacheSearchResults = searchResultsCache.getSearchResults(searchKey);
        if (!TextUtils.isEmpty(cacheSearchResults)) {
            cachedItems = searchResultsParser.parse(cacheSearchResults);
            publishProgress();
        }

        String searchResults = searchResultsFetcher.fetchSearchResults(params[0]);
        searchResultsCache.putSearchResults(searchKey, searchResults);
        if (isCancelled()) {
            return null;
        }
        fetcherItems = searchResultsParser.parse(searchResults);
        if (cachedItems != null && cachedItems.size() == fetcherItems.size()) {
            compareAndUpdate();
            return null;
        }

        return fetcherItems;
    }

    private void compareAndUpdate() {
        for (int i = 0; i < cachedItems.size(); i++) {
            if (!cachedItems.get(i).equals(fetcherItems.get(i))) {
                publishProgress(i);
            }
        }
    }

    @Override
    protected void onProgressUpdate(Integer... positions) {
        if (positions.length == 0) {
            callback.onSearchResultsLoaded(cachedItems, searchResultsParser.hasNext());
        } else {
            for (int pos : positions) {
                callback.onSearchResultsUpdated(fetcherItems.get(pos), pos);
            }
        }
    }

    @Override
    protected void onPostExecute(List<Item> items) {
        if (items != null) {
            callback.onSearchResultsLoaded(items, searchResultsParser.hasNext());
        }
    }
}
