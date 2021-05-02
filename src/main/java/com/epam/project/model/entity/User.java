package com.epam.project.model.entity;

import java.util.List;

public class User extends Entity {
    private int id;
    private String login;
    private String email;
    private String firstName;
    private String secondName;
    private UserRole role;
    private UserState state;
    private String picture;
    private int rating;
    private List<MovieRating> movieRatings;
    private List<MovieReview> movieReviews;

    public User() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public UserState getState() {
        return state;
    }

    public void setState(UserState state) {
        this.state = state;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public List<MovieRating> getMovieRatings() {
        return movieRatings;
    }

    public void setMovieRatings(List<MovieRating> movieRatings) {
        this.movieRatings = movieRatings;
    }

    public List<MovieReview> getMovieReviews() {
        return movieReviews;
    }

    public void setMovieReviews(List<MovieReview> movieReviews) {
        this.movieReviews = movieReviews;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != user.id) return false;
        if (!login.equals(user.login)) return false;
        if (!email.equals(user.email)) return false;
        if (!firstName.equals(user.firstName)) return false;
        if (!secondName.equals(user.secondName)) return false;
        if (role != user.role) return false;
        if (state != user.state) return false;
        return picture.equals(user.picture);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + login.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + firstName.hashCode();
        result = 31 * result + secondName.hashCode();
        result = 31 * result + role.hashCode();
        result = 31 * result + state.hashCode();
        result = 31 * result + picture.hashCode();
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("login='").append(login).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", secondName='").append(secondName).append('\'');
        sb.append(", userRole=").append(role);
        sb.append(", userState=").append(state);
        sb.append('}');
        return sb.toString();
    }
}
