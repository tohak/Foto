package com.konovalov.foto.user;


// интерфейс работы с бд
public interface UserService {
    CustomUser getUserByLogin(String login);
    boolean existsByLogin(String login);
    void addUser(CustomUser customUser);
    void updateUser(CustomUser customUser);
    void getAndUpdateUser(String login, String email);
}
