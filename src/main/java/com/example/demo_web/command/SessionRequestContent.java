package com.example.demo_web.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class SessionRequestContent {
    private Map<String, Object> requestAttributes;
    private Map<String, String[]> requestParameters;
    private Map<String, Object> sessionAttributes;
    private String contextPath;
    private String servletPath;

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
        contextPath = request.getContextPath();
        servletPath = request.getServletPath();
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
}
