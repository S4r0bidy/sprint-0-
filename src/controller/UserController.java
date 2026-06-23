package controller;

import framework.annotation.*;

@Controller
public class UserController {

    @GetMapping("/users")
    public String list() {

        return "Appel de la methode list";
    }
    @GetMapping("/users/test")
    public String test() {

        return "Appel de la methode test";
    }
}