package org.etf.unibl.SecureForum.model.enums;

public enum PermissionType {
    Create("Create"),
    Read("Read"),
    Write("Write"),

    Delete("Delete");

    private String value;
    private PermissionType(String value)
    {
        this.value = value;
    }

    public String getValue() {
        return value;
    }


}
