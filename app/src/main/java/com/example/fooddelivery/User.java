package com.example.fooddelivery;

public class User {

    String name, password, email, phone;

    public User(String name,String email,String phone,String password)
    {
        this.name = name;
        this.password = password;
        this.email = email;
        this.phone = phone;
    }
    public User()
    {

    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }
}
