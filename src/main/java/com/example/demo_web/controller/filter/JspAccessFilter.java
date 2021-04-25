package com.example.demo_web.controller.filter;

import com.example.demo_web.controller.command.Attribute;
import com.example.demo_web.controller.command.PagePath;
import com.example.demo_web.model.entity.User;
import com.example.demo_web.model.entity.UserRole;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"/pages/*"}, filterName = "JspAccessFilter")
public class JspAccessFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession();
        User user = (User) session.getAttribute(Attribute.USER);
        UserRole userRole = UserRole.GUEST;
        String page = PagePath.ALL_MOVIES;
        String uri = httpRequest.getRequestURI();
        if (user != null) {
            if (user.getRole() != null) {
                userRole = user.getRole();
            }
        }
        if (uri.contains(PagePath.ADMIN_URL_PART) && (userRole != UserRole.ADMIN)) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + page);
            return;
        } else if (uri.contains(PagePath.USER_URL_PART) && (userRole == UserRole.GUEST)) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + page);
            return;
        } else if (uri.contains(PagePath.GUEST_URL_PART) && (userRole != UserRole.GUEST)) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + page);
            return;
        } else if (uri.contains(PagePath.ERROR_URL_PART) || uri.contains(PagePath.MODULE_URL_PART)) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + page);
            return;
        }
        chain.doFilter(request, response);
    }
}
