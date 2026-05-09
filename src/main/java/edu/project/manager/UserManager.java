package edu.project.manager;

import edu.project.exceptions.ValidationError;
import edu.project.model.user.User;

import java.util.ArrayList;

public class UserManager {
    private ArrayList<User> users;

    public UserManager() {
        users = new ArrayList<>();
    }

    public void register(User user) throws ValidationError {
        for(User u : users){
            if(u.getId().equals(user.getId())){
                throw new ValidationError("user " + user.getId() + " already exists");
            }
        }
        users.add(user);
    }
}
