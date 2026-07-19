package controller;

import framework.annotation.*;

@Controller
public class UserController {

    @GetMapping("/users")
    public String list() {
        // Nom de vue (sera transformé en path via prefix/suffix)
        return "users";
    }
    
    @GetMapping("/users/test")
    public String testGet() {
        return "users/test";
    }

    @PostMapping("/users/test")
    public String testPost() {
        return "users/test";
    }
}

