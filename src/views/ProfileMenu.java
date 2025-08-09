package views;

import controllers.ProfileMenuController;
import models.Result;
import models.User;
import models.enums.ProfileMenuCommands;
import models.enums.SignUpMenuCommands;

import java.util.Scanner;
import java.util.regex.Matcher;

public class ProfileMenu implements AppMenu {
    private final ProfileMenuController controller = new ProfileMenuController();
    private final User currentUser;

    public ProfileMenu(User currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    public void check(Scanner scanner) {
        String input = scanner.nextLine().trim();
        Matcher matcher;
        if ((matcher = ProfileMenuCommands.ShowUserInfo.getMatcher(input)) != null) {
            Result res = controller.showUserInfo(currentUser);
            System.out.println(res.getMessage());
        } else if ((matcher = ProfileMenuCommands.ChangeCurrency.getMatcher(input)) != null) {
            String newCurrency = matcher.group(1);
            Result res = controller.changeCurrency(newCurrency, currentUser);
            System.out.println(res.getMessage());
        } else if ((matcher = ProfileMenuCommands.ChangeUsername.getMatcher(input)) != null) {
            String newUsername = matcher.group(1);
            Result res = controller.changeUsername(newUsername, currentUser);
            System.out.println(res.getMessage());
        } else if ((matcher = ProfileMenuCommands.ChangePassword.getMatcher(input)) != null) {
            String oldPassword = matcher.group(1);
            String newPassword = matcher.group(2);
            Result res = controller.changePassword(oldPassword, newPassword, currentUser);
            System.out.println(res.getMessage());
        } else if ((matcher = ProfileMenuCommands.BackToDashboard.getMatcher(input)) != null) {
            Result res = controller.backToDashboard();
            System.out.println(res.getMessage());
        } else if (input.equalsIgnoreCase("exit")) {
            System.exit(0);
        } else if ((matcher = ProfileMenuCommands.Exit.getMatcher(input)) != null) {
            System.out.println(controller.exit());
        } else {
            System.out.println("invalid command!");
        }
    }
}
