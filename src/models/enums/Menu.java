package models.enums;

import models.App;
import views.AppMenu;
import views.LoginMenu;
import views.ProfileMenu;
import views.SignUpMenu;
import views.ExitMenu;
import views.Dashboard;

import java.util.Scanner;

public enum Menu {
    LoginMenu(new LoginMenu()),
    ProfileMenu(new ProfileMenu(App.currentUser)),
    SignUpMenu(new SignUpMenu()),
    ExitMenu(new ExitMenu()),
    Dashboard(new Dashboard());

    private AppMenu menu;

    Menu(AppMenu menu) {
        this.menu = menu;
    }

    public void updateProfileMenuUser() {
        if (this == ProfileMenu) {
            this.menu = new ProfileMenu(App.currentUser);
        }
    }

    public void checkCommand(Scanner scanner) {
        ProfileMenu profileMenu = new ProfileMenu(App.currentUser);
        this.menu.check(scanner);
    }
}
