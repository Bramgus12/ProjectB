package com.bramgussekloo.projectb.helpers;

public class Userhelper {
    public String Role;
    public String Name;
    public String Email;

    public Userhelper() {
    }

    public String getUserRole(){
        return Role;
    }
    public void setUserRole(String Role){
        this.Role = Role;
    }
    public String getUserName(){
        return Name;
    }
    public void setUserName(String Name) {
        this.Name = Name;
    }
    public String getUserEmail(){
        return Email;
    }
    public void setUserEmail(String Email){
        this.Email = Email;
    }



}

