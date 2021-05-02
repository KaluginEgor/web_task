package com.epam.project.model.util.mail;

import com.epam.project.model.entity.User;

import java.util.Locale;
import java.util.ResourceBundle;

public class MailBuilder {
    private static final String EMAIL_SUBJECT = "email.subject";
    private static final String EMAIL_BODY = "email.body";
    private static final String RESOURCE_NAME = "property/pagecontent";
    private static final Locale DEFAULT_LOCALE = new Locale("en_US");

    private static final String LINK_FOR_CONFIRMATION = "http://localhost:8080/web_project_war_exploded" +
            "/controller?command=confirm_registration&id=";

    private MailBuilder() {}

    private static String getLocalizedMessageFromResources(String language, String key) {
        Locale locale;
        if (language != null) {
            locale = new Locale(language);
        } else {
            locale = DEFAULT_LOCALE;
        }
        ResourceBundle resourceBundle = ResourceBundle.getBundle(RESOURCE_NAME, locale);
        return resourceBundle.getString(key);
    }

    public static String buildEmailSubject(String locale) {
        String emailSubject = getLocalizedMessageFromResources(locale, EMAIL_SUBJECT);
        return emailSubject;
    }

    public static String buildEmailBody(User user, String locale) {
        String emailBody = String.format(getLocalizedMessageFromResources(locale, EMAIL_BODY),
                user.getFirstName(), LINK_FOR_CONFIRMATION + user.getId());
        return emailBody;
    }
}
