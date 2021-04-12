package com.example.demo_web.tag;

import com.example.demo_web.controller.command.CommandEnum;
import com.example.demo_web.controller.command.SessionAttribute;
import com.example.demo_web.controller.command.SessionRequestContent;
import com.example.demo_web.model.entity.MediaPerson;
import com.example.demo_web.model.util.TagUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.List;

public class ViewAllMediaPersonsTag extends TagSupport {
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
        String command = CommandEnum.OPEN_ALL_MEDIA_PERSONS_PAGE.toString().toLowerCase();
        TagUtil.paginate(pageContext, currentPage, pagesCount, command);
        return SKIP_BODY;
    }

    @Override
    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }

    private void createActors(JspWriter writer, SessionRequestContent sessionRequestContext) throws JspException {
        List<MediaPerson> allMediaPeople = (List<MediaPerson>) sessionRequestContext.getSessionAttribute(SessionAttribute.ALL_ACTORS_LIST);
        if (allMediaPeople != null) {
            int size = allMediaPeople.size();
            int createdActorsCount = 0;
            MediaPerson mediaPerson = null;
            String contextPath = pageContext.getServletContext().getContextPath();
            try {
                writer.write("<ul>");
                for (int i = 0; i < ACTORS_PER_PAGE_NUMBER; i++) {
                    if (size > createdActorsCount) {
                        mediaPerson = allMediaPeople.get(createdActorsCount);
                        writer.write("<li>");
                        writer.write(" <div class=\"movie\">");
                        writer.write("<a href=\"controller?command=open_media_person_page&mediaPersonId=" + mediaPerson.getId() + "\">");
                        writer.write("<h4 class=\"title\">" + mediaPerson.getFirstName() + " " + mediaPerson.getSecondName() + "</h4>");
                        writer.write("</a>");
                        writer.write("<div class=\"poster\">");
                        writer.write("<a href=\"#\">");
                        writer.write("<img src=\"" + contextPath + "/" + mediaPerson.getPicture() + "\" alt=\"" + mediaPerson.getFirstName() + " " + mediaPerson.getSecondName() + "\"/>");
                        writer.write("</a>");
                        writer.write("</div>");
                        writer.write("<p class=\"description\">" + mediaPerson.getBio() + "</p>");
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
