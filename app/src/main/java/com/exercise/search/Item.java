package com.exercise.search;

public class Item {

    public final String title;
    public final String link;

    public Item(String title, String link) {
        this.title = title;
        this.link = link;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Item)) {
            return false;
        }

        Item other = (Item) obj;
        return title.equals(other.title) && link.equals(other.link);
    }
}
