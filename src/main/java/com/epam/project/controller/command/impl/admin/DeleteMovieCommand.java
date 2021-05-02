package com.epam.project.controller.command.impl.admin;

import com.epam.project.controller.command.*;
import com.epam.project.exception.CommandException;
import com.epam.project.exception.ServiceException;
import com.epam.project.model.service.MovieService;
import com.epam.project.model.service.impl.MovieServiceImpl;
import com.epam.project.model.util.message.ErrorMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class DeleteMovieCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger();
    private MovieService movieService = MovieServiceImpl.getInstance();

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) throws CommandException {
        CommandResult commandResult = new CommandResult(PagePath.MAIN, TransitionType.REDIRECT);
        String stringMovieId = sessionRequestContent.getRequestParameter(RequestParameter.MOVIE_ID);
        if (stringMovieId == null) {
            sessionRequestContent.setSessionAttribute(Attribute.ERROR_MESSAGE, ErrorMessage.EMPTY_DELETE_MOVIE_PARAMETERS);
        } else {
            Optional<String> errorMessage;
            try {
                errorMessage = movieService.delete(stringMovieId);
                if (errorMessage.isPresent()) {
                    sessionRequestContent.setSessionAttribute(Attribute.ERROR_MESSAGE, errorMessage.get());
                }
            } catch (ServiceException e) {
                logger.error(e);
                throw new CommandException(e);
            }
        }
        return commandResult;
    }
}
