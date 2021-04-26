package com.example.demo_web.controller.command.impl.user;

import com.example.demo_web.controller.command.*;
import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.model.entity.*;
import com.example.demo_web.model.service.MediaPersonService;
import com.example.demo_web.model.service.MovieService;
import com.example.demo_web.model.service.UserService;
import com.example.demo_web.model.service.impl.MediaPersonServiceImpl;
import com.example.demo_web.model.service.impl.MovieServiceImpl;
import com.example.demo_web.model.service.impl.UserServiceImpl;

import javax.servlet.http.Part;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class UploadPictureCommand implements ActionCommand {
    private MovieService movieService = MovieServiceImpl.getInstance();
    private UserService userService = UserServiceImpl.getInstance();
    private MediaPersonService mediaPersonService = MediaPersonServiceImpl.getInstance();
    private static final String UPLOAD_PICTURE_DIRECTORY = "C:/Epam/pictures";
    private static final char FILE_FORMAT_SEPARATOR = '.';

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        CommandResult commandResult = new CommandResult();
        commandResult.setTransitionType(TransitionType.REDIRECT);
        String page = (String)sessionRequestContent.getSessionAttribute(Attribute.PAGE);
        List<Part> fileParts = sessionRequestContent.getFileParts();
        String fileName = null;

        try {
            for (Part part : fileParts) {
                fileName = part.getSubmittedFileName();
                if (fileName != null && !fileName.isEmpty()) {
                    fileName = buildNewFileName(fileName);
                    part.write(fileName);
                }
            }
        } catch (IOException e) {
            //logger.error(e);
        }
        if (fileName == null) {
            commandResult.setPage(page);
            //requestContext.setRequestAttribute(RequestAttribute.ERROR_MESSAGE, EMPTY_UPLOAD_FILE_PARAMETERS);
        } else {
            if (!fileName.isEmpty()) {
                sessionRequestContent.setSessionAttribute(Attribute.NEW_PICTURE, fileName);
                commandResult.setPage(page);
                //logger.info("User -> {}, change avatar", user);
            } else {
                //requestContext.setRequestAttribute(RequestAttribute.ERROR_MESSAGE, ErrorMessage.ERROR_WITH_UPLOAD);
                commandResult.setPage(page);
            }
        }
        return commandResult;
    }

    private String buildNewFileName(String oldFileName) {
        String fileFormat = oldFileName.substring(oldFileName.indexOf(FILE_FORMAT_SEPARATOR));
        return UPLOAD_PICTURE_DIRECTORY + "/" + UUID.randomUUID().toString() + fileFormat;
    }
}
