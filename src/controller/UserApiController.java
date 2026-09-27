package controller;

import framework.annotation.ApiController;
import framework.annotation.GetMapping;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;

@ApiController
public class UserApiController {

    @GetMapping("/api/users")
    public String getUsersJson() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("users", new String[]{"Alice", "Bob", "Charlie"});

        return new Gson().toJson(response); // Retourne JSON directement
    }
}