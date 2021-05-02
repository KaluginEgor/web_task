package com.epam.project.model.entity;

/**
 * The type Movie rating.
 */
public class MovieRating extends Entity {
    private int id;
    private int value;
    private int movieId;
    private int userId;
    private String movieTitle;

    /**
     * Instantiates a new Movie rating.
     */
    public MovieRating() {}

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
     * Gets value.
     *
     * @return the value
     */
    public int getValue() {
        return value;
    }

    /**
     * Sets value.
     *
     * @param value the value
     */
    public void setValue(int value) {
        this.value = value;
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
