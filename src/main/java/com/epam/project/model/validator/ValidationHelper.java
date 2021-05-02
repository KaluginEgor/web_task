package com.epam.project.model.validator;

import com.epam.project.controller.command.Attribute;
import com.epam.project.controller.command.RequestParameter;
import com.epam.project.model.util.message.ErrorMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ValidationHelper {

    public static List<String> defineValidParameters(Map<String, Boolean> map) {
        return map.entrySet().stream().filter(entry -> Boolean.TRUE.equals(entry.getValue())).map(Map.Entry::getKey).collect(Collectors.toList());
    }

    public static List<String> defineValidationErrorMessages(Map<String, Boolean> map) {
        List<String> errorMessages = new ArrayList<>();
        map.entrySet().stream().filter(entry -> Boolean.FALSE.equals(entry.getValue())).forEach(entry ->
                errorMessages.add(defineErrorValidationMessage(entry)));
        return errorMessages;
    }

    public static String defineErrorValidationMessage(Map.Entry<String, Boolean> entry) {
        String message = switch (entry.getKey()) {
            case RequestParameter.FIRST_NAME -> ErrorMessage.NOT_VALID_FIRST_NAME;
            case RequestParameter.SECOND_NAME -> ErrorMessage.NOT_VALID_SECOND_NAME;
            case RequestParameter.BIO -> ErrorMessage.NOT_VALID_BIO;
            case RequestParameter.MEDIA_PERSON_OCCUPATION_TYPE -> ErrorMessage.NOT_VALID_OCCUPATION;
            case RequestParameter.MEDIA_PERSON_BIRTHDAY -> ErrorMessage.NOT_VALID_BIRTHDAY;
            case RequestParameter.PICTURE -> ErrorMessage.NOT_VALID_PICTURE;
            case RequestParameter.MEDIA_PERSON_MOVIES -> ErrorMessage.NOT_VALID_MOVIES_ID;
            case Attribute.MOVIE_ID -> ErrorMessage.NOT_VALID_MOVIE_ID;
            case Attribute.USER_ID -> ErrorMessage.NOT_VALID_USER_ID;
            case Attribute.MOVIE_RATING_VALUE -> ErrorMessage.NOT_VALID_RATING_VALUE;
            case Attribute.MOVIE_TITLE -> ErrorMessage.NOT_VALID_TITLE;
            case Attribute.MOVIE_DESCRIPTION -> ErrorMessage.NOT_VALID_DESCRIPTION;
            case Attribute.MOVIE_RELEASE_DATE -> ErrorMessage.NOT_VALID_RELEASE_DATE;
            case Attribute.MOVIE_GENRE -> ErrorMessage.NOT_VALID_GENRES_ID;
            case Attribute.MOVIE_CREW -> ErrorMessage.NOT_VALID_MEDIA_PERSONS_ID;
            case Attribute.MOVIE_REVIEW_TITLE -> ErrorMessage.NOT_VALID_MOVIE_REVIEW_TITLE;
            case Attribute.MOVIE_REVIEW_BODY -> ErrorMessage.NOT_VALID_MOVIE_REVIEW_BODY;
            case Attribute.EMAIL -> ErrorMessage.NOT_VALID_EMAIL;
            case Attribute.LOGIN -> ErrorMessage.NOT_VALID_LOGIN;
            case Attribute.PASSWORD -> ErrorMessage.INCORRECT_PASSWORD;
            default -> null;
        };
        return message;
    }

    private ValidationHelper() {}
}
