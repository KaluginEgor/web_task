package com.epam.project.controller;

import com.epam.project.controller.command.*;
import com.epam.project.exception.CommandException;
import com.epam.project.exception.ConnectionException;
import com.epam.project.model.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "controller", urlPatterns = {"/controller"})
@MultipartConfig(maxFileSize = 1024 * 1024 * 4, maxRequestSize = 1024 * 1024 * 8)
public class Controller extends HttpServlet {
    private static final Logger logger = LogManager.getLogger();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SessionRequestContent sessionRequestContent = new SessionRequestContent();
        sessionRequestContent.extractValues(request);

        CommandName commandName = CommandName.valueOf(request.getParameter(RequestParameter.COMMAND).toUpperCase());
        ActionCommand command = commandName.getCurrentCommand();
        CommandResult commandResult;
        try {
            commandResult = command.execute(sessionRequestContent);
        } catch (CommandException e) {
            logger.error(e);
            request.getSession().setAttribute(Attribute.ERROR_MESSAGE, e);
            commandResult = new CommandResult(PagePath.ERROR_500, TransitionType.REDIRECT);
        }
        sessionRequestContent.insertAttributes(request);

        if (commandResult.getPage() != null) {
            if (commandResult.getTransitionType() == TransitionType.FORWARD) {
                RequestDispatcher dispatcher = request.getRequestDispatcher(commandResult.getPage());
                dispatcher.forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + commandResult.getPage());
            }
        } else {
            String page = PagePath.INDEX;
            response.sendRedirect(request.getContextPath() + page);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        try {
            ConnectionPool.getInstance().destroyPool();
        } catch (ConnectionException e) {
            logger.error(e);
        }
    }
}
