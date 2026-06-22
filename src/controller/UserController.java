package controller;

import framework.annotation.*;

@Controller
public class UserController {

    @GetMapping("/users")
    public String list() {

        return "Bonjour depuis UserController";
    }
    @GetMapping("/users/test")
    public String test() {

        return "Test depuis UserController";
    }
}