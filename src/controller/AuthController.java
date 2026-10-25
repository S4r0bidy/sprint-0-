
package controller;

import framework.annotation.PostMapping;
import framework.annotation.GetMapping;
import framework.annotation.Authenticated;
import model.User;
import service.AuthService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthController {

    private final AuthService authService;

    public AuthController() {
        // À remplacer par l'initialisation adaptée
        // à ton implémentation du service et du repository.
        this.authService = /* ton implémentation */;
    }

    @PostMapping("/login")
    public void login(
            HttpServletRequest req,
            HttpServletResponse res
    ) throws IOException {

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (username == null || username.trim().isEmpty()
                || password == null || password.isEmpty()) {
            res.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "Identifiants manquants"
            );
            return;
        }

        User user = authService.authenticate(username, password);

        if (user == null) {
            res.sendError(
                HttpServletResponse.SC_UNAUTHORIZED,
                "Identifiants invalides"
            );
            return;
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
        res.setContentType("application/json;charset=UTF-8");
        res.getWriter().write(
            "{\"status\":\"success\",\"message\":\"Connexion réussie\"}"
        );
    }

    @PostMapping("/logout")
    public void logout(
            HttpServletRequest req,
            HttpServletResponse res
    ) throws IOException {

        HttpSession session = req.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        res.setStatus(HttpServletResponse.SC_OK);
        res.setContentType("application/json;charset=UTF-8");
        res.getWriter().write(
            "{\"status\":\"success\",\"message\":\"Déconnexion réussie\"}"
        );
    }

    @Authenticated
    @GetMapping("/me")
    public void me(
            HttpServletRequest req,
            HttpServletResponse res
    ) throws IOException {

        HttpSession session = req.getSession(false);

        res.setContentType("application/json;charset=UTF-8");
        res.getWriter().write(
            "{\"username\":\""
                + escapeJson(
                    (String) session.getAttribute("AUTH_USERNAME")
                )
                + "\"}"
        );
    }

    private String escapeJson(String value) {
        return value.replace("\\", "\\\\")
                    .replace("\"", "\\\"");
    }
}
