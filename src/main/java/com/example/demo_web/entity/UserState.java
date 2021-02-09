package com.example.demo_web.entity;

import java.util.Arrays;
import java.util.Optional;

public enum UserState {
    ACTIVE(1),
    INACTIVE(2),
    BLOCKED(3),
    DELETED(3);

    private int value;

    private UserState(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
