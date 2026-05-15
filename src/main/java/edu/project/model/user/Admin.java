package edu.project.model.user;

import edu.project.storage.FileManager;

public class Admin extends User{
    public Admin(String id, String password) {
        super(id, password);
    }

    //todo: method
    public String[][] getTable(){
        return FileManager.getAnalytics();
    }

    //todo: method
    public String[][] generateAnalytics(){
        return FileManager.getRegionalAnalytics();
    }
}
