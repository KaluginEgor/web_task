package com.example.demo_web.command;

import com.example.demo_web.command.impl.*;

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
    CHANGE_LANGUAGE {
        {
            this.command = new ChangeLocaleCommand();
        }
    },
    CONFIRM_REGISTRATION {
        {
            this.command = new ConfirmRegistrationCommand();
        }
    };
    ActionCommand command;
    public ActionCommand getCurrentCommand() {
        return command;
    }
}
