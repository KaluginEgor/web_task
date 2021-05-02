package com.epam.project.controller.command;


import com.epam.project.controller.command.impl.admin.*;
import com.epam.project.controller.command.impl.common.ChangeLocaleCommand;
import com.epam.project.controller.command.impl.common.FindMoviesByTitleCommand;
import com.epam.project.controller.command.impl.common.page.*;
import com.epam.project.controller.command.impl.guest.LoginCommand;
import com.epam.project.controller.command.impl.guest.RegisterCommand;
import com.epam.project.controller.command.impl.user.*;
import com.epam.project.model.entity.UserRole;

import java.util.EnumSet;
import java.util.List;

/**
 * The enum Command name.
 */
public enum CommandName {
    /**
     * The Login.
     */
    LOGIN(new LoginCommand()) {
        {
            setAllowedUserRoles(UserRole.GUEST);
        }
    },
    /**
     * The Logout.
     */
    LOGOUT(new LogoutCommand()) {
        {
            setAllowedUserRoles(UserRole.ADMIN, UserRole.USER);
        }
    },
    /**
     * The Register.
     */
    REGISTER(new RegisterCommand()) {
        {
            setAllowedUserRoles(UserRole.GUEST);
        }
    },
    /**
     * The Open registration page.
     */
    OPEN_REGISTRATION_PAGE(new OpenRegistrationPageCommand()) {
        {
            setAllowedUserRoles(UserRole.GUEST);
        }
    },
    /**
     * The Open login page.
     */
    OPEN_LOGIN_PAGE(new OpenLoginPageCommand()) {
        {
            setAllowedUserRoles(UserRole.GUEST);
        }
    },
    /**
     * The Change language.
     */
    CHANGE_LANGUAGE(new ChangeLocaleCommand()) {
        {
            setAllowedUserRoles(UserRole.values());
        }
    },
    /**
     * The Confirm registration.
     */
    CONFIRM_REGISTRATION(new ConfirmRegistrationCommand()) {
        {
            setAllowedUserRoles(UserRole.GUEST);
        }
    },
    /**
     * The Open all movies page.
     */
    OPEN_ALL_MOVIES_PAGE(new OpenAllMoviesPageCommand()) {
        {
            setAllowedUserRoles(UserRole.values());
        }
    },
    /**
     * The Open all media persons page.
     */
    OPEN_ALL_MEDIA_PERSONS_PAGE(new OpenAllMediaPersonsPageCommand()) {
        {
            setAllowedUserRoles(UserRole.values());
        }
    },
    /**
     * The Open all users page.
     */
    OPEN_ALL_USERS_PAGE(new OpenAllUsersPageCommand()) {
        {
            setAllowedUserRoles(UserRole.ADMIN);
        }
    },
    /**
     * The Open media person page.
     */
    OPEN_MEDIA_PERSON_PAGE(new OpenMediaPersonPageCommand()) {
        {
            setAllowedUserRoles(UserRole.values());
        }
    },
    /**
     * The Open movie page.
     */
    OPEN_MOVIE_PAGE(new OpenMoviePageCommand()) {
        {
            setAllowedUserRoles(UserRole.values());
        }
    },
    /**
     * The Open edit media person page.
     */
    OPEN_EDIT_MEDIA_PERSON_PAGE(new OpenEditMediaPersonPageCommand()) {
        {
            setAllowedUserRoles(UserRole.ADMIN);
        }
    },
    /**
     * The Update media person.
     */
    UPDATE_MEDIA_PERSON(new UpdateMediaPersonCommand()) {
        {
            setAllowedUserRoles(UserRole.ADMIN);
        }
    },
    /**
     * The Create media person.
     */
    CREATE_MEDIA_PERSON(new CreateMediaPersonCommand()) {
        {
            setAllowedUserRoles(UserRole.ADMIN);
        }
    },
    /**
     * The Delete media person.
     */
    DELETE_MEDIA_PERSON(new DeleteMediaPersonCommand()) {
        {
            setAllowedUserRoles(UserRole.ADMIN);
        }
    },
    /**
     * The Create movie.
     */
    CREATE_MOVIE(new CreateMovieCommand()) {
        {
            setAllowedUserRoles(UserRole.ADMIN);
        }
    },
    /**
     * The Update movie.
     */
    UPDATE_MOVIE(new UpdateMovieCommand()) {
        {
            setAllowedUserRoles(UserRole.ADMIN);
        }
    },
    /**
     * The Delete movie.
     */
    DELETE_MOVIE(new DeleteMovieCommand()) {
        {
            setAllowedUserRoles(UserRole.ADMIN);
        }
    },
    /**
     * The Create movie rating.
     */
    CREATE_MOVIE_RATING(new CreateMovieRatingCommand()) {
        {
            setAllowedUserRoles(UserRole.ADMIN, UserRole.USER);
        }
    },
    /**
     * The Update movie rating.
     */
    UPDATE_MOVIE_RATING(new UpdateMovieRatingCommand()) {
        {
            setAllowedUserRoles(UserRole.ADMIN, UserRole.USER);
        }
    },
    /**
     * The Delete movie rating.
     */
    DELETE_MOVIE_RATING(new DeleteMovieRatingCommand()) {
        {
            setAllowedUserRoles(UserRole.ADMIN, UserRole.USER);
        }
    },
    /**
     * The Create movie review.
     */
    CREATE_MOVIE_REVIEW(new CreateMovieReviewCommand()) {
        {
            setAllowedUserRoles(UserRole.ADMIN, UserRole.USER);
        }
    },
    /**
     * The Prepare movie review update.
     */
    PREPARE_MOVIE_REVIEW_UPDATE(new PrepareMovieReviewUpdateCommand()) {
        {
            setAllowedUserRoles(UserRole.ADMIN, UserRole.USER);
        }
    },
    /**
     * The Update movie review.
     */
    UPDATE_MOVIE_REVIEW(new UpdateMovieReviewCommand()) {
        {
            setAllowedUserRoles(UserRole.ADMIN, UserRole.USER);
        }
    },
    /**
     * The Delete movie review.
     */
    DELETE_MOVIE_REVIEW(new DeleteMovieReviewCommand()) {
        {
            setAllowedUserRoles(UserRole.ADMIN, UserRole.USER);
        }
    },
    /**
     * The Open edit movie page.
     */
    OPEN_EDIT_MOVIE_PAGE(new OpenEditMoviePageCommand()) {
        {
            setAllowedUserRoles(UserRole.ADMIN);
        }
    },
    /**
     * The Open user page.
     */
    OPEN_USER_PAGE(new OpenUserPageCommand()) {
        {
            setAllowedUserRoles(UserRole.values());
        }
    },
    /**
     * The Open edit user page.
     */
    OPEN_EDIT_USER_PAGE(new OpenEditUserPageCommand()) {
        {
            setAllowedUserRoles(UserRole.ADMIN, UserRole.USER);
        }
    },
    /**
     * The Update user.
     */
    UPDATE_USER(new UpdateUserCommand()) {
        {
            setAllowedUserRoles(UserRole.ADMIN, UserRole.USER);
        }
    },
    /**
     * The Upload picture.
     */
    UPLOAD_PICTURE(new UploadPictureCommand()) {
        {
            setAllowedUserRoles(UserRole.ADMIN, UserRole.USER);
        }
    },
    /**
     * The Find movies by title.
     */
    FIND_MOVIES_BY_TITLE(new FindMoviesByTitleCommand()) {
        {
            setAllowedUserRoles(UserRole.values());
        }
    },
    /**
     * The Delete user.
     */
    DELETE_USER(new DeleteUserCommand()) {
        {
            setAllowedUserRoles(UserRole.ADMIN, UserRole.USER);
        }
    },
    /**
     * The Block user.
     */
    BLOCK_USER(new BlockUserCommand()) {
        {
            setAllowedUserRoles(UserRole.ADMIN);
        }
    },
    /**
     * The Activate user.
     */
    ACTIVATE_USER(new ActivateUserCommand()) {
        {
            setAllowedUserRoles(UserRole.ADMIN);
        }
    };
    private ActionCommand command;
    private EnumSet<UserRole> allowedUserRoles = EnumSet.noneOf(UserRole.class);

    CommandName(ActionCommand command) {
        this.command = command;
    }

    /**
     * Sets allowed user roles.
     *
     * @param userRoles the user roles
     */
    public void setAllowedUserRoles(UserRole... userRoles) {
        allowedUserRoles.addAll(List.of(userRoles));
    }

    /**
     * Is role allowed boolean.
     *
     * @param userRole the user role
     * @return the boolean
     */
    public boolean isRoleAllowed(UserRole userRole) {
        return allowedUserRoles.contains(userRole);
    }

    /**
     * Gets current command.
     *
     * @return the current command
     */
    public ActionCommand getCurrentCommand() {
        return command;
    }
}
