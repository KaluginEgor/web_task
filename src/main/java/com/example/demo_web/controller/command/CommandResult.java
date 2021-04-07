package com.example.demo_web.controller.command;

public class CommandResult {
    private String page;
    private TransitionType transitionType;

    public CommandResult() {
    }

    public CommandResult(String page, TransitionType transitionType) {
        this.page = page;
        this.transitionType = transitionType;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public TransitionType getTransitionType() {
        return transitionType;
    }

    public void setTransitionType(TransitionType transitionType) {
        this.transitionType = transitionType;
    }
}
