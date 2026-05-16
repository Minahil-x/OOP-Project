package edu.project;

import edu.project.gui.LoginFrame;
import edu.project.storage.FileManager;

public class Main {
    public static void main(String[] args) {
        FileManager.init();
        new LoginFrame(FileManager.loadUserManager()).setVisible(true);
    }
}
