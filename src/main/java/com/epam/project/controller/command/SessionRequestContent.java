package com.epam.project.controller.command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.*;

/**
 * The type Session request content.
 */
public class SessionRequestContent {
    private Map<String, Object> requestAttributes;
    private Map<String, String[]> requestParameters;
    private Map<String, Object> sessionAttributes;
    private List<Part> fileParts;

    /**
     * Instantiates a new Session request content.
     */
    public SessionRequestContent() {
        requestAttributes = new HashMap<>();
        requestParameters = new HashMap<>();
        sessionAttributes = new HashMap<>();
        //extractValues(request);
    }

    /**
     * Extract values.
     *
     * @param request the request
     */
    public void extractValues(HttpServletRequest request) {
        requestParameters = extractRequestParameters(request);
        requestAttributes = extractRequestAttributes(request);
        sessionAttributes = extractSessionAttributes(request);
        fileParts = extractFileParts(request);
    }

    /**
     * Insert attributes.
     *
     * @param request the request
     */
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

    /**
     * Invalidate.
     */
    public void invalidate() {
        sessionAttributes.clear();
        requestAttributes.clear();
    }

    /**
     * Extract locale string.
     *
     * @return the string
     */
    public String extractLocale() {
        String currentLocale = (String) sessionAttributes.get(Attribute.LANG);
        if (currentLocale == null) {
            currentLocale = "en_US";
        }
        return currentLocale;
    }

    /**
     * Gets request attribute.
     *
     * @param key the key
     * @return the request attribute
     */
    public Object getRequestAttribute(String key) {
        return requestAttributes.get(key);
    }

    /**
     * Sets request attribute.
     *
     * @param key   the key
     * @param value the value
     */
    public void setRequestAttribute(String key, Object value) {
        requestAttributes.put(key, value);
    }

    /**
     * Remove request attribute.
     *
     * @param key the key
     */
    public void removeRequestAttribute(String key) {
        requestAttributes.remove(key);
    }

    /**
     * Gets request parameter.
     *
     * @param key the key
     * @return the request parameter
     */
    public String getRequestParameter(String key) {
        if (requestParameters.get(key) == null){
            return null;
        }
        return requestParameters.get(key)[0];
    }

    /**
     * Get request parameters string [ ].
     *
     * @param key the key
     * @return the string [ ]
     */
    public String[] getRequestParameters(String key) {
        return requestParameters.get(key);
    }

    /**
     * Sets request parameters.
     *
     * @param requestParameters the request parameters
     */
    public void setRequestParameters(HashMap<String, String[]> requestParameters) {
        this.requestParameters = requestParameters;
    }

    /**
     * Gets session attribute.
     *
     * @param key the key
     * @return the session attribute
     */
    public Object getSessionAttribute(String key) {
        return sessionAttributes.get(key);
    }

    /**
     * Sets session attribute.
     *
     * @param key   the key
     * @param value the value
     */
    public void setSessionAttribute(String key, Object value) {
        sessionAttributes.put(key, value);
    }

    /**
     * Remove session attribute.
     *
     * @param key the key
     */
    public void removeSessionAttribute(String key) {
        sessionAttributes.remove(key);
    }

    /**
     * Gets file parts.
     *
     * @return the file parts
     */
    public List<Part> getFileParts() {
        return fileParts;
    }

    /**
     * Sets file parts.
     *
     * @param fileParts the file parts
     */
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
