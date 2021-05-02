package com.epam.project.model.entity;

import java.time.LocalDate;
import java.util.List;

/**
 * The type Media person.
 */
public class MediaPerson extends Entity {
    private int id;
    private String firstName;
    private String secondName;
    private OccupationType occupationType;
    private String bio;
    private LocalDate birthday;
    private String picture;
    private List<Movie> movies;

    /**
     * Instantiates a new Media person.
     */
    public MediaPerson() {}

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
     * Gets first name.
     *
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets first name.
     *
     * @param firstName the first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets second name.
     *
     * @return the second name
     */
    public String getSecondName() {
        return secondName;
    }

    /**
     * Sets second name.
     *
     * @param secondName the second name
     */
    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    /**
     * Gets occupation type.
     *
     * @return the occupation type
     */
    public OccupationType getOccupationType() {
        return occupationType;
    }

    /**
     * Sets occupation type.
     *
     * @param occupationType the occupation type
     */
    public void setOccupationType(OccupationType occupationType) {
        this.occupationType = occupationType;
    }

    /**
     * Gets bio.
     *
     * @return the bio
     */
    public String getBio() {
        return bio;
    }

    /**
     * Sets bio.
     *
     * @param bio the bio
     */
    public void setBio(String bio) {
        this.bio = bio;
    }

    /**
     * Gets birthday.
     *
     * @return the birthday
     */
    public LocalDate getBirthday() {
        return birthday;
    }

    /**
     * Sets birthday.
     *
     * @param birthday the birthday
     */
    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
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
     * Gets movies.
     *
     * @return the movies
     */
    public List<Movie> getMovies() {
        return movies;
    }

    /**
     * Sets movies.
     *
     * @param movies the movies
     */
    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MediaPerson that = (MediaPerson) o;

        if (id != that.id) return false;
        if (!firstName.equals(that.firstName)) return false;
        if (!secondName.equals(that.secondName)) return false;
        if (occupationType != that.occupationType) return false;
        if (!birthday.equals(that.birthday)) return false;
        return picture.equals(that.picture);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + firstName.hashCode();
        result = 31 * result + secondName.hashCode();
        result = 31 * result + occupationType.hashCode();
        result = 31 * result + birthday.hashCode();
        result = 31 * result + picture.hashCode();
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MediaPerson{");
        sb.append("id=").append(id);
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", secondName='").append(secondName).append('\'');
        sb.append(", occupationType=").append(occupationType);
        sb.append(", bio='").append(bio).append('\'');
        sb.append(", birthday=").append(birthday);
        sb.append(", picture='").append(picture).append('\'');
        sb.append(", movies=").append(movies);
        sb.append('}');
        return sb.toString();
    }
}
