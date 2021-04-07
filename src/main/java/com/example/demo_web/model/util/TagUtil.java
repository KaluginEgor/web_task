package com.example.demo_web.model.util;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import java.io.IOException;

public class TagUtil {
    public static void paginate(PageContext pageContext, int currentPage, int pagesCount, String command) throws JspException {
        try {
            JspWriter writer = pageContext.getOut();
            writer.write("<form method=\"post\" action=\"controller\">");
            writer.write("<input type=\"hidden\" name=\"command\" value=\"" + command + "\"/>");
            writer.write("<ul class=\"navigate\">");
            for (int i = 0; i < pagesCount; i++) {
                createButton(writer, i + 1);
            }
            writer.write("</ul>");
            writer.write("</form>");
        } catch (IOException e) {
            //logger.error(e);
            throw new JspException(e);
        }
    }

    private static void createButton(JspWriter writer, int pageNumber) throws IOException {
        writer.write("<li><button type=\"submit\" name=\"newPage\" ");
        writer.write("value=\"" + pageNumber + "\" ");
        writer.write("style=\""
                + "background-color: #ffffffb8; color: #000" + "\">");
        writer.write(pageNumber + " </button></li>");
    }
}
