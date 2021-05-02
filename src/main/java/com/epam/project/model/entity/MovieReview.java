package com.epam.project.model.entity;

import java.time.LocalDate;

/**
 * The type Movie review.
 */
public class MovieReview extends Entity {
    private int id;
    private String title;
    private String body;
    private LocalDate creationDate;
    private int movieId;
    private int userId;
    private String userLogin;
    private String movieTitle;

    /**
     * Instantiates a new Movie review.
     */
    public MovieReview() {}

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
     * Gets body.
     *
     * @return the body
     */
    public String getBody() {
        return body;
    }

    /**
     * Sets body.
     *
     * @param body the body
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * Gets creation date.
     *
     * @return the creation date
     */
    public LocalDate getCreationDate() {
        return creationDate;
    }

    /**
     * Sets creation date.
     *
     * @param creationDate the creation date
     */
    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Gets movie id.
     *
     * @return the movie id
     */
    public int getMovieId() {
        return movieId;
    }

    /**
     * Sets movie id.
     *
     * @param movieId the movie id
     */
    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    /**
     * Gets user id.
     *
     * @return the user id
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets user id.
     *
     * @param userId the user id
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Gets user login.
     *
     * @return the user login
     */
    public String getUserLogin() {
        return userLogin;
    }

    /**
     * Sets user login.
     *
     * @param userLogin the user login
     */
    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    /**
     * Gets movie title.
     *
     * @return the movie title
     */
    public String getMovieTitle() {
        return movieTitle;
    }

    /**
     * Sets movie title.
     *
     * @param movieTitle the movie title
     */
    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MovieReview that = (MovieReview) o;

        if (id != that.id) return false;
        if (movieId != that.movieId) return false;
        if (userId != that.userId) return false;
        if (!title.equals(that.title)) return false;
        if (!body.equals(that.body)) return false;
        return creationDate.equals(that.creationDate);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + title.hashCode();
        result = 31 * result + body.hashCode();
        result = 31 * result + creationDate.hashCode();
        result = 31 * result + movieId;
        result = 31 * result + userId;
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MovieReview{");
        sb.append("id=").append(id);
        sb.append(", title='").append(title).append('\'');
        sb.append(", body='").append(body).append('\'');
        sb.append(", creationDate=").append(creationDate);
        sb.append('}');
        return sb.toString();
    }
}
