package framework;

import framework.Mapping;
import framework.annotation.Controller;
import framework.annotation.GetMapping;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FrontController extends HttpServlet {

        private HashMap<String, Mapping> routes = new HashMap<>();

        @Override
        public void init() {
                try {
                        System.out.println("=== Scan des controllers ===");
                        scanController("controller.UserController");
                        System.out.println("=== Fin scan des controllers ===");
                } catch (Exception e) {
                        e.printStackTrace();
                }
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
                        Mapping mapping = routes.get(url);
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

                        response.getWriter().println("Méthode appelée: " + methodName);
                        response.getWriter().println(result);

                } catch (Exception e) {
                        e.printStackTrace();

                        response.getWriter()
                                        .println("Erreur : " + e.getMessage());
                }
        }

        private void scanController(
                        String className)
                        throws Exception {

                Class<?> clazz = Class.forName(className);

                System.out.println("Controller trouvé: " + clazz.getName());

                if (!clazz.isAnnotationPresent(
                                framework.annotation.Controller.class)) {

                        System.out.println("Ignoré (pas @Controller): " + clazz.getName());
                        return;
                }

                Method[] methods = clazz.getDeclaredMethods();

                for (Method method : methods) {

                        if (!method.isAnnotationPresent(framework.annotation.GetMapping.class)) {
                                continue;
                        }

                        framework.annotation.GetMapping gm = method
                                        .getAnnotation(framework.annotation.GetMapping.class);

                        String url = gm.value();

                        routes.put(url, new Mapping(clazz.getName(), method.getName()));

                        // Affiche la route + méthode appelée
                        System.out.println("Route: " + url + " -> " + clazz.getName() + "#" + method.getName());
                }
        }
}