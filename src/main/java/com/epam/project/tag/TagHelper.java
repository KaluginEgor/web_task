package com.epam.project.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * The type Tag helper.
 */
class TagHelper {
    private static final String PAGE_PROPERTIES_PATH = "property/pagecontent";

    /**
     * Gets localize text.
     *
     * @param locale the locale
     * @return the localize text
     */
    static ResourceBundle getLocalizeText(String locale) {
        String language = locale.substring(0, locale.indexOf("_"));
        String country = locale.substring(locale.indexOf("_") + 1);
        return ResourceBundle.getBundle(PAGE_PROPERTIES_PATH, new Locale(language, country));
    }

    /**
     * Paginate.
     *
     * @param pageContext the page context
     * @param pagesCount  the pages count
     * @param command     the command
     * @throws JspException the jsp exception
     */
    static void paginate(PageContext pageContext, int pagesCount, String command) throws JspException {
        try {
            JspWriter writer = pageContext.getOut();
            String contextPath = pageContext.getServletContext().getContextPath();
            writer.write("<form method=\"post\" action=\"" + contextPath + "/controller\">");
            writer.write("<input type=\"hidden\" name=\"command\" value=\"" + command + "\"/>");
            writer.write("<div class=\"accent-bar\">");
            writer.write("<ul class=\"accents\">");
            for (int i = 0; i < pagesCount; i++) {
                createButton(writer, i + 1);
            }
            writer.write("</ul>");
            writer.write("</div>");
            writer.write("</form>");
        } catch (IOException e) {
            //logger.error(e);
            throw new JspException(e);
        }
    }

    /**
     * Create table head item.
     *
     * @param writer  the writer
     * @param content the content
     * @throws JspException the jsp exception
     */
    static void createTableHeadItem(JspWriter writer, String content) throws JspException {
        try {
            writer.write("<th>" + content + "</th>");
        } catch (IOException e) {
            throw new JspException(e);
        }
    }

    /**
     * Create user button.
     *
     * @param writer      the writer
     * @param command     the command
     * @param pageContext the page context
     * @param userId      the user id
     * @param buttonText  the button text
     * @throws IOException the io exception
     */
    static void createUserButton(JspWriter writer, String command, PageContext pageContext, int userId, String buttonText) throws IOException {
        String contextPath = pageContext.getServletContext().getContextPath();
        writer.write("<form method=\"post\" action=\"" + contextPath + "/controller\">");
        writer.write("<input type=\"hidden\" name=\"command\" value=\"" + command + "\"/>");
        writer.write("<input type=\"hidden\" name=\"userId\" value=\"" + userId + "\"/>");
        writer.write("<button class=\"apply_button\" type=\"submit\">");
        writer.write(buttonText + "</button></form>");
    }

    /**
     * Create button.
     *
     * @param writer     the writer
     * @param pageNumber the page number
     * @throws IOException the io exception
     */
    static void createButton(JspWriter writer, int pageNumber) throws IOException {
        writer.write("<li><button type=\"submit\" name=\"newPage\" ");
        writer.write("value=\"" + pageNumber + "\" ");
        writer.write("style=\""
                + "background-color: #ffffffb8; color: #000" + "\">");
        writer.write(pageNumber + " </button></li>");
    }
}
