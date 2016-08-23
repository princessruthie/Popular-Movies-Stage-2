package com.ruthiefloats.popularmoviesstage2.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class BOMR {

    List<Results> resultsList;

    public BOMR(){
        resultsList = new ArrayList<>();
    }

    public List<Results> getResultSet(){
        return resultsList;
    }

    public class Results {

        public String posterPath;
        public boolean adult;
        public String overview;
        public String releaseDate;
        public List<Integer> genreIds = new ArrayList<Integer>();
        public int id;
        public String originalTitle;
        public String originalLanguage;
        public String title;
        public String backdropPath;
        public double popularity;
        public int voteCount;

        public String getPosterPath() {
            return posterPath;
        }

        public void setPosterPath(String posterPath) {
            this.posterPath = posterPath;
        }

        public boolean isAdult() {
            return adult;
        }

        public void setAdult(boolean adult) {
            this.adult = adult;
        }

        public String getOverview() {
            return overview;
        }

        public void setOverview(String overview) {
            this.overview = overview;
        }

        public String getReleaseDate() {
            return releaseDate;
        }

        public void setReleaseDate(String releaseDate) {
            this.releaseDate = releaseDate;
        }

        public List<Integer> getGenreIds() {
            return genreIds;
        }

        public void setGenreIds(List<Integer> genreIds) {
            this.genreIds = genreIds;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getOriginalTitle() {
            return originalTitle;
        }

        public void setOriginalTitle(String originalTitle) {
            this.originalTitle = originalTitle;
        }

        public String getOriginalLanguage() {
            return originalLanguage;
        }

        public void setOriginalLanguage(String originalLanguage) {
            this.originalLanguage = originalLanguage;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getBackdropPath() {
            return backdropPath;
        }

        public void setBackdropPath(String backdropPath) {
            this.backdropPath = backdropPath;
        }

        public double getPopularity() {
            return popularity;
        }

        public void setPopularity(double popularity) {
            this.popularity = popularity;
        }

        public int getVoteCount() {
            return voteCount;
        }

        public void setVoteCount(int voteCount) {
            this.voteCount = voteCount;
        }

        public boolean isVideo() {
            return video;
        }

        public void setVideo(boolean video) {
            this.video = video;
        }

        public double getVoteAverage() {
            return voteAverage;
        }

        public void setVoteAverage(double voteAverage) {
            this.voteAverage = voteAverage;
        }

        public boolean video;
        public double voteAverage;

    }

    class Example {
        public int page;
        public List<Results> results = new ArrayList<>();

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public List<Results> getResults() {
            return results;
        }

        public void setResults(List<Results> results) {
            this.results = results;
        }

        public int getTotalResults() {
            return totalResults;
        }

        public void setTotalResults(int totalResults) {
            this.totalResults = totalResults;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }

        public int totalResults;
        public int totalPages;

    }
}
