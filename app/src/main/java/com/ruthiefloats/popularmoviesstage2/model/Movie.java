package com.ruthiefloats.popularmoviesstage2.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created on 7/18/16.  Movie class models The Movie DB API movie data.
 * Uses a rote implementation of Parcelable (from parcelabler.com)
 */
public class Movie implements Parcelable {
    /**
     * Member variable names do not reflect Java standards because using this class
     * with Retrofit requires matching the names to those in the JSON data.
     */
    private String title;
    private String release_date;
    private String poster_path;
    private double vote_average;
    private String overview;


    /**
     * @param title        Name of the movie
     * @param release_date year the film was released
     * @param poster_path  the image location postfix?
     * @param vote_average average score
     * @param overview     summary of the plot
     */
    public Movie(String title, String release_date, String poster_path, double vote_average, String overview) {
        this.title = title;
        this.release_date = release_date;
        this.poster_path = poster_path;
        this.vote_average = vote_average;
        this.overview = overview;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    protected Movie(Parcel in) {
        title = in.readString();
        release_date = in.readString();
        poster_path = in.readString();
        vote_average = in.readDouble();
        overview = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(release_date);
        dest.writeString(poster_path);
        dest.writeDouble(vote_average);
        dest.writeString(overview);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}