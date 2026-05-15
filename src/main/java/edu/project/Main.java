package edu.project;

import edu.project.gui.LoginFrame;
import edu.project.manager.UserManager;
import edu.project.storage.FileManager;

public class Main {
    public static void main(String[] args) {
        FileManager.init();
        UserManager userManager = FileManager.loadUserManager();
        new LoginFrame(userManager).setVisible(true);
    }
}
