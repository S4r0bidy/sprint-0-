package framework;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

@WebServlet("/*")
public class FrontController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        handleRequest(req, res, "GET");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        handleRequest(req, res, "POST");
    }

    private void handleRequest(HttpServletRequest req, HttpServletResponse res, String method) 
            throws ServletException, IOException {
        
        // Récupère l'URL après le contexte
        String url = req.getRequestURI().substring(req.getContextPath().length());
        if (url.isEmpty()) url = "/";

        System.out.println("[FrontController] " + method + " " + url);

        // Cherche le mapping
        Map<String, Mapping> routes = ("GET".equals(method)) 
            ? (Map<String, Mapping>) getServletContext().getAttribute("routesGet")
            : (Map<String, Mapping>) getServletContext().getAttribute("routesPost");

        if (routes == null || !routes.containsKey(url)) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND, "Route not found: " + method + " " + url);
            return;
        }

        try {
            Mapping mapping = routes.get(url);
            Class<?> controllerClass = Class.forName(mapping.getClassName());
            Object controller = controllerClass.getDeclaredConstructor().newInstance();

            Method actionMethod = controllerClass.getDeclaredMethod(mapping.getMethodName());
            Object result = actionMethod.invoke(controller);

            // ✅ Gestion View vs API
            if ("api".equals(mapping.getControllerType())) {
                // Pour API : envoyer directement JSON (String)
                res.setContentType("application/json; charset=UTF-8");
                res.getWriter().write((String) result);
            } else {
                // Pour View MVC : interpréter comme JSP
                String viewName = (String) result;
                String viewPrefix = getServletContext().getInitParameter("viewPrefix");
                String viewSuffix = getServletContext().getInitParameter("viewSuffix");

                String jspPath = viewPrefix + viewName + viewSuffix;
                getServletContext().getRequestDispatcher(jspPath).forward(req, res);
            }

        } catch (Exception e) {
            System.err.println("[FrontController] Error: " + e.getMessage());
            e.printStackTrace();
            res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Controller error: " + e.getMessage());
        }
    }
}