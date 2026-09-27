package controller;

import framework.annotation.ApiRest;
import framework.annotation.GetMapping;

@ApiRest
public class UserController {

    @GetMapping("/users")
    public String listUsers() {
        System.out.println("UserController.listUsers() called");
        return "users"; // → forward vers /WEB-INF/views/users.jsp
    }
}