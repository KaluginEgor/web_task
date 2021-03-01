package com.example.demo_web.command;

import com.example.demo_web.command.impl.*;
import com.example.demo_web.command.impl.ShowAllMoviesCommand;
import com.example.demo_web.command.impl.LoginCommand;
import com.example.demo_web.command.impl.page.LoginPageCommand;
import com.example.demo_web.command.impl.page.RegistrationPageCommand;

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
    REGISTRATION_PAGE {
        {
            this.command = new RegistrationPageCommand();
        }
    },
    LOGIN_PAGE {
        {
            this.command = new LoginPageCommand();
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
    SHOW_ALL_ACTORS {
        {
            this.command = new ShowAllActorsCommand();
        }
    };
    ActionCommand command;
    public ActionCommand getCurrentCommand() {
        return command;
    }
}
