package com.exercise.search;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.exercise.search.task.SearchLoaderTask;
import com.exercise.search.task.SearchParams;

import java.util.List;

/**
 * This fragment holds {@link SearchLoaderTask} instance
 * to execute when a new search query received from activity or when next page request received from adapter.
 * the search task results received by its callback are delivered to the adapter
 * to display or update the results.
 */
public class SearchFragment extends Fragment implements SearchLoaderTask.OnSearchResultsCallback, SearchRecyclerAdapter.Callback {

    public static final String TAG = "SearchFragment";
    public static final int FIRST_PAGE_NUM = 0;

    private int currentPage;
    private String currentQuery;
    private SearchLoaderTask searchLoaderTask;
    private SearchRecyclerAdapter adapter;
    private View loadingLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        adapter = new SearchRecyclerAdapter(this);
    }

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, null);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        loadingLayout = view.findViewById(R.id.loading_layout);
        loadingLayout.setVisibility(View.INVISIBLE);

        return view;
    }

    public void onQueryTextSubmit(String newQuery) {
        loadingLayout.setVisibility(View.VISIBLE);
        if (searchLoaderTask != null) {
            searchLoaderTask.cancel(true);
        }

        currentPage = FIRST_PAGE_NUM;
        currentQuery = newQuery;
        executeSearch();
    }

    @Override
    public void onLoadNext() {
        currentPage += 1;
        executeSearch();
    }

    @Override
    public void onItemSelected(Item item) {
        Intent intent = new Intent(getContext(), FullScreenImageActivity.class);
        intent.putExtra(FullScreenImageActivity.EXTRA_IMAGE_LINK, item.link);
        intent.putExtra(FullScreenImageActivity.EXTRA_IMAGE_TITLE, item.title);
        startActivity(intent);
    }

    private void executeSearch() {
        searchLoaderTask = new SearchLoaderTask(getContext().getApplicationContext(), this);
        searchLoaderTask.execute(new SearchParams(currentQuery, currentPage));
    }

    @Override
    public void onSearchResultsLoaded(List<Item> items, boolean hasNext) {
        loadingLayout.setVisibility(View.INVISIBLE);

        if (currentPage == FIRST_PAGE_NUM) {
            adapter.setItems(items, hasNext);
        } else {
            adapter.addItems(items, hasNext);
        }
    }

    @Override
    public void onSearchResultsUpdated(Item item, int position) {
        adapter.updateItem(item, position);
    }
}
