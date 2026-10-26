package controller;

import framework.DynamicBindingDemo;
import framework.JsonBuilder;
import framework.StaticBindingDemo;
import framework.UploadedFile;
import framework.annotation.ApiController;
import framework.annotation.GetMapping;
import framework.annotation.PostMapping;
import framework.annotation.RequestFile;
import java.util.Arrays;
import framework.SessionData;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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

    
@PostMapping("/api/session/demo")
public String createSession(HttpServletRequest request) {
    SessionData session = SessionData.get(request);

    session.set("username", "Sarobidy");
    session.set("role", "USER");

    return new JsonBuilder()
        .put("status", "success")
        .put("message", "Session créée")
        .put("username", session.getString("username"))
        .put("role", session.getString("role"))
        .toString();
}

@GetMapping("/api/session")
public String getSessionInfo(HttpServletRequest request) {
    HttpSession httpSession = request.getSession(false);

    if (httpSession == null) {
        return new JsonBuilder()
            .put("status", "error")
            .put("message", "Aucune session active")
            .toString();
    }

    SessionData session = SessionData.get(request);

    if (!session.contains("username")) {
        return new JsonBuilder()
            .put("status", "error")
            .put("message", "Aucune donnée utilisateur en session")
            .toString();
    }

    return new JsonBuilder()
        .put("status", "success")
        .put("username", session.getString("username"))
        .put("role", session.getString("role"))
        .toString();
}

    @PostMapping("/api/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        return new JsonBuilder()
            .put("status", "success")
            .put("message", "Déconnexion effectuée")
            .toString();
    }

    @GetMapping("/api/session/id")
    public String getSessionId(HttpServletRequest request) {
        HttpSession httpSession = request.getSession(false);
        if (httpSession == null) {
            return new JsonBuilder()
                .put("status", "error")
                .put("message", "Aucune session active")
                .put("hasSession", false)
                .toString();
        }
        return new JsonBuilder()
            .put("status", "success")
            .put("sessionId", httpSession.getId())
            .put("creationTime", httpSession.getCreationTime())
            .put("lastAccessedTime", httpSession.getLastAccessedTime())
            .put("maxInactiveInterval", httpSession.getMaxInactiveInterval())
            .put("isNew", httpSession.isNew())
            .put("hasSession", true)
            .toString();
    }

    @GetMapping("/api/session/attributes")
    public String getAllSessionAttributes(HttpServletRequest request) {
        HttpSession httpSession = request.getSession(false);
        if (httpSession == null) {
            return new JsonBuilder()
                .put("status", "error")
                .put("message", "Aucune session active")
                .toString();
        }
        JsonBuilder builder = new JsonBuilder()
            .put("status", "success")
            .put("sessionId", httpSession.getId());
        java.util.Enumeration<String> names = httpSession.getAttributeNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            Object value = httpSession.getAttribute(name);
            if (value == null) {
                builder.put(name, null);
            } else {
                builder.put(name, value.toString());
            }
        }
        return builder.toString();
    }

    @GetMapping("/api/session/test")
    public String testSession(HttpServletRequest request) {
        HttpSession httpSession = request.getSession(true);
        SessionData session = SessionData.get(request);

        session.set("testKey", "testValue-" + System.currentTimeMillis());

        Integer count = null;
        Object countObj = session.get("visitCount");
        if (countObj instanceof Number) {
            count = ((Number) countObj).intValue();
        }
        if (count == null) {
            count = 0;
        }
        count = count + 1;
        session.set("visitCount", count);

        return new JsonBuilder()
            .put("status", "success")
            .put("sessionId", httpSession.getId())
            .put("isNew", httpSession.isNew())
            .put("visitCount", count)
            .put("testKey", session.getString("testKey"))
            .put("message", "Test de session effectué")
            .toString();
    }

    }
