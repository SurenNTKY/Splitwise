package models;

import models.enums.Menu;

import java.util.ArrayList;

public class App {

    public final static ArrayList<User> users = new ArrayList<>();
    public final static ArrayList<Group> groups = new ArrayList<>();
    public static User currentUser = null;

    private static Menu currentMenu = Menu.SignUpMenu;

    public static Menu getCurrentMenu() {
        return currentMenu;
    }

    public static void setCurrentMenu(Menu currentMenu) {
        App.currentMenu = currentMenu;
    }
}
