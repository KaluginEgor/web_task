package com.epam.project.controller.command.impl.user;

import com.epam.project.controller.command.*;
import com.epam.project.model.util.message.ErrorMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.Part;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * The type Upload picture command.
 */
public class UploadPictureCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger();
    private static final String UPLOAD_PICTURE_DIRECTORY = "C:/Epam/pictures";
    private static final char FILE_FORMAT_SEPARATOR = '.';

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        String page = (String)sessionRequestContent.getSessionAttribute(Attribute.PAGE);
        CommandResult commandResult = new CommandResult(page, TransitionType.REDIRECT);
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
            logger.error(e);
        }
        if (fileName == null) {
            sessionRequestContent.setRequestAttribute(Attribute.ERROR_MESSAGE, ErrorMessage.EMPTY_UPLOAD_FILE_PARAMETERS);
        } else {
            if (!fileName.isEmpty()) {
                sessionRequestContent.setSessionAttribute(Attribute.NEW_PICTURE, fileName);
            } else {
                sessionRequestContent.setRequestAttribute(Attribute.ERROR_MESSAGE, ErrorMessage.ERROR_WITH_UPLOAD);
            }
        }
        return commandResult;
    }

    private String buildNewFileName(String oldFileName) {
        String fileFormat = oldFileName.substring(oldFileName.indexOf(FILE_FORMAT_SEPARATOR));
        return UPLOAD_PICTURE_DIRECTORY + "/" + UUID.randomUUID().toString() + fileFormat;
    }
}
