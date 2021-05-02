package com.epam.project.model.entity;

import java.time.LocalDate;

public class MovieReview extends Entity {
    private int id;
    private String title;
    private String body;
    private LocalDate creationDate;
    private int movieId;
    private int userId;
    private String userLogin;
    private String movieTitle;

    public MovieReview() {}

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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

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
