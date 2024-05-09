package org.etf.unibl.SecureForum.model.enums;


public enum UserType {
    ADMINISTRATOR("Administrator"),
    MODERATOR("Moderator"),
    FORUMER("Forumer");

    private String value;
    private UserType(String value)
    {
        this.value = value;
    }

    public String getValue() {
        return value;
    }


}