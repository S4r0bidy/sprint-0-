package framework;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

import java.io.IOException;

@WebServlet(urlPatterns = "/*")
public class FrontController extends HttpServlet {

    @Override
    public void init() {

        routes.put(
                "/users",
                "controller.UserController:list");
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // Récupération de l'URL demandée
        String uri = request.getRequestURI();

        // Récupération du contexte de l'application
        String context = request.getContextPath();

        // Extraction de la partie de l'URL après le contexte
        String url = uri.substring(context.length());

        // Affichage de l'URL dans la réponse
        response.getWriter().println(
                "FrontController appelé"
                        + " pour l'URL : " + url);
    }
}
