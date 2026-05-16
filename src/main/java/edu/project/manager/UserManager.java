package edu.project.manager;

import edu.project.exceptions.ValidationError;
import edu.project.model.user.User;

import java.io.Serializable;
import java.util.ArrayList;

public class UserManager implements Serializable {
    private ArrayList<User> users;

    public UserManager() {
        users = new ArrayList<>();
    }

    public void register(User user) throws ValidationError {
        for(User u : users){
            if(u.getId().equals(user.getId())){
                throw new ValidationError("User " + user.getId() + " already exists.");
            }
        }
        users.add(user);
    }

    public User login(String id, String password) throws ValidationError {
        for(User u : users){
            if(u.getId().trim().equals(id.trim()) && u.getPassword().trim().equals(password.trim())){
                u.setLoginStatus(true);
                return u;
            }
        }
        throw new ValidationError("User " + id + " doesn't exist or password is incorrect.");
    }

    public void logout(User user){
        for(User u : users){
            if(u.getId().equals(user.getId())){
                u.setLoginStatus(false);
            }
        }
    }

    public  ArrayList<User> getUserList() {
        return users;
    }
}
