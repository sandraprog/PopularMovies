package com.sandraprog.popularmovies2.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sandrapog on 11.06.2018.
 */

public class TrailersList {
    @SerializedName("id")
    private int id_trailer;
    @SerializedName("results")
    private List<Trailer> results;

    public int getIdTrailer() {
        return id_trailer;
    }

    public List<Trailer> getResults() {
        return results;
    }
}
