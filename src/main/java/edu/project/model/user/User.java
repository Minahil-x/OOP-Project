package edu.project.model.user;

public class User {
    protected String password;
    protected String id;
    protected boolean loginStatus;

    User(String password, String id) {
        this.password = password;
        this.id = id;
        this.loginStatus = false;
    }

    public String getPassword() {return password;}
    public String getId() {return id;}
    public boolean isLoginStatus() {return loginStatus;}

    public void setLoginStatus(boolean loginStatus) {this.loginStatus = loginStatus;}
    public void setPassword(String password) {this.password = password;}
    public void setId(String id) {this.id = id;}
}
