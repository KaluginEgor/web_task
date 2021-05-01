package com.epam.project.controller;

import com.epam.project.controller.command.RequestParameter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@WebServlet(urlPatterns = {"/picture"})
public class PictureViewServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pictureName = request.getParameter(RequestParameter.CURRENT_PICTURE);
        byte[] image = Files.readAllBytes(Paths.get(pictureName));
        response.setHeader("Content-Type", getServletContext().getMimeType(pictureName));
        response.setHeader("Content-Length", String.valueOf(image.length));
        response.setHeader("Content-Disposition", "inline filename=\"" + pictureName + "\"");
        response.setContentType(getServletContext().getMimeType(pictureName));
        response.setContentLength(image.length);
        response.getOutputStream().write(image);
    }
}
