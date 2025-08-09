package views;

import controllers.DashboardController;
import models.Result;
import models.User;
import models.App;
import models.enums.DashboardMenuCommands;
import models.enums.ProfileMenuCommands;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;

public class Dashboard implements AppMenu {
    private final DashboardController controller = new DashboardController();
    private User currentUser;

    public Dashboard() {
        this.currentUser = App.currentUser;
    }

    @Override
    public void check(Scanner scanner) {
        String input = scanner.nextLine().trim();
        this.currentUser = App.currentUser;
        Matcher matcher;
        if ((matcher = DashboardMenuCommands.CreateGroup.getMatcher(input)) != null) {
            String name = matcher.group("name");
            String type = matcher.group("type");
            if (currentUser == null) {
                System.out.println("Error: You must be logged in to create a group.");
                return;
            }
            Result res = controller.createGroup(name, type, currentUser);
            System.out.println(res.getMessage());
        } else if ((matcher = DashboardMenuCommands.ShowMyGroups.getMatcher(input)) != null) {
            System.out.println(controller.showMyGroups(currentUser));
        } else if ((matcher = DashboardMenuCommands.AddUserToGroup.getMatcher(input)) != null) {
            String username = matcher.group("username");
            String email = matcher.group("email");
            int groupId = Integer.parseInt(matcher.group("groupId"));
            System.out.println(controller.addUserToGroup(username, email, groupId, currentUser).getMessage());
        }else if ((matcher = DashboardMenuCommands.AddExpense.getMatcher(input)) != null) {
            int groupId = Integer.parseInt(matcher.group("groupId"));
            String shareType = matcher.group("shareType");
            int totalExpense = Integer.parseInt(matcher.group("totalExpense"));
            int numberOfUsers = Integer.parseInt(matcher.group("numberOfUsers"));

            String linesStr = matcher.group("lines");
            List<String> expenseLines = new ArrayList<>();
            if (linesStr != null) {
                String[] lines = linesStr.split("\\r?\\n|,");
                for (String line : lines) {
                    String trimmed = line.trim();
                    if (!trimmed.isEmpty()) {
                        expenseLines.add(trimmed);
                    }
                }
            } else {
                for (int i = 0; i < numberOfUsers; i++) {
                    String line = scanner.nextLine().trim();
                    expenseLines.add(line);
                }
            }

            System.out.println(controller.addExpense(groupId, shareType, totalExpense, numberOfUsers, expenseLines, currentUser).getMessage());
        } else if ((matcher = DashboardMenuCommands.ShowBalance.getMatcher(input)) != null) {
            String otherUsername = matcher.group("username");
            System.out.println(controller.showBalance(otherUsername, currentUser).getMessage());
        } else if ((matcher = DashboardMenuCommands.SettleUp.getMatcher(input)) != null) {
            String otherUsername = matcher.group("username");
            int inputMoney = Integer.parseInt(matcher.group("inputMoney"));
            System.out.println(controller.settleUp(otherUsername, inputMoney, currentUser).getMessage());
        } else if ((matcher = DashboardMenuCommands.GoToProfileMenu.getMatcher(input)) != null) {
            System.out.println(controller.goToProfileMenu().getMessage());
        } else if ((matcher = DashboardMenuCommands.Logout.getMatcher(input)) != null) {
            System.out.println(controller.logout().getMessage());
        } else if ((matcher = DashboardMenuCommands.Exit.getMatcher(input)) != null) {
            System.out.println(controller.exit());
        } else {
            System.out.println("invalid command!");
        }
    }
}
