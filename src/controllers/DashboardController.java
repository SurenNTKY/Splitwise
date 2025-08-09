package controllers;

import models.App;
import models.Expense;
import models.Group;
import models.Result;
import models.User;
import models.enums.Currency;
import models.enums.Menu;

import java.util.*;
import java.util.regex.Pattern;

public class DashboardController {

    public Result createGroup(String name, String type, User creator) {
        String groupNamePattern = "^[a-zA-Z0-9!@#$%^&* ]{4,30}$";
        if (!Pattern.matches(groupNamePattern, name)) {
            return new Result(false, "group name format is invalid!");
        }
        if (!(type.equals("Home") || type.equals("Trip") || type.equals("Zan-o-Bache") || type.equals("Other"))) {
            return new Result(false, "group type is invalid!");
        }
        int groupId = App.groups.size() + 1;
        Group group = new Group(groupId, name, type, creator);
        App.groups.add(group);
        return new Result(true, "group created successfully!");
    }

    public String showMyGroups(User user) {
        StringBuilder sb = new StringBuilder();
        for (Group group : App.groups) {
            if (group.getMembers().contains(user)) {
                sb.append("group name : ").append(group.getName()).append("\n");
                sb.append("id : ").append(group.getId()).append("\n");
                sb.append("type : ").append(group.getType()).append("\n");
                if (group.getCreator() != null) {
                    sb.append("creator : ").append(group.getCreator().getName()).append("\n");
                } else {
                    sb.append("creator : Unknown\n");
                }
                sb.append("members :\n");
                for (User member : group.getMembers()) {
                    sb.append(member.getName()).append("\n");
                }
                sb.append("--------------------\n");
            }
        }
        return sb.toString().trim();
    }

    public Result addUserToGroup(String username, String email, int groupId, User currentUser) {
        username = username.trim();
        email = email.trim();

        User userToAdd = null;
        for (User user : App.users) {
            if (user.getUsername().equals(username)) {
                userToAdd = user;
                break;
            }
        }
        if (userToAdd == null) {
            return new Result(false, "user not found!");
        }
        if (!userToAdd.getEmail().equals(email)) {
            return new Result(false, "the email provided does not match the username!");
        }
        Group group = null;
        for (Group g : App.groups) {
            if (g.getId() == groupId) {
                group = g;
                break;
            }
        }
        if (group == null) {
            return new Result(false, "group not found!");
        }
        if (!group.getCreator().getUsername().equals(currentUser.getUsername())) {
            return new Result(false, "only the group creator can add users!");
        }
        if (group.getMembers().contains(userToAdd)) {
            return new Result(false, "user already in the group!");
        }
        group.addMember(userToAdd);
        return new Result(true, "user added to the group successfully!");
    }

