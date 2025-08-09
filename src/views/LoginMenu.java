package views;

import controllers.LoginMenuController;
import models.User;
import models.enums.LoginMenuCommands;

import java.util.Scanner;
import java.util.regex.Matcher;

public class LoginMenu implements AppMenu {
    private final LoginMenuController controller = new LoginMenuController();
    private User currentUser;

    @Override
    public void check(Scanner scanner) {
        String input = scanner.nextLine().trim();

        Matcher matcher;
        if ((matcher = LoginMenuCommands.Login.getMatcher(input)) != null) {
            System.out.println(controller.login(
                    matcher.group("username"),
                    matcher.group("password")
            ));
        } else if ((matcher = LoginMenuCommands.ForgetPassword.getMatcher(input)) != null) {
            System.out.println(controller.forgetPassword(
                    matcher.group("username"),
                    matcher.group("email")
            ));
        } else if ((matcher = LoginMenuCommands.BackSignUpMenu.getMatcher(input)) != null) {
            System.out.println(controller.backToSignUpMenu());
        } else if ((matcher = LoginMenuCommands.Exit.getMatcher(input)) != null) {
            System.out.println(controller.exit());
        } else {
            System.out.println("invalid command!");
        }
    }
}
