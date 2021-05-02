package com.epam.project.controller.command;

/**
 * The type Command result.
 */
public class CommandResult {
    private String page;
    private TransitionType transitionType;

    /**
     * Instantiates a new Command result.
     */
    public CommandResult() {
    }

    /**
     * Instantiates a new Command result.
     *
     * @param page           the page
     * @param transitionType the transition type
     */
    public CommandResult(String page, TransitionType transitionType) {
        this.page = page;
        this.transitionType = transitionType;
    }

    /**
     * Gets page.
     *
     * @return the page
     */
    public String getPage() {
        return page;
    }

    /**
     * Sets page.
     *
     * @param page the page
     */
    public void setPage(String page) {
        this.page = page;
    }

    /**
     * Gets transition type.
     *
     * @return the transition type
     */
    public TransitionType getTransitionType() {
        return transitionType;
    }

    /**
     * Sets transition type.
     *
     * @param transitionType the transition type
     */
    public void setTransitionType(TransitionType transitionType) {
        this.transitionType = transitionType;
    }
}
