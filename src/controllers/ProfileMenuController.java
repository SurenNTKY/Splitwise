package controllers;

import models.*;
import models.enums.Currency;
import models.enums.Menu;

import java.util.regex.Pattern;

public class ProfileMenuController {

    public Result showUserInfo(User user) {
        String info = "username : " + user.getUsername() + "\n" +
                "password : " + user.getPassword() + "\n" +
                "currency : " + user.getCurrency() + "\n" +
                "email : " + user.getEmail() + "\n" +
                "name : " + user.getName();
        return new Result(true, info);
    }

    public Result changeCurrency(String newCurrency, User user) {
        if (user == null) {
            return new Result(false, "Error: User not found! Please log in first.");
        }

        Currency targetCurrency = Currency.fromPattern(newCurrency);
        if (targetCurrency == null) {
            return new Result(false, "currency format is invalid!");
        }

        user.setCurrency(newCurrency);

        return new Result(true, "your currency changed to " + newCurrency + " successfully!");
    }

    public Result changeUsername(String newUsername, User user) {
        if (user.getUsername().equals(newUsername)) {
            return new Result(false, "please enter a new username!");
        }
        for (User u : App.users) {
            if (u.getUsername().equals(newUsername)) {
                return new Result(false, "this username is already taken!");
            }
        }
        String usernamePattern = "^[A-Za-z][A-Za-z0-9._-]{3,9}$";
        if (!Pattern.matches(usernamePattern, newUsername)) {
            return new Result(false, "new username format is invalid!");
        }
        user.setUsername(newUsername);
        return new Result(true, "your username changed to " + newUsername + " successfully!");
    }

    public Result changePassword(String oldPassword, String newPassword, User user) {
        if (!user.getPassword().equals(oldPassword)) {
            return new Result(false, "password incorrect!");
        }
        if (oldPassword.equals(newPassword)) {
            return new Result(false, "please enter a new password!");
        }
        String passwordPattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{6,12}$";
        if (!Pattern.matches(passwordPattern, newPassword)) {
            return new Result(false, "new password format is invalid!");
        }
        user.setPassword(newPassword);
        return new Result(true, "your password changed successfully!");
    }

    public Result backToDashboard() {
        App.setCurrentMenu(models.enums.Menu.Dashboard);
        return new Result(true, "you are now in dashboard!");
    }

    public Result exit() {
        App.setCurrentMenu(Menu.ExitMenu);
        return new Result(true, "");
    }
}
