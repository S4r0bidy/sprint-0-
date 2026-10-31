package controller;

import framework.JsonBuilder;
import framework.annotation.ApiController;
import framework.annotation.Authenticated;
import framework.annotation.GetMapping;
import framework.annotation.PostMapping;
import model.User;
import service.AuthService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@ApiController
public class AuthController {

    private final AuthService authService;

    public AuthController() {
        this.authService = AuthService.getInstance();
    }

    @PostMapping("/login")
    public String login(
            HttpServletRequest req,
            HttpServletResponse res
    ) {

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (username == null || username.trim().isEmpty()
                || password == null || password.isEmpty()) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new JsonBuilder()
                    .put("status", "error")
                    .put("message", "Identifiants manquants")
                    .toString();
        }

        User user = authService.authenticate(username, password);

        if (user == null) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return new JsonBuilder()
                    .put("status", "error")
                    .put("message", "Identifiants invalides")
                    .toString();
        }

        // Évite de conserver l'ancien identifiant de session
        // après l'authentification.
        HttpSession oldSession = req.getSession(false);

        if (oldSession != null) {
            oldSession.invalidate();
        }

        HttpSession session = req.getSession(true);
        session.setAttribute("AUTH_USER_ID", user.getId());
        session.setAttribute("AUTH_USERNAME", user.getUsername());
        session.setAttribute("AUTH_ROLE", user.getRole());

        res.setStatus(HttpServletResponse.SC_OK);
        return new JsonBuilder()
                .put("status", "success")
                .put("message", "Connexion réussie")
                .toString();
    }

    @PostMapping("/logout")
    public String logout(
            HttpServletRequest req,
            HttpServletResponse res
    ) {

        HttpSession session = req.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        res.setStatus(HttpServletResponse.SC_OK);
        return new JsonBuilder()
                .put("status", "success")
                .put("message", "Déconnexion réussie")
                .toString();
    }

    @Authenticated
    @GetMapping("/me")
    public String me(
            HttpServletRequest req,
            HttpServletResponse res
    ) {

        HttpSession session = req.getSession(false);

        if (session == null) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return new JsonBuilder()
                    .put("status", "error")
                    .put("message", "Authentification requise")
                    .toString();
        }

        Object username = session.getAttribute("AUTH_USERNAME");

        return new JsonBuilder()
                .put("username", username == null ? "" : username.toString())
                .toString();
    }
}
