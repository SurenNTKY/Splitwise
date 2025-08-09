package models.enums;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum GroupType {
    Home("Home"),
    Trip("Trip"),
    ZanoOBache("ZanoOBache"),
    Other("Other");

    final String pattern;

    GroupType(String pattern) {
        this.pattern = pattern;
    }

    public Matcher getMatcher(String input) {
        Matcher matcher = Pattern.compile(this.pattern).matcher(input);

        if (matcher.matches()) {
            return matcher;
        }
        return null;
    }
}
