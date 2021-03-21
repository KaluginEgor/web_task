package com.example.demo_web.entity;

import java.time.LocalDate;
import java.util.List;

public class Movie extends Entity {
    private int id;
    private String title;
    private String description;
    private float rating;
    private LocalDate releaseDate;
    private String picture;
    private List<GenreType> genre;
    private List<MovieReview> movieReviews;
    private List<Integer> reviewersIds;
    private List<MovieRating> ratingList;
    private List<MediaPerson> crew;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public List<GenreType> getGenre() {
        return genre;
    }

    public void setGenre(List<GenreType> genre) {
        this.genre = genre;
    }

    public List<MovieReview> getMovieReviews() {
        return movieReviews;
    }

    public void setMovieReviews(List<MovieReview> movieReviews) {
        this.movieReviews = movieReviews;
    }

    public List<Integer> getReviewersIds() {
        return reviewersIds;
    }

    public void setReviewersIds(List<Integer> reviewersIds) {
        this.reviewersIds = reviewersIds;
    }

    public List<MovieRating> getRatingList() {
        return ratingList;
    }

    public void setRatingList(List<MovieRating> ratingList) {
        this.ratingList = ratingList;
    }

    public List<MediaPerson> getCrew() {
        return crew;
    }

    public void setCrew(List<MediaPerson> crew) {
        this.crew = crew;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Movie{");
        sb.append("id=").append(id);
        sb.append(", title='").append(title).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
