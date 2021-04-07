package com.example.demo_web.controller;

import com.example.demo_web.model.pool.ConnectionPool;
import com.example.demo_web.controller.command.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "controller", urlPatterns = {"/controller"})
public class Controller extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String page;
        CommandResult commandResult;

        SessionRequestContent sessionRequestContent = new SessionRequestContent();
        sessionRequestContent.extractValues(request);

        ActionFactory client = new ActionFactory();
        ActionCommand command = client.defineCommand(sessionRequestContent);

        commandResult = command.execute(sessionRequestContent);
        sessionRequestContent.insertAttributes(request);

        if (commandResult.getPage() != null) {
            if (commandResult.getTransitionType() == TransitionType.FORWARD) {
                RequestDispatcher dispatcher = request.getRequestDispatcher(commandResult.getPage());
                dispatcher.forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + commandResult.getPage());
            }
        } else {
            page = PagePath.INDEX;
            response.sendRedirect(request.getContextPath() + page);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        ConnectionPool.getInstance().destroyPool();
    }
}