    public Result addExpense(int groupId, String shareType, int totalExpense, int numberOfUsers, List<String> expenseLines, User currentUser) {
        Group group = null;
        for (Group g : App.groups) {
            if (g.getId() == groupId) {
                group = g;
                break;
            }
        }
        if (group == null) {
            return new Result(false, "group not found!");
        }
        if (!(shareType.equals("equally") || shareType.equals("unequally"))) {
            return new Result(false, "expense format is invalid!");
        }
        Currency userCurr = Currency.fromPattern(currentUser.getCurrency());
        if (userCurr == null) {
            return new Result(false, "current currency format is invalid!");
        }

        double totalExpenseInGTC = totalExpense / (double) userCurr.getRate();
        Map<String, Double> userExpenses = new HashMap<>();
        double sum = 0.0;
        if (shareType.equals("equally")) {
            if (expenseLines.size() != numberOfUsers) {
                return new Result(false, "expense format is invalid!");
            }
            double share = totalExpenseInGTC / numberOfUsers;
            if (Math.abs(share * numberOfUsers - totalExpenseInGTC) > 0.0001) {
                return new Result(false, "the sum of individual costs does not equal the total cost!");
            }
            List<String> invalidUsers = new ArrayList<>();
            for (String username : expenseLines) {
                boolean found = false;
                for (User member : group.getMembers()) {
                    if (member.getUsername().equals(username)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    invalidUsers.add(username);
                } else {
                    userExpenses.put(username, share);
                    sum += share;
                }
            }
            if (!invalidUsers.isEmpty()) {
                String errorMsg = String.join("\n", invalidUsers.stream().map(name -> name + " not in group!").toArray(String[]::new));
                return new Result(false, errorMsg);
            }
        } else {
            if (expenseLines.size() != numberOfUsers) {
                return new Result(false, "expense format is invalid!");
            }
            List<String> invalidUsers = new ArrayList<>();
            for (String line : expenseLines) {
                String[] parts = line.split("\\s+");
                if (parts.length != 2) {
                    return new Result(false, "expense format is invalid!");
                }
                String username = parts[0];
                if (!Pattern.matches("\\d+", parts[1])) {
                    return new Result(false, "expense format is invalid!");
                }
                int expenseEntered = Integer.parseInt(parts[1]);
                double expenseInGTC = expenseEntered / (double) userCurr.getRate();
                boolean found = false;
                for (User member : group.getMembers()) {
                    if (member.getUsername().equals(username)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    invalidUsers.add(username);
                } else {
                    userExpenses.put(username, expenseInGTC);
                    sum += expenseInGTC;
                }
            }
            if (!invalidUsers.isEmpty()) {
                String errorMsg = String.join("\n", invalidUsers.stream().map(name -> name + " not in group!").toArray(String[]::new));
                return new Result(false, errorMsg);
            }
            if (Math.abs(sum - totalExpenseInGTC) > 0.0001) {
                return new Result(false, "the sum of individual costs does not equal the total cost!");
            }
        }
        Expense expense = new Expense(groupId, shareType, totalExpenseInGTC, userExpenses, currentUser.getUsername());
        group.addExpense(expense);
        return new Result(true, "expense added successfully!");
    }

    public Result showBalance(String otherUsername, User currentUser) {
        User otherUser = null;
        for (User u : App.users) {
            if (u.getUsername().equals(otherUsername)) {
                otherUser = u;
                break;
            }
        }
        if (otherUser == null) {
            return new Result(false, "user not found!");
        }

        double netGTC = 0.0;
        Set<String> groupsInvolved = new LinkedHashSet<>();

        for (Group group : App.groups) {
            boolean isCurrentUserInGroup = group.getMembers().contains(currentUser);
            boolean isOtherUserInGroup = group.getMembers().stream().anyMatch(u -> u.getUsername().equals(otherUsername));

            if (isCurrentUserInGroup && isOtherUserInGroup) {
                for (Expense expense : group.getExpenses()) {
                    if (expense.getPayer().equals(currentUser.getUsername())) {
                        Double amt = expense.getUserExpenses().get(otherUsername);
                        if (amt != null) {
                            netGTC += amt;
                            groupsInvolved.add(group.getName().trim());
                        }
                    }
                    if (expense.getPayer().equals(otherUsername)) {
                        Double amt = expense.getUserExpenses().get(currentUser.getUsername());
                        if (amt != null) {
                            netGTC -= amt;
                            groupsInvolved.add(group.getName().trim());
                        }
                    }
                }
            }
        }

        Currency currentUserCurr = Currency.fromPattern(currentUser.getCurrency());
        if(currentUserCurr == null) {
            return new Result(false, "current currency format is invalid!");
        }
        double netConverted = netGTC * currentUserCurr.getRate();
        String currency = currentUser.getCurrency();

        if (Math.abs(netConverted - Math.round(netConverted)) < 0.0001) {
            netConverted = Math.round(netConverted);
        }

        if (Math.abs(netConverted) < 0.0001) {
            return new Result(true, "you are settled with " + otherUsername );
        } else if (netConverted < 0) {
            return new Result(true, "you owe " + otherUsername + " " + (int) Math.round(-netConverted) + " " + currency + " in " + String.join(", ", groupsInvolved) + "!");
        } else {
            return new Result(true, otherUsername + " owes you " + (int) Math.round(netConverted) + " " + currency + " in " + String.join(", ", groupsInvolved) + "!");
        }
    }

    public Result settleUp(String otherUsername, int inputMoney, User currentUser) {
        List<Group> commonGroups = new ArrayList<>();
        for (Group group : App.groups) {
            if (group.getMembers().contains(currentUser) &&
                    group.getMembers().stream().anyMatch(u -> u.getUsername().equals(otherUsername))) {
                commonGroups.add(group);
            }
        }
        if(commonGroups.isEmpty()){
            return new Result(false, "no common groups found!");
        }
        Currency currentUserCurr = Currency.fromPattern(currentUser.getCurrency());
        double settlementTotal = inputMoney / (double) currentUserCurr.getRate();
        double perGroup = settlementTotal / commonGroups.size();

        for (Group group : commonGroups) {
            Map<String, Double> settlementMap = new HashMap<>();
            settlementMap.put(otherUsername, perGroup);
            Expense settlementExpense = new Expense(group.getId(), "settlement", perGroup, settlementMap, currentUser.getUsername());
            group.addExpense(settlementExpense);
        }
        double net = 0.0;
        Set<String> groupsInvolved = new LinkedHashSet<>();
        for (Group group : App.groups) {
            boolean isCurrentUserInGroup = group.getMembers().contains(currentUser);
            boolean isOtherUserInGroup = group.getMembers().stream().anyMatch(u -> u.getUsername().equals(otherUsername));
            if (isCurrentUserInGroup && isOtherUserInGroup) {
                for (Expense expense : group.getExpenses()) {
                    if (expense.getPayer().equals(currentUser.getUsername())) {
                        Double amt = expense.getUserExpenses().get(otherUsername);
                        if (amt != null) {
                            net += amt;
                            groupsInvolved.add(group.getName());
                        }
                    }
                    if (expense.getPayer().equals(otherUsername)) {
                        Double amt = expense.getUserExpenses().get(currentUser.getUsername());
                        if (amt != null) {
                            net -= amt;
                            groupsInvolved.add(group.getName());
                        }
                    }
                }
            }
        }
        double netConverted = net * currentUserCurr.getRate();
        String currency = currentUser.getCurrency();
        if (Math.abs(netConverted) < 0.0001) {
            return new Result(true, "you are settled with " + otherUsername + " now!");
        } else if (netConverted < 0) {
            return new Result(true, "you owe " + otherUsername + " " + (int) Math.round(-netConverted) + " " + currency + " now!");
        } else {
            return new Result(true, otherUsername + " owes you " + (int) Math.round(netConverted) + " " + currency + " now!");
        }
    }

    public Result goToProfileMenu() {
        Menu.ProfileMenu.updateProfileMenuUser();
        App.setCurrentMenu(Menu.ProfileMenu);
        return new Result(true, "you are now in profile menu!");
    }

    public Result logout() {
        App.currentUser = null;
        App.setCurrentMenu(Menu.LoginMenu);
        return new Result(true, "user logged out successfully.you are now in login menu!");
    }

    public Result exit() {
        App.setCurrentMenu(Menu.ExitMenu);
        return new Result(true, "");
    }
}
