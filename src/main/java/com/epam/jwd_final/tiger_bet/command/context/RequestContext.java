package com.epam.jwd_final.tiger_bet.command.context;

public interface RequestContext {

    void setAttribute(String name, Object obj);
    Object getAttribute(String name);
    void invalidateSession();
    void setSessionAttribute(String name, Object obj);
}
