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

public enum CommandName {
    LOGIN(new LoginCommand()) {
        {
            setAllowedUserRoles(UserRole.values());
        }
    },
    LOGOUT(new LogoutCommand()) {
        {
            setAllowedUserRoles(UserRole.values());
        }
    },
    REGISTER(new RegisterCommand()) {
        {
            setAllowedUserRoles(UserRole.GUEST);
        }
    },
    OPEN_REGISTRATION_PAGE(new OpenRegistrationPageCommand()) {
        {
            setAllowedUserRoles(UserRole.GUEST);
        }
    },
    OPEN_LOGIN_PAGE(new OpenLoginPageCommand()) {
        {
            setAllowedUserRoles(UserRole.GUEST);
        }
    },
    CHANGE_LANGUAGE(new ChangeLocaleCommand()) {
        {
            setAllowedUserRoles(UserRole.values());
        }
    },
    CONFIRM_REGISTRATION(new ConfirmRegistrationCommand()) {
        {
            setAllowedUserRoles(UserRole.GUEST);
        }
    },
    OPEN_ALL_MOVIES_PAGE(new OpenAllMoviesPageCommand()) {
        {
            setAllowedUserRoles(UserRole.values());
        }
    },
    OPEN_ALL_MEDIA_PERSONS_PAGE(new OpenAllMediaPersonsPageCommand()) {
        {
            setAllowedUserRoles(UserRole.values());
        }
    },
    OPEN_ALL_USERS_PAGE(new OpenAllUsersPageCommand()) {
        {
            setAllowedUserRoles(UserRole.ADMIN);
        }
    },
    OPEN_MEDIA_PERSON_PAGE(new OpenMediaPersonPageCommand()) {
        {
            setAllowedUserRoles(UserRole.values());
        }
    },
    OPEN_MOVIE_PAGE(new OpenMoviePageCommand()) {
        {
            setAllowedUserRoles(UserRole.values());
        }
    },
    OPEN_EDIT_MEDIA_PERSON_PAGE(new OpenEditMediaPersonPageCommand()) {
        {
            setAllowedUserRoles(UserRole.ADMIN);
        }
    },
    UPDATE_MEDIA_PERSON(new UpdateMediaPersonCommand()) {
        {
            setAllowedUserRoles(UserRole.ADMIN);
        }
    },
    CREATE_MEDIA_PERSON(new CreateMediaPersonCommand()) {
        {
            setAllowedUserRoles(UserRole.ADMIN);
        }
    },
    DELETE_MEDIA_PERSON(new DeleteMediaPersonCommand()) {
        {
            setAllowedUserRoles(UserRole.ADMIN);
        }
    },
    CREATE_MOVIE(new CreateMovieCommand()) {
        {
            setAllowedUserRoles(UserRole.ADMIN);
        }
    },
    UPDATE_MOVIE(new UpdateMovieCommand()) {
        {
            setAllowedUserRoles(UserRole.ADMIN);
        }
    },
    DELETE_MOVIE(new DeleteMovieCommand()) {
        {
            setAllowedUserRoles(UserRole.ADMIN);
        }
    },
    CREATE_MOVIE_RATING(new CreateMovieRatingCommand()) {
        {
            setAllowedUserRoles(UserRole.ADMIN, UserRole.USER);
        }
    },
    UPDATE_MOVIE_RATING(new UpdateMovieRatingCommand()) {
        {
            setAllowedUserRoles(UserRole.ADMIN, UserRole.USER);
        }
    },
    DELETE_MOVIE_RATING(new DeleteMovieRatingCommand()) {
        {
            setAllowedUserRoles(UserRole.ADMIN, UserRole.USER);
        }
    },
    CREATE_MOVIE_REVIEW(new CreateMovieReviewCommand()) {
        {
            setAllowedUserRoles(UserRole.ADMIN, UserRole.USER);
        }
    },
    PREPARE_MOVIE_REVIEW_UPDATE(new PrepareMovieReviewUpdateCommand()) {
        {
            setAllowedUserRoles(UserRole.ADMIN, UserRole.USER);
        }
    },
    UPDATE_MOVIE_REVIEW(new UpdateMovieReviewCommand()) {
        {
            setAllowedUserRoles(UserRole.ADMIN, UserRole.USER);
        }
    },
    DELETE_MOVIE_REVIEW(new DeleteMovieReviewCommand()) {
        {
            setAllowedUserRoles(UserRole.ADMIN, UserRole.USER);
        }
    },
    OPEN_EDIT_MOVIE_PAGE(new OpenEditMoviePageCommand()) {
        {
            setAllowedUserRoles(UserRole.ADMIN);
        }
    },
    OPEN_USER_PAGE(new OpenUserPageCommand()) {
        {
            setAllowedUserRoles(UserRole.values());
        }
    },
    OPEN_EDIT_USER_PAGE(new OpenEditUserPageCommand()) {
        {
            setAllowedUserRoles(UserRole.ADMIN, UserRole.USER);
        }
    },
    UPDATE_USER(new UpdateUserCommand()) {
        {
            setAllowedUserRoles(UserRole.ADMIN, UserRole.USER);
        }
    },
    UPLOAD_PICTURE(new UploadPictureCommand()) {
        {
            setAllowedUserRoles(UserRole.ADMIN, UserRole.USER);
        }
    },
    FIND_MOVIES_BY_TITLE(new FindMoviesByTitleCommand()) {
        {
            setAllowedUserRoles(UserRole.values());
        }
    },
    DELETE_USER(new DeleteUserCommand()) {
        {
            setAllowedUserRoles(UserRole.ADMIN, UserRole.USER);
        }
    },
    BLOCK_USER(new BlockUserCommand()) {
        {
            setAllowedUserRoles(UserRole.ADMIN);
        }
    },
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

    public void setAllowedUserRoles(UserRole... userRoles) {
        allowedUserRoles.addAll(List.of(userRoles));
    }

    public boolean isRoleAllowed(UserRole userRole) {
        return allowedUserRoles.contains(userRole);
    }

    public ActionCommand getCurrentCommand() {
        return command;
    }
}
