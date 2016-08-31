package net.hugarte.android.popularmoviesapp;

/**
 * Created by Hasier on 31/08/2016.
 */
public class MovieInfo {
    private String poster_path;
    private String overview;
    private String id;
    private String original_title;
    private String vote_average;
    private String release_date;

    public MovieInfo(){

    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }
    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getPoster_path() {
        return poster_path;
    }
}
