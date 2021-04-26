package com.example.demo_web.controller.command.impl.common.page;


import com.example.demo_web.controller.command.*;
import com.example.demo_web.exception.CommandException;
import com.example.demo_web.model.entity.Movie;
import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.model.service.MovieService;
import com.example.demo_web.model.service.impl.MovieServiceImpl;
import com.example.demo_web.model.util.message.FriendlyMessage;
import com.example.demo_web.tag.ViewAllMoviesTag;
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
