package framework;

import framework.Mapping;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class FrontController extends HttpServlet {


        private HashMap<String, Mapping> routesGet() {
                Object value = getServletContext().getAttribute(ControllerScannerListener.ATTR_ROUTES_GET);
                if (value instanceof HashMap<?, ?>) {
                        return (HashMap<String, Mapping>) value;
                }
                return new HashMap<>();
        }

        private HashMap<String, Mapping> routesPost() {
                Object value = getServletContext().getAttribute(ControllerScannerListener.ATTR_ROUTES_POST);
                if (value instanceof HashMap<?, ?>) {
                        return (HashMap<String, Mapping>) value;
                }
                return new HashMap<>();
        }



        private Mapping resolveRoute(String url, String httpMethod) {
                if ("GET".equals(httpMethod)) {
                        return routesGet().get(url);
                }
                if ("POST".equals(httpMethod)) {
                        return routesPost().get(url);
                }

                return null;
        }






        @Override
        protected void doGet(
                        HttpServletRequest request,
                        HttpServletResponse response)
                        throws ServletException, IOException {


                try {
                        // Récupérer l'URL demandée
                        String uri = request.getRequestURI();

                        // Récupérer le contexte de l'application
                        String context = request.getContextPath();


                        // Extraire la partie de l'URL après le contexte
                        String url = uri.substring(context.length());
                        if (url == null || url.isEmpty()) {
                                url = "/";
                        }

                        // Afficher l'URL demandée
                        response.getWriter().println(
                                        "URL : " + uri);


                        response.getWriter().println(
                                        "URL demandée : " + url);

                        // Trouver la route correspondante
                        Mapping mapping = routesGet().get(url);

                        if (mapping == null) {
                                response.getWriter().println("404 - Route introuvable : " + url);
                                return;
                        }

                        Class<?> clazz = Class.forName(mapping.getClassName());
                        Object controller = clazz.getDeclaredConstructor().newInstance();

                        // Récupérer la méthode par son nom
                        String methodName = mapping.getMethodName();
                        System.out.println("Appel: " + mapping.getClassName() + "#" + methodName);

                        Method method = clazz.getMethod(methodName);
                        Object result = method.invoke(controller);

                        response.getWriter().println("HTTP method utilisée: GET");
                        response.getWriter().println("Méthode appelée: " + methodName);
                        response.getWriter().println(result);

                } catch (Exception e) {
                        e.printStackTrace();

                        response.getWriter()
                                        .println("Erreur : " + e.getMessage());
                }
        }



        @Override
        protected void doPost(
                        HttpServletRequest request,
                        HttpServletResponse response)
                        throws ServletException, IOException {

                try {
                        // Récupérer l'URL demandée
                        String uri = request.getRequestURI();

                        // Récupérer le contexte de l'application
                        String context = request.getContextPath();

                        // Extraire la partie de l'URL après le contexte
                        String url = uri.substring(context.length());
                        if (url == null || url.isEmpty()) {
                                url = "/";
                        }

                        // Afficher l'URL demandée
                        response.getWriter().println(
                                        "URL : " + uri);

                        response.getWriter().println(
                                        "URL demandée : " + url);

                        // Trouver la route correspondante
                        Mapping mapping = routesPost().get(url);

                        if (mapping == null) {
                                response.getWriter().println("404 - Route introuvable : " + url);
                                return;
                        }

                        Class<?> clazz = Class.forName(mapping.getClassName());
                        Object controller = clazz.getDeclaredConstructor().newInstance();

                        // Récupérer la méthode par son nom
                        String methodName = mapping.getMethodName();
                        System.out.println("Appel: " + mapping.getClassName() + "#" + methodName);

                        Method method = clazz.getMethod(methodName);
                        Object result = method.invoke(controller);

                        response.getWriter().println("HTTP method utilisée: POST");
                        response.getWriter().println("Méthode appelée: " + methodName);
                        response.getWriter().println(result);

                } catch (Exception e) {
                        e.printStackTrace();
                        response.getWriter().println("Erreur : " + e.getMessage());
                }
        }
}
