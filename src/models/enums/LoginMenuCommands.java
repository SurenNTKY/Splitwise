package models.enums;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum LoginMenuCommands {

    Login("(\\s*)login(\\s+)-u(\\s+)(?<username>[\\S\\s]+)(\\s+)-p(\\s+)(?<password>[\\S\\s]+)(\\s*)"),
    ForgetPassword("(\\s*)forget-password(\\s+)-u(\\s+)(?<username>[\\S\\s]+)(\\s+)-e(\\s+)(?<email>[\\S\\s]+)(\\s*)"),
    BackSignUpMenu("(\\s*)go(\\s+)to(\\s+)signup(\\s+)menu(\\s*)"),
    Exit("(\\s*)exit(\\s*)");


    final String pattern;

    LoginMenuCommands(String pattern) {
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