package org.etf.unibl.SecureForum.model.enums;


public enum UserType {
    Administrator("Administrator"),
    Moderator("Moderator"),
    Forumer("Forumer");

    private String value;
    private UserType(String value)
    {
        this.value = value;
    }

    public String getValue() {
        return value;
    }


}
