package com.konovalov.foto.Controll;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;




// контроллер  логина
@Controller
@RequestMapping("/login")
public class LoginController {
    @RequestMapping
    public String loginPage() {
        return "login";
    }
}
