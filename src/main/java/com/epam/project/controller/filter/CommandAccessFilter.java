package com.epam.project.controller.filter;

import com.epam.project.controller.command.Attribute;
import com.epam.project.controller.command.CommandName;
import com.epam.project.controller.command.PagePath;
import com.epam.project.controller.command.RequestParameter;
import com.epam.project.model.entity.User;
import com.epam.project.model.entity.UserRole;
import com.epam.project.model.util.message.ErrorMessage;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * The type Command access filter.
 */
@WebFilter(filterName = "CommandAccessFilter")
public class CommandAccessFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession();
        User user = (User) session.getAttribute(Attribute.USER);
        UserRole userRole = UserRole.GUEST;
        if (user != null) {
            if (user.getRole() != null) {
                userRole = user.getRole();
            }
        }
        String command = httpRequest.getParameter(RequestParameter.COMMAND);
        CommandName commandName;
        try {
            if (command == null) {
                httpRequest.setAttribute(Attribute.ERROR_MESSAGE, ErrorMessage.ERROR_COMMAND);
                RequestDispatcher dispatcher = httpRequest.getRequestDispatcher(PagePath.ERROR_404);
                dispatcher.forward(httpRequest, httpResponse);
                return;
            }
            commandName = CommandName.valueOf(command.toUpperCase());
        } catch (IllegalArgumentException e) {
            httpRequest.setAttribute(Attribute.ERROR_MESSAGE, ErrorMessage.ERROR_COMMAND);
            RequestDispatcher dispatcher = httpRequest.getRequestDispatcher(PagePath.ERROR_404);
            dispatcher.forward(httpRequest, httpResponse);
            return;
        }
        if (!commandName.isRoleAllowed(userRole)) {
            httpRequest.setAttribute(Attribute.ERROR_MESSAGE, ErrorMessage.ERROR_ACCESS);
            RequestDispatcher dispatcher = httpRequest.getRequestDispatcher(PagePath.ERROR_404);
            dispatcher.forward(httpRequest, httpResponse);
            return;
        }
        chain.doFilter(request, response);
    }
}