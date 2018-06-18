package com.konovalov.foto.user;


import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
// костяк+бд
@Entity
public class CustomUser {
    @Id
    @GeneratedValue
    private long id;

    private String login;
    private String password;

    @Enumerated(EnumType.STRING)   // использовать роли
    private UserRole role;

    @Email   // проверка коректное ввод почты
    private String email;

    public CustomUser() {
    }

    public CustomUser(String login, String password, UserRole role) {
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public CustomUser(String login, String password, UserRole role, String email) {
        this.login = login;
        this.password = password;
        this.role = role;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
