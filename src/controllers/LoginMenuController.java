package controllers;

import models.App;
import models.Result;
import models.User;
import models.enums.Menu;
import views.Dashboard;

public class LoginMenuController {
    public Result login(String username, String password) {
        for (User user : App.users) {
            if (user.getUsername().equals(username)) {
                if (!user.getPassword().equals(password)) {
                    return new Result(false, "password is incorrect!");
                }
                App.currentUser = user;
                App.setCurrentMenu(Menu.Dashboard);
                Dashboard dashboard = new Dashboard();
                return new Result(true, "user logged in successfully.you are now in dashboard!");
            }
        }
        return new Result(false, "username doesn't exist!");
    }

    public Result forgetPassword(String username, String email) {
        for (User user : App.users) {
            if (user.getUsername().equals(username)) {
                if (!user.getEmail().equals(email)) {
                    return new Result(false, "email doesn't match!");
                }
                return new Result(true, "password : " + user.getPassword());
            }
        }
        return new Result(false, "username doesn't exist!");
    }

    public Result backToSignUpMenu() {
        App.setCurrentMenu(Menu.SignUpMenu);
        return new Result(true, "you are now in signup menu!");
    }

    public Result exit() {
        App.setCurrentMenu(Menu.ExitMenu);
        return new Result(true, "");
    }
}


