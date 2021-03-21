package com.example.demo_web.entity;

public class MovieRating extends Entity {
    private int id;
    private float value;
    private int movieId;
    private int userId;
    private String movieTitle;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
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
    public String toString() {
        final StringBuilder sb = new StringBuilder("MovieRating{");
        sb.append("id=").append(id);
        sb.append(", value=").append(value);
        sb.append('}');
        return sb.toString();
    }
}
