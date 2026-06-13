package framework;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FrontController extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // Récupérer l'URL demandée
        String uri = request.getRequestURI();

        // Récupérer le contexte de l'application
        String context = request.getContextPath();

        // Extraire la partie de l'URL après le contexte
        String url = uri.substring(context.length());

        response.getWriter().println(
            "FrontController appelé"
        );
        
        // Afficher l'URL demandée
        response.getWriter().println(
        "URL : " + uri);

        response.getWriter().println(
        "Contexte : " + context);

        response.getWriter().println(
        "URL demandée : " + url);
    }
}