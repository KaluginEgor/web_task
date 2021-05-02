package com.epam.project.model.validator;

import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

public class UserValidatorTest {

    @Test
    public void testIsValidId() {
        String id = "10";
        boolean actualResult = UserValidator.isValidId(id);
        assertTrue(actualResult);
    }

    @Test
    public void testIsValidLogin() {
        String login = "login_example";
        boolean actualResult = UserValidator.isValidLogin(login);
        assertTrue(actualResult);
    }

    @Test
    public void testIsValidName() {
        String name = "John";
        boolean actualResult = UserValidator.isValidName(name);
        assertTrue(actualResult);
    }

    @Test
    public void testIsValidEmail() {
        String email = "ivan.ivanovich@gmail.com";
        boolean actualResult = UserValidator.isValidEmail(email);
        assertTrue(actualResult);
    }

    @Test
    public void testIsPictureValid() {
        String picture = "picture.jpg";
        boolean actualResult = UserValidator.isPictureValid(picture);
        assertTrue(actualResult);
    }

    @Test
    public void testIsValidPassword() {
        String password = "password12345678";
        boolean actualResult = UserValidator.isValidPassword(password);
        assertTrue(actualResult);
    }
}