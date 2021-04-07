package com.example.demo_web.tag;

import com.example.demo_web.controller.command.CommandEnum;
import com.example.demo_web.controller.command.SessionAttribute;
import com.example.demo_web.controller.command.SessionRequestContent;
import com.example.demo_web.model.entity.Movie;
import com.example.demo_web.model.util.TagUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.List;

public class ViewAllMoviesTag extends TagSupport {
    public static final int MOVIES_PER_PAGE_NUMBER = 10;
    @Override
    public int doStartTag() throws JspException {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        JspWriter writer = pageContext.getOut();
        SessionRequestContent sessionRequestContent = new SessionRequestContent();
        sessionRequestContent.extractValues(request);
        createMovies(writer, sessionRequestContent);
        int currentPage = (int) sessionRequestContent.getSessionAttribute(SessionAttribute.ALL_MOVIES_CURRENT_PAGE);
        int moviesCount = (int) sessionRequestContent.getSessionAttribute(SessionAttribute.MOVIES_COUNT);
        int pagesCount = moviesCount % MOVIES_PER_PAGE_NUMBER == 0 ? (moviesCount / MOVIES_PER_PAGE_NUMBER) : (moviesCount / MOVIES_PER_PAGE_NUMBER + 1);
        String command = CommandEnum.SHOW_ALL_MOVIES.toString().toLowerCase();
        TagUtil.paginate(pageContext, currentPage, pagesCount, command);
        return SKIP_BODY;
    }

    @Override
    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }

    private void createMovies(JspWriter writer, SessionRequestContent sessionRequestContext) throws JspException {
        List<Movie> allMovies = (List<Movie>) sessionRequestContext.getSessionAttribute(SessionAttribute.ALL_MOVIES_LIST);
        if (allMovies != null) {
            int size = allMovies.size();
            int createdMoviesCount = 0;
            Movie movie = null;
            String contextPath = pageContext.getServletContext().getContextPath();
            try {
                writer.write("<ul>");
                for (int i = 0; i < MOVIES_PER_PAGE_NUMBER; i++) {
                    if (size > createdMoviesCount) {
                        movie = allMovies.get(createdMoviesCount);
                        writer.write("<li>");
                        writer.write(" <div class=\"movie\">");
                        writer.write("<a href=\"controller?command=open_movie_page&movieId=" + movie.getId() + "\">");
                        writer.write("<h4 class=\"title\">" + movie.getTitle() + "</h4>");
                        writer.write("</a>");
                        writer.write("<div class=\"poster\">");
                        writer.write("<a href=\"#\">");
                        writer.write("<img src=\"" + contextPath + "/" + movie.getPicture() + "\" alt=\"" + movie.getTitle() + "\"/>");
                        writer.write("</a>");
                        writer.write("</div>");
                        writer.write("<p class=\"description\">" + movie.getDescription() + "</p>");
                        writer.write("</div>");
                        writer.write("</li>");
                        createdMoviesCount++;
                    }
                }
                writer.write("</ul>");
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }
    }
}
