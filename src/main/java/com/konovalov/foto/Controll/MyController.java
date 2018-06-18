package com.konovalov.foto.Controll;


import com.konovalov.foto.user.CustomUser;
import com.konovalov.foto.user.UserRole;
import com.konovalov.foto.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

//  основной контроллер
@Controller
public class MyController {

    @Autowired
    private UserService userService;

    //главная страница работает только после авторизации
    @RequestMapping("/")
    public String index(Model model) {
        // достает юзера который авторизировался
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String login = user.getUsername();
        // достает юзера с бд по логину авторизированого юзера
        CustomUser dbUser = userService.getUserByLogin(login);
        // атрибуты использованый на страници
        model.addAttribute("login", login);
        model.addAttribute("roles", user.getAuthorities());
        model.addAttribute("email", dbUser.getEmail());

        return "index";
    }
    // обновляет данные
    //@RequestParam - обновляет параметр, required = false дает установку не обязательно
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@RequestParam(required = false) String email) {
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String login = user.getUsername();
        // по логину меняет имейл
        userService.getAndUpdateUser(login,email);
        // перенаправляет в корень сайта
        return "redirect:/";
    }

    // форма добавления пользователя с правами пользователя
    @RequestMapping(value = "/newuser", method = RequestMethod.POST)
    public String update(@RequestParam String login,
                         @RequestParam String password,
                         @RequestParam(required = false) String email,
                         Model model) {
        // проверка пользователя
        if (userService.existsByLogin(login)) {
            model.addAttribute("exists", true);
            return "register";
        }
        // кодирует пороль для БД
        ShaPasswordEncoder encoder = new ShaPasswordEncoder();
        String passHash = encoder.encodePassword(password, null);

        CustomUser dbUser = new CustomUser(login, passHash, UserRole.USER, email);
        userService.addUser(dbUser);

        return "redirect:/";
    }
//страница регистрации
    @RequestMapping("/register")
    public String register() {
        return "register";
    }
//страница админ, доступ только группе админ
    @RequestMapping("/admin")
    public String admin(){
        return "admin";
    }
//если не хватает прав доступа перенаправляет на эту
    @RequestMapping("/unauthorized")
    public String unauthorized(Model model){
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("login", user.getUsername());
        return "unauthorized";
    }
}
