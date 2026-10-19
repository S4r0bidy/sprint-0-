package controller;

import framework.DynamicBindingDemo;
import framework.JsonBuilder;
import framework.UploadedFile;
import framework.StaticBindingDemo;
import framework.annotation.ApiController;
import framework.annotation.GetMapping;
import framework.annotation.PostMapping;
import framework.annotation.RequestFile;
import java.util.Arrays;

@ApiController
public class UserApiController {

    @GetMapping("/api/users")
    public String listUsers() {
        return new JsonBuilder()
            .put("status", "success")
            .put("data", Arrays.asList("Sarobidy", "Sandro", "Princio"))
            .put("message", "Nom d'utilisateurs récupérés avec succès")
            .toString();
    }

    @GetMapping("/api/users/count")
    public String getUserCount() {
        return new JsonBuilder()
            .put("status", "success")
            .put("count", 3)
            .put("message", "Total de nombre d'utilisateurs récupérés avec succès")
            .toString();
    }

    @PostMapping("/api/users")
    public String createUser() {
        return new JsonBuilder()
            .put("status", "success")
            .put("id", 4)
            .put("message", "User created successfully")
            .toString();
    }

    @PostMapping("/api/upload")
    public String upload(@RequestFile("file") UploadedFile file) {
        return new JsonBuilder()
            .put("status", "success")
            .put("filename", file.getOriginalFilename())
            .put("contentType", file.getContentType())
            .put("size", file.getSize())
            .put("message", "Fichier reçu avec succès")
            .toString();
    }

    @GetMapping("/api/static-binding")
    public String staticBinding() {
        return StaticBindingDemo.buildResponse();
    }

    @GetMapping("/api/dynamic-binding")
    public String dynamicBinding() {
        return DynamicBindingDemo.buildResponse();
    }
}
