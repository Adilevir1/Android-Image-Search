package com.exercise.search;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

/**
 * This activity responsible for getting the search input query from the user
 * and pass it to the {@link SearchFragment}
 */
public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private SearchFragment searchFragment;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        FragmentManager fm = getSupportFragmentManager();
        searchFragment = (SearchFragment) fm.findFragmentByTag(SearchFragment.TAG);
        if (searchFragment == null) {
            // no retain instance - add the fragment
            searchFragment = new SearchFragment();
            fm.beginTransaction().add(R.id.container, searchFragment, SearchFragment.TAG).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_activity, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        searchView = (SearchView) searchItem.getActionView();
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setOnQueryTextListener(this);
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchFragment.onQueryTextSubmit(query);
        searchView.clearFocus();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
