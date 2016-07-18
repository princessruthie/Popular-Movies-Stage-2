package com.ruthiefloats.popularmoviesstage1.model;

/**
 * Created on 7/18/16.
 */
public class Movie {
    private String mTitle;
    private int mReleaseYear;
    private String mPosterLocation;
    private double mVoteAverage;
    private String mPlotSynopsis;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public int getReleaseYear() {
        return mReleaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        mReleaseYear = releaseYear;
    }

    public String getPosterLocation() {
        return mPosterLocation;
    }

    public void setPosterLocation(String posterLocation) {
        mPosterLocation = posterLocation;
    }

    public double getVoteAverage() {
        return mVoteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        mVoteAverage = voteAverage;
    }

    public String getPlotSynopsis() {
        return mPlotSynopsis;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        mPlotSynopsis = plotSynopsis;
    }
}
