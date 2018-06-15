package com.sandraprog.popularmovies2.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sandrapog on 11.06.2018.
 */

public class ReviewsList {

    @SerializedName("results")
    private List<Review> results;

    public List<Review> getResults() {
        return results;
    }
}
