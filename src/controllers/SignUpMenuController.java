package controllers;

import models.App;
import models.Result;
import models.User;
import models.enums.Menu;

import java.util.regex.Pattern;


public class SignUpMenuController {
    public Result register(String username, String password, String email, String name){
        if (!isValidUsername(username)){
            return new Result(false, "username format is invalid!");
        }

        for (User users : App.users) {
            if (users.getUsername().equals(username)){
                return new Result(false, "this username is already taken!");
            }
        }

        if (!isValidPassword(password)) {
            return new Result(false, "password format is invalid!");
        }

        if (!isValidEmail(email)) {
            return new Result(false, "email format is invalid!");
        }

        if (!isValidName(name)) {
            return new Result(false, "name format is invalid!");
        }

        App.users.add(new User(username, name, password, email));
        App.setCurrentMenu(Menu.LoginMenu);
        return new Result(true, "user registered successfully.you are now in login menu!");
    }

    public Result loginToMenu() {
        App.setCurrentMenu(Menu.LoginMenu);
        return new Result(true, "you are now in login menu!");
    }

    public Result exit() {
        App.setCurrentMenu(Menu.ExitMenu);
        return new Result(true, "");
    }
    private boolean isValidUsername(String username) {
        String usernamePattern = "^[A-Za-z][A-Za-z0-9._-]{3,9}$";
        return Pattern.matches(usernamePattern, username);
    }

    private boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{6,12}$";
        return Pattern.matches(passwordPattern, password);
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "^([A-Za-z][A-Za-z0-9._-]{3,9})@([A-Za-z]([A-Za-z.-]*[A-Za-z])?){2,6}\\.(org|com|net|edu)$";
        return Pattern.matches(emailPattern, email);
    }

    private boolean isValidName(String name) {
        String namePattern = "^[A-Za-z]([A-Za-z-]*[A-Za-z])?$";
        return Pattern.matches(namePattern, name);
    }
}
