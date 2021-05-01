package com.epam.project.controller.command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.*;

public class SessionRequestContent {
    private Map<String, Object> requestAttributes;
    private Map<String, String[]> requestParameters;
    private Map<String, Object> sessionAttributes;
    private List<Part> fileParts;

    public SessionRequestContent() {
        requestAttributes = new HashMap<>();
        requestParameters = new HashMap<>();
        sessionAttributes = new HashMap<>();
        //extractValues(request);
    }

    public void extractValues(HttpServletRequest request) {
        requestParameters = extractRequestParameters(request);
        requestAttributes = extractRequestAttributes(request);
        sessionAttributes = extractSessionAttributes(request);
        fileParts = extractFileParts(request);
    }

    public void insertAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Enumeration<String> attributeNames = session.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            session.removeAttribute(attributeNames.nextElement());
        }
        attributeNames = request.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            request.removeAttribute(attributeNames.nextElement());
        }
        requestAttributes.forEach(request::setAttribute);
        sessionAttributes.forEach(session::setAttribute);
    }

    public void invalidate() {
        sessionAttributes.clear();
        requestAttributes.clear();
    }

    public String extractLocale() {
        String currentLocale = (String) sessionAttributes.get(Attribute.LANG);
        if (currentLocale == null) {
            currentLocale = "en_US";
        }
        return currentLocale;
    }

    public Object getRequestAttribute(String key) {
        return requestAttributes.get(key);
    }

    public void setRequestAttribute(String key, Object value) {
        requestAttributes.put(key, value);
    }

    public void removeRequestAttribute(String key) {
        requestAttributes.remove(key);
    }

    public String getRequestParameter(String key) {
        if (requestParameters.get(key) == null){
            return null;
        }
        return requestParameters.get(key)[0];
    }

    public String[] getRequestParameters(String key) {
        return requestParameters.get(key);
    }

    public void setRequestParameters(HashMap<String, String[]> requestParameters) {
        this.requestParameters = requestParameters;
    }

    public Object getSessionAttribute(String key) {
        return sessionAttributes.get(key);
    }

    public void setSessionAttribute(String key, Object value) {
        sessionAttributes.put(key, value);
    }

    public void removeSessionAttribute(String key) {
        sessionAttributes.remove(key);
    }

    public List<Part> getFileParts() {
        return fileParts;
    }

    public void setFileParts(List<Part> fileParts) {
        this.fileParts = fileParts;
    }

    private Map<String, String[]> extractRequestParameters(HttpServletRequest request) {
        requestParameters = request.getParameterMap();
        return requestParameters;
    }

    private Map<String, Object> extractRequestAttributes(HttpServletRequest request) {
        requestAttributes = new HashMap<String, Object>();
        Enumeration<String> attributeNames = request.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String name = attributeNames.nextElement();
            requestAttributes.put(name, request.getAttribute(name));
        }
        return requestAttributes;
    }

    private Map<String, Object> extractSessionAttributes(HttpServletRequest request) {
        sessionAttributes = new HashMap<String, Object>();
        HttpSession session = request.getSession(true);
        Enumeration<String> attributeNames = session.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String name = attributeNames.nextElement();
            sessionAttributes.put(name, session.getAttribute(name));
        }
        return sessionAttributes;
    }

    private List<Part> extractFileParts(HttpServletRequest request) {
        List<Part> fileParts = new ArrayList<>();
        try {
            if (request.getContentType() != null && request.getContentType().toLowerCase().contains("multipart/form-data")) {
                fileParts.addAll(request.getParts());
            }
        } catch (IOException | ServletException e) {
            //logger.error(e);
        }
        return fileParts;
    }
}
