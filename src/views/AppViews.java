package views;

import models.App;
import models.enums.Menu;
import java.util.Scanner;

public class AppViews {

    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (App.getCurrentMenu() != Menu.ExitMenu) {
            App.getCurrentMenu().checkCommand(scanner);
        }
        scanner.close();
    }
}
