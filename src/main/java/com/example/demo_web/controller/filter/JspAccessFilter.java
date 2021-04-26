package com.example.demo_web.controller.filter;

import com.example.demo_web.controller.command.Attribute;
import com.example.demo_web.controller.command.PagePath;
import com.example.demo_web.model.entity.MediaPerson;
import com.example.demo_web.model.entity.Movie;
import com.example.demo_web.model.entity.User;
import com.example.demo_web.model.entity.UserRole;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebFilter(filterName = "JspAccessFilter")
public class JspAccessFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession();
        User user = (User) session.getAttribute(Attribute.USER);
        UserRole userRole = UserRole.GUEST;
        String page = (String) session.getAttribute(Attribute.PAGE);
        if (page == null) {
            page = PagePath.INDEX;
        }
        String uri = httpRequest.getRequestURI();
        List<Movie> movies = (List<Movie>) session.getAttribute(Attribute.ALL_MOVIES_LIST);
        List<MediaPerson> mediaPersons = (List<MediaPerson>) session.getAttribute(Attribute.ALL_MEDIA_PERSONS_LIST);
        List<User> users = (List<User>) session.getAttribute(Attribute.ALL_USERS_LIST);
        Movie movie = (Movie) session.getAttribute(Attribute.MOVIE);
        MediaPerson mediaPerson = (MediaPerson) session.getAttribute(Attribute.MEDIA_PERSON);
        User someUser = (User) session.getAttribute(Attribute.SOME_USER);
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
        } else if (uri.contains(PagePath.ALL_MOVIES) && movies == null) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + page);
            return;
        } else if (uri.contains(PagePath.ALL_MEDIA_PERSONS) && mediaPersons == null) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + page);
            return;
        } else if (uri.contains(PagePath.ALL_USERS) && users == null) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + page);
            return;
        } else if ((uri.contains(PagePath.EDIT_MOVIE) || uri.contains(PagePath.MOVIE)) && movie == null) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + page);
            return;
        } else if ((uri.contains(PagePath.EDIT_MEDIA_PERSON) || uri.contains(PagePath.MEDIA_PERSON)) && mediaPerson == null) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + page);
            return;
        } else if ((uri.contains(PagePath.EDIT_USER) || uri.contains(PagePath.USER)) && someUser == null) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + page);
            return;
        }
        chain.doFilter(request, response);
    }
}
