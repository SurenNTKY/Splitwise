package views;

import controllers.SignUpMenuController;
import models.enums.SignUpMenuCommands;

import java.util.Scanner;
import java.util.regex.Matcher;

public class SignUpMenu implements AppMenu {
    private final SignUpMenuController controller = new SignUpMenuController();

    @Override
    public void check(Scanner scanner) {
        String input = scanner.nextLine().trim();
        Matcher matcher;
        if ((matcher = SignUpMenuCommands.Register.getMatcher(input)) != null) {
            System.out.println(controller.register(
                    matcher.group("username"),
                    matcher.group("password"),
                    matcher.group("email"),
                    matcher.group("name")
            ));
        } else if ((matcher = SignUpMenuCommands.LoginToMenu.getMatcher(input)) != null) {
            System.out.println(controller.loginToMenu());
        } else if ((matcher = SignUpMenuCommands.Exit.getMatcher(input)) != null) {
        System.out.println(controller.exit());
        } else {
            System.out.println("invalid command!");
        }
    }
}
