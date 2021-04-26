package com.example.demo_web.controller.command;

public class PagePath {
    public static final String INDEX = "/index.jsp";

    public static final String LOGIN = "/pages/guest/login.jsp";
    public static final String REGISTRATION = "/pages/guest/registration.jsp";

    public static final String MAIN = "/pages/common/main.jsp";
    public static final String MOVIE = "/pages/common/movie.jsp";
    public static final String MEDIA_PERSON = "/pages/common/media_person.jsp";
    public static final String ALL_MOVIES = "/pages/common/all_movies.jsp";
    public static final String ALL_MEDIA_PERSONS = "/pages/common/all_media_persons.jsp";
    public static final String USER = "/pages/common/user.jsp";

    public static final String EDIT_USER = "/pages/user/edit_user.jsp";

    public static final String EDIT_MEDIA_PERSON = "/pages/admin/edit_media_person.jsp";
    public static final String EDIT_MOVIE = "/pages/admin/edit_movie.jsp";
    public static final String ALL_USERS = "/pages/admin/all_users.jsp";

    public static final String ERROR_404 = "/pages/error/404.jsp";
    public static final String ERROR_500 = "/pages/error/500.jsp";

    public static final String ADMIN_URL_PART = "pages/admin";
    public static final String GUEST_URL_PART = "pages/guest";
    public static final String USER_URL_PART = "pages/user";
    public static final String ERROR_URL_PART = "pages/error";
    public static final String MODULE_URL_PART = "pages/module";

    private PagePath() {}
}
