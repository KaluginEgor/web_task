package com.epam.project.controller.command.impl.common.page;


import com.epam.project.controller.command.*;
import com.epam.project.exception.CommandException;
import com.epam.project.exception.ServiceException;
import com.epam.project.model.entity.Movie;
import com.epam.project.model.service.MovieService;
import com.epam.project.model.service.impl.MovieServiceImpl;
import com.epam.project.model.util.message.FriendlyMessage;
import com.epam.project.tag.ViewAllMoviesTag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class OpenAllMoviesPageCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger();
    private MovieService movieService = MovieServiceImpl.getInstance();

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) throws CommandException {
        CommandResult commandResult = new CommandResult(PagePath.ALL_MOVIES, TransitionType.REDIRECT);

        Integer currentTablePage = (Integer) sessionRequestContent.getSessionAttribute(Attribute.ALL_MOVIES_CURRENT_PAGE);
        Optional<String> currentPage = Optional.ofNullable(sessionRequestContent.getRequestParameter(RequestParameter.NEW_PAGE));
        if (currentPage.isEmpty() && currentTablePage == null) {
            currentTablePage = 1;
        } else if (currentPage.isPresent()) {
            currentTablePage = Integer.parseInt(currentPage.get());
        }
        sessionRequestContent.setSessionAttribute(Attribute.ALL_MOVIES_CURRENT_PAGE, currentTablePage);
        int moviesNumber = ViewAllMoviesTag.MOVIES_PER_PAGE_NUMBER;
        int start = (currentTablePage - 1) * moviesNumber;
        int end = moviesNumber + start;
        try {
            List<Movie> allCurrentMovies = movieService.findAllBetween(start, end);
            sessionRequestContent.setSessionAttribute(Attribute.ALL_MOVIES_LIST, allCurrentMovies);
            int moviesCount = movieService.countMovies();
            sessionRequestContent.setSessionAttribute(Attribute.MOVIES_COUNT, moviesCount);
            if (allCurrentMovies.size() == 0) {
                sessionRequestContent.setRequestAttribute(Attribute.CONFIRM_MESSAGE, FriendlyMessage.EMPTY_MOVIES_LIST);
            }
        } catch (ServiceException e) {
            logger.error(e);
            throw new CommandException(e);
        }
        return commandResult;
    }

}
