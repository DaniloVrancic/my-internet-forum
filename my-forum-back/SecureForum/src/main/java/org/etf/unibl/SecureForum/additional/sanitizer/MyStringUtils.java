package org.etf.unibl.SecureForum.additional.sanitizer;

public class MyStringUtils {

    public static String sanitize(String value) {
        if (value == null) {
            return null;
        }
        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
