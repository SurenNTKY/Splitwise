package models.enums;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum DashboardMenuCommands {
    CreateGroup(
            "(\\s*)create-group(\\s+)-n(\\s+)(?<name>[\\S\\s]+)(\\s+)-t(\\s+)(?<type>[\\S\\s]+)(\\s*)"
    ),
    ShowMyGroups("(\\s*)show(\\s+)my(\\s+)groups(\\s*)"),
    AddUserToGroup("(\\s*)add-user(\\s+)-u(\\s+)(?<username>[\\S\\s]+)(\\s+)-e(\\s+)(?<email>[\\S\\s]+)(\\s+)-g(\\s+)(?<groupId>-?\\d+)(\\s*)"),
    AddExpense("add-expense\\s+-g\\s+(?<groupId>\\d+)\\s+-s\\s+(?<shareType>equally|unequally)\\s+-t\\s+(?<totalExpense>\\d+)(?:\\D+)?\\s+-n\\s+(?<numberOfUsers>\\d+)(?:\\s+-l\\s+(?<lines>.+))?"),
    ShowBalance("(\\s*)show(\\s+)balance(\\s+)-u(\\s+)(?<username>[\\S\\s]+)(\\s*)"),
    SettleUp("(\\s*)settle-up(\\s+)-u(\\s+)(?<username>[\\S\\s]+)(\\s+)-m(\\s+)(?<inputMoney>[\\S\\s]+)(\\s*)"),
    GoToProfileMenu("(\\s*)go(\\s+)to(\\s+)profile(\\s+)menu(\\s*)"),
    Logout("(\\s*)logout(\\s*)"),
    Exit("(\\s*)exit(\\s*)");

    private final String pattern;

    DashboardMenuCommands(String pattern) {
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
