package com.movieticket.model;

import java.io.Serializable;

/**
 * Model class representing a Movie.
 */
public class Movie implements Serializable {
    private int movieId;
    private String title;
    private String genre;
    private int durationMinutes;
    private String language;
    private String rating;

    public Movie() {}

    public Movie(int movieId, String title, String genre, int durationMinutes, String language, String rating) {
        this.movieId = movieId;
        this.title = title;
        this.genre = genre;
        this.durationMinutes = durationMinutes;
        this.language = language;
        this.rating = rating;
    }

    public Movie(String title, String genre, int durationMinutes, String language, String rating) {
        this.title = title;
        this.genre = genre;
        this.durationMinutes = durationMinutes;
        this.language = language;
        this.rating = rating;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return title + " (" + language + " | " + genre + " | " + durationMinutes + " mins | " + rating + ")";
    }
}
