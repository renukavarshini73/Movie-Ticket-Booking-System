package com.movieticket.model;

import java.io.Serializable;
import java.sql.Date;

/**
 * Model class representing a Movie Show screening.
 */
public class Show implements Serializable {
    private int showId;
    private int movieId;
    private int screenId;
    private Date showDate;
    private String showTime;
    private double ticketPrice;

    // Display fields populated via JOINs
    private String movieTitle;
    private String screenName;

    public Show() {}

    public Show(int showId, int movieId, int screenId, Date showDate, String showTime, double ticketPrice) {
        this.showId = showId;
        this.movieId = movieId;
        this.screenId = screenId;
        this.showDate = showDate;
        this.showTime = showTime;
        this.ticketPrice = ticketPrice;
    }

    public int getShowId() {
        return showId;
    }

    public void setShowId(int showId) {
        this.showId = showId;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public int getScreenId() {
        return screenId;
    }

    public void setScreenId(int screenId) {
        this.screenId = screenId;
    }

    public Date getShowDate() {
        return showDate;
    }

    public void setShowDate(Date showDate) {
        this.showDate = showDate;
    }

    public String getShowTime() {
        return showTime;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    @Override
    public String toString() {
        return (movieTitle != null ? movieTitle + " - " : "") +
               showDate + " @ " + showTime + 
               (screenName != null ? " (" + screenName + ")" : "") + 
               " - $" + String.format("%.2f", ticketPrice);
    }
}
