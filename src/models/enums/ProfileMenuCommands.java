package models.enums;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum ProfileMenuCommands {
    ShowUserInfo("(\\s*)show(\\s+)user(\\s+)info(\\s*)"),
    ChangeCurrency("^change-currency\\s+-n\\s+(\\S+)$"),
    ChangeUsername("^change-username\\s+-n\\s+(\\S+)$"),
    ChangePassword("^change-password\\s+-o\\s+(\\S+)\\s+-n\\s+(\\S+)$"),
    BackToDashboard("(\\s*)back(\\s*)"),
    Exit("(\\s*)exit(\\s*)");


    final String pattern;

    ProfileMenuCommands(String pattern) {
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
