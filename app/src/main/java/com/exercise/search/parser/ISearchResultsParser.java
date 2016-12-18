package com.exercise.search.parser;

import com.exercise.search.Item;

import java.util.List;

public interface ISearchResultsParser {

    List<Item> parse(String searchResults);

    boolean hasNext();
}
