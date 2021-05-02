package com.epam.project.model.entity;

import java.time.LocalDate;
import java.util.List;

/**
 * The type Movie.
 */
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

    /**
     * Instantiates a new Movie.
     */
    public Movie() {}

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets title.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets description.
     *
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets average rating.
     *
     * @return the average rating
     */
    public float getAverageRating() {
        return averageRating;
    }

    /**
     * Sets average rating.
     *
     * @param averageRating the average rating
     */
    public void setAverageRating(float averageRating) {
        this.averageRating = averageRating;
    }

    /**
     * Gets release date.
     *
     * @return the release date
     */
    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    /**
     * Sets release date.
     *
     * @param releaseDate the release date
     */
    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    /**
     * Gets picture.
     *
     * @return the picture
     */
    public String getPicture() {
        return picture;
    }

    /**
     * Sets picture.
     *
     * @param picture the picture
     */
    public void setPicture(String picture) {
        this.picture = picture;
    }

    /**
     * Gets genres.
     *
     * @return the genres
     */
    public List<GenreType> getGenres() {
        return genres;
    }

    /**
     * Sets genres.
     *
     * @param genres the genres
     */
    public void setGenres(List<GenreType> genres) {
        this.genres = genres;
    }

    /**
     * Gets reviews.
     *
     * @return the reviews
     */
    public List<MovieReview> getReviews() {
        return reviews;
    }

    /**
     * Sets reviews.
     *
     * @param reviews the reviews
     */
    public void setReviews(List<MovieReview> reviews) {
        this.reviews = reviews;
    }

    /**
     * Gets reviewers ids.
     *
     * @return the reviewers ids
     */
    public List<Integer> getReviewersIds() {
        return reviewersIds;
    }

    /**
     * Sets reviewers ids.
     *
     * @param reviewersIds the reviewers ids
     */
    public void setReviewersIds(List<Integer> reviewersIds) {
        this.reviewersIds = reviewersIds;
    }

    /**
     * Gets rating list.
     *
     * @return the rating list
     */
    public List<MovieRating> getRatingList() {
        return ratingList;
    }

    /**
     * Sets rating list.
     *
     * @param ratingList the rating list
     */
    public void setRatingList(List<MovieRating> ratingList) {
        this.ratingList = ratingList;
    }

    /**
     * Gets crew.
     *
     * @return the crew
     */
    public List<MediaPerson> getCrew() {
        return crew;
    }

    /**
     * Sets crew.
     *
     * @param crew the crew
     */
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
