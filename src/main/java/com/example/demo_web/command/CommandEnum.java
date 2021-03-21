package com.example.demo_web.command;

import com.example.demo_web.command.impl.*;
import com.example.demo_web.command.impl.ShowAllMoviesCommand;
import com.example.demo_web.command.impl.LoginCommand;
import com.example.demo_web.command.impl.admin.OpenEditMediaPersonPageCommand;
import com.example.demo_web.command.impl.admin.UpdateMediaPersonCommand;
import com.example.demo_web.command.impl.page.OpenLoginPageCommand;
import com.example.demo_web.command.impl.page.OpenRegistrationPageCommand;

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
            this.command = new ShowAllMoviesCommand();
        }
    },
    SHOW_ALL_MEDIA_PERSONS {
        {
            this.command = new ShowAllMediaPersonsCommand();
        }
    },
    SHOW_MEDIA_PERSON_PAGE {
        {
            this.command = new ShowMediaPersonPageCommand();
        }
    },
    SHOW_MOVIE_PAGE {
        {
            this.command = new ShowMoviePageCommand();
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
    };
    ActionCommand command;
    public ActionCommand getCurrentCommand() {
        return command;
    }
}
