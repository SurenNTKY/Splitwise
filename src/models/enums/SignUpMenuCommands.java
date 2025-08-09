package models.enums;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum SignUpMenuCommands {
    Register("(\\s*)register(\\s+)-u(\\s+)(?<username>[\\S\\s]+)(\\s+)-p(\\s+)(?<password>[\\S\\s]+)(\\s+)-e(\\s+)(?<email>[\\S\\s]+)(\\s+)-n(\\s+)(?<name>[\\S\\s]+)(\\s*)"),
    Exit("(\\s*)exit(\\s*)"),
    LoginToMenu("(\\s*)go(\\s+)to(\\s+)login(\\s+)menu(\\s+)(\\s*)");

    private final String pattern;

    SignUpMenuCommands(String pattern) {
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
