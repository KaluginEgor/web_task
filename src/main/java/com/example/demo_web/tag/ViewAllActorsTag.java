package com.example.demo_web.tag;

import com.example.demo_web.command.CommandEnum;
import com.example.demo_web.command.SessionAttribute;
import com.example.demo_web.command.SessionRequestContent;
import com.example.demo_web.entity.Actor;
import com.example.demo_web.entity.Movie;
import com.example.demo_web.util.TagUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.List;

public class ViewAllActorsTag extends TagSupport {
    public static final int ACTORS_PER_PAGE_NUMBER = 5;
    @Override
    public int doStartTag() throws JspException {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        JspWriter writer = pageContext.getOut();
        SessionRequestContent sessionRequestContent = new SessionRequestContent();
        sessionRequestContent.extractValues(request);
        createActors(writer, sessionRequestContent);
        int currentPage = (int) sessionRequestContent.getSessionAttribute(SessionAttribute.ALL_ACTORS_CURRENT_PAGE);
        int moviesCount = (int) sessionRequestContent.getSessionAttribute(SessionAttribute.ACTORS_COUNT);
        int pagesCount = moviesCount % ACTORS_PER_PAGE_NUMBER == 0 ? (moviesCount / ACTORS_PER_PAGE_NUMBER) : (moviesCount / ACTORS_PER_PAGE_NUMBER + 1);
        String command = CommandEnum.SHOW_ALL_ACTORS.toString().toLowerCase();
        TagUtil.paginate(pageContext, currentPage, pagesCount, command);
        return SKIP_BODY;
    }

    @Override
    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }

    private void createActors(JspWriter writer, SessionRequestContent sessionRequestContext) throws JspException {
        List<Actor> allActors = (List<Actor>) sessionRequestContext.getSessionAttribute(SessionAttribute.ALL_ACTORS_LIST);
        if (allActors != null) {
            int size = allActors.size();
            int createdActorsCount = 0;
            Actor actor = null;
            String contextPath = pageContext.getServletContext().getContextPath();
            try {
                writer.write("<ul>");
                for (int i = 0; i < ACTORS_PER_PAGE_NUMBER; i++) {
                    if (size > createdActorsCount) {
                        actor = allActors.get(createdActorsCount);
                        writer.write("<li>");
                        writer.write(" <div class=\"movie\">");
                        writer.write("<a href=\"controller?command=show_movie_page&movieId=" + actor.getId() + "\">");
                        writer.write("<h4 class=\"title\">" + actor.getFirstName() + " " + actor.getSecondName() + "</h4>");
                        writer.write("</a>");
                        writer.write("<div class=\"poster\">");
                        writer.write("<a href=\"#\">");
                        writer.write("<img src=\"" + contextPath + "/" + actor.getPicture() + "\" alt=\"" + actor.getFirstName() + " " + actor.getSecondName() + "\"/>");
                        writer.write("</a>");
                        writer.write("</div>");
                        writer.write("<p class=\"description\">" + actor.getBio() + "</p>");
                        writer.write("</div>");
                        writer.write("</li>");
                        createdActorsCount++;
                    }
                }
                writer.write("</ul>");
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }
    }
}
