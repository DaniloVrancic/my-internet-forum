package org.etf.unibl.SecureForum.model.enums;

/**
 * Has all the defined  rights of use for
 * a user commenting on a certain topic,
 * as defined in the users` specification.
 */
public enum PermissionType {
    Create("Create"),
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
