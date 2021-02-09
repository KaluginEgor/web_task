package com.example.demo_web.entity;

public enum UserRole {
    ADMIN(1),
    USER(2),
    GUEST(3);

    private int value;

    private UserRole(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
