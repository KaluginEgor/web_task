package com.example.demo_web.controller.command;


import com.example.demo_web.controller.command.impl.page.OpenAllMoviesPageCommand;
import com.example.demo_web.controller.command.impl.LoginCommand;
import com.example.demo_web.controller.command.impl.admin.CreateMediaPersonCommand;
import com.example.demo_web.controller.command.impl.admin.OpenEditMediaPersonPageCommand;
import com.example.demo_web.controller.command.impl.admin.OpenEditMoviePageCommand;
import com.example.demo_web.controller.command.impl.admin.UpdateMediaPersonCommand;
import com.example.demo_web.controller.command.impl.page.*;
import com.example.demo_web.controller.command.impl.user.CreateMovieRatingCommand;
import com.example.demo_web.controller.command.impl.user.CreateMovieReviewCommand;
import com.example.demo_web.controller.command.impl.user.UpdateMovieRatingCommand;
import com.example.demo_web.controller.command.impl.*;
import com.example.demo_web.controller.command.impl.user.UpdateMovieReviewCommand;

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
    SHOW_ALL_MOVIES {
        {
            this.command = new OpenAllMoviesPageCommand();
        }
    },
    SHOW_ALL_MEDIA_PERSONS {
        {
            this.command = new OpenAllMediaPersonsPageCommand();
        }
    },
    SHOW_MEDIA_PERSON_PAGE {
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
    CREATE_MOVIE_REVIEW {
        {
            this.command = new CreateMovieReviewCommand();
        }
    },
    UPDATE_MOVIE_REVIEW {
        {
            this.command = new UpdateMovieReviewCommand();
        }
    },
    OPEN_EDIT_MOVIE_PAGE {
        {
            this.command = new OpenEditMoviePageCommand();
        }
    };
    ActionCommand command;
    public ActionCommand getCurrentCommand() {
        return command;
    }
}
