package com.epam.project.tag;

import com.epam.project.controller.command.Attribute;
import com.epam.project.controller.command.CommandName;
import com.epam.project.controller.command.SessionRequestContent;
import com.epam.project.model.entity.Movie;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

public class ViewAllMoviesTag extends TagSupport {
    private static final String RATING_BUNDLE = "movie.rating";
    public static final int MOVIES_PER_PAGE_NUMBER = 4;

    @Override
    public int doStartTag() throws JspException {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        JspWriter writer = pageContext.getOut();
        SessionRequestContent sessionRequestContent = new SessionRequestContent();
        sessionRequestContent.extractValues(request);
        createMovies(writer, sessionRequestContent);
        int moviesCount = (int) sessionRequestContent.getSessionAttribute(Attribute.MOVIES_COUNT);
        int pagesCount = moviesCount % MOVIES_PER_PAGE_NUMBER == 0 ? (moviesCount / MOVIES_PER_PAGE_NUMBER) : (moviesCount / MOVIES_PER_PAGE_NUMBER + 1);
        String command = CommandName.OPEN_ALL_MOVIES_PAGE.toString().toLowerCase();
        TagHelper.paginate(pageContext, pagesCount, command);
        return SKIP_BODY;
    }

    @Override
    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }

    private void createMovies(JspWriter writer, SessionRequestContent sessionRequestContent) throws JspException {
        List<Movie> allMovies = (List<Movie>) sessionRequestContent.getSessionAttribute(Attribute.ALL_MOVIES_LIST);
        String lang = sessionRequestContent.extractLocale();
        ResourceBundle resourceBundle = TagHelper.getLocalizeText(lang);
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
                        writer.write("<a href=\"" + contextPath +"/controller?command=open_movie_page&movieId=" + movie.getId() + "\">");
                        writer.write("<h4 class=\"title\">" + movie.getTitle() + "</h4>");
                        writer.write("</a>");
                        writer.write("<div class=\"poster\">");
                        writer.write("<a href=\"" + contextPath +"/controller?command=open_movie_page&movieId=" + movie.getId() + "\">");
                        writer.write("<img src=\"" + contextPath + "/picture?currentPicture=" + movie.getPicture() + "\" alt=\"" + movie.getTitle() + "\"/>");
                        writer.write("</a>");
                        writer.write("</div>");
                        writer.write("<p class=\"description\">" + resourceBundle.getString(RATING_BUNDLE) + " "  + movie.getAverageRating() + "</p>");
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
