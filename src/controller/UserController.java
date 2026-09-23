package controller;

import framework.annotation.*;

@ApiRest
public class UserController {

    @GetMapping("/bonjour")
    public String direBonjour() {
        // Logique pour lister les utilisateurs
        return "Bonjour !";
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

