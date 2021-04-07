package com.example.demo_web.controller.command.impl;


import com.example.demo_web.controller.command.*;
import com.example.demo_web.model.entity.Movie;
import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.model.service.MovieService;
import com.example.demo_web.model.service.impl.MovieServiceImpl;
import com.example.demo_web.tag.ViewAllMoviesTag;

import java.util.List;
import java.util.Optional;

public class ShowAllMoviesCommand implements ActionCommand {

    private MovieService movieService = new MovieServiceImpl();

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        CommandResult commandResult = new CommandResult();
        commandResult.setTransitionType(TransitionType.FORWARD);

        Integer currentTablePage = (Integer) sessionRequestContent.getSessionAttribute(SessionAttribute.ALL_MOVIES_CURRENT_PAGE);
        Optional<String> currentPage = Optional.ofNullable(sessionRequestContent.getRequestParameter(RequestParameter.NEW_PAGE));
        if (currentPage.isEmpty() && currentTablePage == null) {
            currentTablePage = 1;
        } else if (currentPage.isPresent()) {
            currentTablePage = Integer.parseInt(currentPage.get());
        }
        sessionRequestContent.setSessionAttribute(SessionAttribute.ALL_MOVIES_CURRENT_PAGE, currentTablePage);
        int moviesNumber = ViewAllMoviesTag.MOVIES_PER_PAGE_NUMBER;
        int start = (currentTablePage - 1) * moviesNumber;
        int end = moviesNumber + start;
        try {
            List<Movie> allCurrentMovies = movieService.findAllBetween(start, end);
            sessionRequestContent.setSessionAttribute(SessionAttribute.ALL_MOVIES_LIST, allCurrentMovies);
            int moviesCount = movieService.countMovies();
            sessionRequestContent.setSessionAttribute(SessionAttribute.MOVIES_COUNT, moviesCount);
            if (allCurrentMovies.size() == 0) {
                //sessionRequestContent.setRequestAttribute(RequestAttribute.CONFIRM_MESSAGE, FriendlyMessage.EMPTY_USER_LIST);
            }
            commandResult.setPage(PagePath.ALL_MOVIES);
        } catch (ServiceException e) {
            commandResult.setPage(PagePath.ERROR);
        }
        return commandResult;
    }

}
