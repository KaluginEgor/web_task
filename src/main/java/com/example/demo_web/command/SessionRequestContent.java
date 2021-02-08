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
    }

    public void insertAttributes(HttpServletRequest request) {
        requestAttributes.forEach(request::setAttribute);
        HttpSession session = request.getSession();
        sessionAttributes.forEach(request::setAttribute);
    }

    public Object getRequestAttribute(String key) {
        return requestAttributes.get(key);
    }

    public void setRequestAttribute(String key, Object value) {
        requestAttributes.put(key, value);
    }

    public String getRequestParameter(String key) {
        if (requestParameters.isEmpty()){
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
