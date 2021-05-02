package com.epam.project.model.entity;

public class MovieRating extends Entity {
    private int id;
    private int value;
    private int movieId;
    private int userId;
    private String movieTitle;

    public MovieRating() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
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

        MovieRating that = (MovieRating) o;

        if (id != that.id) return false;
        if (value != that.value) return false;
        if (movieId != that.movieId) return false;
        return userId == that.userId;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + value;
        result = 31 * result + movieId;
        result = 31 * result + userId;
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MovieRating{");
        sb.append("id=").append(id);
        sb.append(", value=").append(value);
        sb.append('}');
        return sb.toString();
    }
}
