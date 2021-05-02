package com.epam.project.model.entity;

import java.time.LocalDate;
import java.util.List;

public class Movie extends Entity {
    private int id;
    private String title;
    private String description;
    private float averageRating;
    private LocalDate releaseDate;
    private String picture;
    private List<GenreType> genres;
    private List<MovieReview> reviews;
    private List<MovieRating> ratingList;
    private List<MediaPerson> crew;
    private List<Integer> reviewersIds;

    public Movie() {}

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

    public float getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(float averageRating) {
        this.averageRating = averageRating;
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

    public List<GenreType> getGenres() {
        return genres;
    }

    public void setGenres(List<GenreType> genres) {
        this.genres = genres;
    }

    public List<MovieReview> getReviews() {
        return reviews;
    }

    public void setReviews(List<MovieReview> reviews) {
        this.reviews = reviews;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Movie movie = (Movie) o;

        if (id != movie.id) return false;
        if (Float.compare(movie.averageRating, averageRating) != 0) return false;
        if (!title.equals(movie.title)) return false;
        if (!description.equals(movie.description)) return false;
        if (!releaseDate.equals(movie.releaseDate)) return false;
        return picture.equals(movie.picture);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + title.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + (averageRating != +0.0f ? Float.floatToIntBits(averageRating) : 0);
        result = 31 * result + releaseDate.hashCode();
        result = 31 * result + picture.hashCode();
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Movie{");
        sb.append("id=").append(id);
        sb.append(", title='").append(title).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", averageRating=").append(averageRating);
        sb.append(", releaseDate=").append(releaseDate);
        sb.append(", picture='").append(picture).append('\'');
        sb.append(", genres=").append(genres);
        sb.append(", reviews=").append(reviews);
        sb.append(", ratingList=").append(ratingList);
        sb.append(", crew=").append(crew);
        sb.append(", reviewersIds=").append(reviewersIds);
        sb.append('}');
        return sb.toString();
    }
}
