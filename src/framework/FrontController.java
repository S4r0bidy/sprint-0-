package framework;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;

@WebServlet(urlPatterns = "/*")
public class FrontController extends HttpServlet {

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
