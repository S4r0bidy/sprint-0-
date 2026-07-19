package controller;

import framework.annotation.*;

@Controller
public class UserController {

    @GetMapping("/users")
    public String list() {
        return "Appel de la methode list";
    }
    
    @GetMapping("/users/test")
    public String testGet() {
        return "Appel de la methode test GET";
    }

    @PostMapping("/users/test")
    public String testPost() {
        return "Appel de la methode test POST";
    }
}
