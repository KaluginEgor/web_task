package com.example.demo_web.controller.command;


import com.example.demo_web.controller.command.impl.admin.*;
import com.example.demo_web.controller.command.impl.page.OpenAllMoviesPageCommand;
import com.example.demo_web.controller.command.impl.user.LoginCommand;
import com.example.demo_web.controller.command.impl.page.*;
import com.example.demo_web.controller.command.impl.user.*;
import com.example.demo_web.controller.command.impl.*;

public enum CommandEnum {
    LOGIN {
        {
            this.command = new LoginCommand();
        }
    },
    LOGOUT {
        {
            this.command = new LogoutCommand();
        }
    },
    REGISTER {
        {
            this.command = new RegisterCommand();
        }
    },
    OPEN_REGISTRATION_PAGE {
        {
            this.command = new OpenRegistrationPageCommand();
        }
    },
    OPEN_LOGIN_PAGE {
        {
            this.command = new OpenLoginPageCommand();
        }
    },
    CHANGE_LANGUAGE {
        {
            this.command = new ChangeLocaleCommand();
        }
    },
    CONFIRM_REGISTRATION {
        {
            this.command = new ConfirmRegistrationCommand();
        }
    },
    OPEN_ALL_MOVIES_PAGE {
        {
            this.command = new OpenAllMoviesPageCommand();
        }
    },
    OPEN_ALL_MEDIA_PERSONS_PAGE {
        {
            this.command = new OpenAllMediaPersonsPageCommand();
        }
    },
    OPEN_MEDIA_PERSON_PAGE {
        {
            this.command = new OpenMediaPersonPageCommand();
        }
    },
    OPEN_MOVIE_PAGE {
        {
            this.command = new OpenMoviePageCommand();
        }
    },
    OPEN_EDIT_MEDIA_PERSON_PAGE {
        {
            this.command = new OpenEditMediaPersonPageCommand();
        }
    },
    UPDATE_MEDIA_PERSON {
        {
            this.command = new UpdateMediaPersonCommand();
        }
    },
    CREATE_MEDIA_PERSON {
        {
            this.command = new CreateMediaPersonCommand();
        }
    },
    DELETE_MEDIA_PERSON {
        {
            this.command = new DeleteMediaPersonCommand();
        }
    },
    CREATE_MOVIE {
        {
            this.command = new CreateMovieCommand();
        }
    },
    UPDATE_MOVIE {
        {
            this.command = new UpdateMovieCommand();
        }
    },
    DELETE_MOVIE {
        {
            this.command = new DeleteMovieCommand();
        }
    },
    CREATE_MOVIE_RATING {
        {
            this.command = new CreateMovieRatingCommand();
        }
    },
    UPDATE_MOVIE_RATING {
        {
            this.command = new UpdateMovieRatingCommand();
        }
    },
    DELETE_MOVIE_RATING {
        {
            this.command = new DeleteMovieRatingCommand();
        }
    },
    CREATE_MOVIE_REVIEW {
        {
            this.command = new CreateMovieReviewCommand();
        }
    },
    PREPARE_MOVIE_REVIEW_UPDATE {
        {
            this.command = new PrepareMovieReviewUpdateCommand();
        }
    },
    UPDATE_MOVIE_REVIEW {
        {
            this.command = new UpdateMovieReviewCommand();
        }
    },
    DELETE_MOVIE_REVIEW {
        {
            this.command = new DeleteMovieReviewCommand();
        }
    },
    OPEN_EDIT_MOVIE_PAGE {
        {
            this.command = new OpenEditMoviePageCommand();
        }
    },
    OPEN_USER_PAGE {
        {
            this.command = new OpenUserPageCommand();
        }
    },
    OPEN_EDIT_USER_PAGE {
        {
            this.command = new OpenEditUserPageCommand();
        }
    },
    UPDATE_USER {
        {
            this.command = new UpdateUserCommand();
        }
    },
    UPLOAD_PICTURE {
        {
            this.command = new UploadPictureCommand();
        }
    },
    FIND_MOVIES_BY_TITLE {
        {
            this.command = new FindMoviesByTitleCommand();
        }
    };
    ActionCommand command;
    public ActionCommand getCurrentCommand() {
        return command;
    }
}
