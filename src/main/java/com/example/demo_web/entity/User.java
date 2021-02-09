package com.example.demo_web.entity;

public class User extends Entity {
    private int id;
    private String activationKey;
    private String login;
    private String email;
    private String firstName;
    private String secondName;
    private UserRole userRole;
    private UserState userState;

    public User() {}

    public User(String login, String email, String firstName, String secondName) {
        this.login = login;
        this.email = email;
        this.firstName = firstName;
        this.secondName = secondName;
    }

    public User(int id, String login, String email, String firstName, String secondName, UserRole userRole, UserState userState) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.firstName = firstName;
        this.secondName = secondName;
        this.userRole = userRole;
        this.userState = userState;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getActivationKey() {
        return activationKey;
    }

    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
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

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public UserState getUserState() {
        return userState;
    }

    public void setUserState(UserState userState) {
        this.userState = userState;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("login='").append(login).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", secondName='").append(secondName).append('\'');
        sb.append(", userRole=").append(userRole);
        sb.append(", userState=").append(userState);
        sb.append('}');
        return sb.toString();
    }
}
