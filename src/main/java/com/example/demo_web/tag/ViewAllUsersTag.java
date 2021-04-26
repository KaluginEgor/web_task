package com.example.demo_web.tag;

import com.example.demo_web.controller.command.Attribute;
import com.example.demo_web.controller.command.CommandName;
import com.example.demo_web.controller.command.SessionRequestContent;
import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.model.entity.User;
import com.example.demo_web.model.entity.UserRole;
import com.example.demo_web.model.entity.UserState;
import com.example.demo_web.model.service.UserService;
import com.example.demo_web.model.service.impl.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

public class ViewAllUsersTag extends TagSupport {
    public static final int USERS_PER_PAGE_NUMBER = 4;

    private static final String USER_ID_BUNDLE = "user.id";
    private static final String USER_LOGIN_BUNDLE = "user.login";
    private static final String USER_FIRST_NAME_BUNDLE = "user.name.first";
    private static final String USER_SECOND_NAME_BUNDLE = "user.name.second";
    private static final String USER_EMAIL_BUNDLE = "user.email";
    private static final String USER_ROLE_BUNDLE = "user.role";
    private static final String USER_STATUS_BUNDLE = "user.state";
    private static final String OPERATION = "label.operation";
    private static final String USER_BLOCK_BUTTON_BUNDLE = "label.block.user";
    private static final String USER_ACTIVATE_BUTTON_BUNDLE = "label.activate.user";
    private static final String OPEN_PROFILE_BUNDLE = "label.open.profile";

    @Override
    public int doStartTag() throws JspException {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        JspWriter writer = pageContext.getOut();
        SessionRequestContent sessionRequestContent = new SessionRequestContent();
        sessionRequestContent.extractValues(request);
        createTable(writer, sessionRequestContent);
        int usersCount = (int) sessionRequestContent.getSessionAttribute(Attribute.USERS_COUNT);
        int pagesCount = usersCount % USERS_PER_PAGE_NUMBER == 0 ? (usersCount / USERS_PER_PAGE_NUMBER) : (usersCount / USERS_PER_PAGE_NUMBER + 1);
        String command = CommandName.OPEN_ALL_USERS_PAGE.toString().toLowerCase();
        TagHelper.paginate(pageContext, pagesCount, command);
        return SKIP_BODY;
    }

    @Override
    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }

    private void createTable(JspWriter writer, SessionRequestContent sessionRequestContent) throws JspException {
        try {
            writer.write("<table id=\"customers\">");
            createTableHeader(writer, sessionRequestContent);
            writer.write("<tbody>");
            List<User> allUsers = (List<User>) sessionRequestContent.getSessionAttribute(Attribute.ALL_USERS_LIST);
            createLines(writer, sessionRequestContent, allUsers);
            writer.write("</tbody></table>");
        } catch (IOException e) {
            throw new JspException(e);
        }
    }

    private void createTableHeader(JspWriter writer, SessionRequestContent sessionRequestContent) throws JspException {
        try {
            String lang = sessionRequestContent.extractLocale();
            ResourceBundle resourceBundle = TagHelper.getLocalizeText(lang);
            String userId = resourceBundle.getString(USER_ID_BUNDLE);
            String userLogin = resourceBundle.getString(USER_LOGIN_BUNDLE);
            String userFirstName = resourceBundle.getString(USER_FIRST_NAME_BUNDLE);
            String userSecondName = resourceBundle.getString(USER_SECOND_NAME_BUNDLE);
            String userEmail = resourceBundle.getString(USER_EMAIL_BUNDLE);
            String userType = resourceBundle.getString(USER_ROLE_BUNDLE);
            String userStatus = resourceBundle.getString(USER_STATUS_BUNDLE);
            String operation = resourceBundle.getString(OPERATION);
            String open = resourceBundle.getString(OPEN_PROFILE_BUNDLE);
            writer.write("<thead><tr>");
            writer.write("<th><span style=\"font-weight: bold\">№</span></th>");
            TagHelper.createTableHeadItem(writer, userId);
            TagHelper.createTableHeadItem(writer, userLogin);
            TagHelper.createTableHeadItem(writer, userFirstName);
            TagHelper.createTableHeadItem(writer, userSecondName);
            TagHelper.createTableHeadItem(writer, userEmail);
            TagHelper.createTableHeadItem(writer, userType);
            TagHelper.createTableHeadItem(writer, userStatus);
            TagHelper.createTableHeadItem(writer, operation);
            TagHelper.createTableHeadItem(writer, open);
            writer.write("</tr></thead>");
        } catch (IOException e) {
            throw new JspException(e);
        }
    }

    private void createLines(JspWriter writer, SessionRequestContent sessionRequestContent, List<User> allUsers) throws JspException {
        String lang = sessionRequestContent.extractLocale();
        ResourceBundle resourceBundle = TagHelper.getLocalizeText(lang);
        UserService userService = UserServiceImpl.getInstance();
        try {
            if (allUsers != null) {
                int size = allUsers.size();
                int currentPage = (int) sessionRequestContent.getSessionAttribute(Attribute.ALL_USERS_CURRENT_PAGE);
                for (int i = 0; i < USERS_PER_PAGE_NUMBER; i++) {
                    int lineNumber = USERS_PER_PAGE_NUMBER * (currentPage - 1) + i + 1;
                    if (size > i) {
                        User user = allUsers.get(i);
                        UserState userState = userService.detectStateById(user.getId());
                        writer.write("<tr><th>" + lineNumber + "</th>");
                        writer.write("<td>" + user.getId() + "</td>");
                        writer.write("<td>" + user.getLogin() + "</td>");
                        writer.write("<td>" + user.getFirstName() + "</td>");
                        writer.write("<td>" + user.getSecondName() + "</td>");
                        writer.write("<td>" + user.getEmail() + "</td>");
                        writer.write("<td>" + user.getRole() + "</td>");
                        writer.write("<td>" + userState + "</td>");
                        writer.write("<td>");
                        if ((userState == UserState.ACTIVE) && (user.getRole() != UserRole.ADMIN)) {
                            TagHelper.createUserButton(writer, CommandName.BLOCK_USER.name(), pageContext, user.getId(), resourceBundle.getString(USER_BLOCK_BUTTON_BUNDLE));
                        } else if (userState == UserState.BLOCKED) {
                            TagHelper.createUserButton(writer, CommandName.ACTIVATE_USER.name(), pageContext, user.getId(), resourceBundle.getString(USER_ACTIVATE_BUTTON_BUNDLE));
                        }
                        writer.write("</td>");
                        writer.write("<td>");
                        TagHelper.createUserButton(writer, CommandName.OPEN_USER_PAGE.name(), pageContext, user.getId(), resourceBundle.getString(OPEN_PROFILE_BUNDLE));
                        writer.write("</td>");
                    }
                    writer.write("</tr>");
                }
            }
        } catch (IOException | ServiceException e) {
            throw new JspException(e);
        }
    }
}
