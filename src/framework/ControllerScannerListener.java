package framework;

import framework.annotation.Controller;
import framework.annotation.GetMapping;
import framework.annotation.PostMapping;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import java.lang.reflect.Method;
import java.util.HashMap;


public class ControllerScannerListener implements ServletContextListener {


    public static final String ATTR_ROUTES_GET = "routesGet";
    public static final String ATTR_ROUTES_POST = "routesPost";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();

        HashMap<String, Mapping> routesGet = new HashMap<>();
        HashMap<String, Mapping> routesPost = new HashMap<>();

        System.out.println("=== Scan des controllers (Listener) ===");

        // Sans librairie de scan de classpath: on limite à une liste de packages connues.
        // Ici on garde 'controller' et on peut ajouter d'autres packages selon ton projet.
        scanKnownControllers(routesGet, routesPost, "controller");
        scanKnownControllers(routesGet, routesPost, "controllers");
        scanKnownControllers(routesGet, routesPost, "app.controller");

        // Démo: package actuel présent dans le projet
        safeAddController(routesGet, routesPost, "controller.UserController");

        context.setAttribute(ATTR_ROUTES_GET, routesGet);
        context.setAttribute(ATTR_ROUTES_POST, routesPost);

        System.out.println("=== Fin scan des controllers (Listener) ===");
    }

    private void scanKnownControllers(HashMap<String, Mapping> routesGet,
                                       HashMap<String, Mapping> routesPost,
                                       String basePackage) {
        // Pour ce sprint, on ne peut pas scanner récursivement le classpath sans utilitaire.
        // Donc: on tente une convention simple (noms connus) — à compléter si besoin.
        // Exemples courants: basePackage.UserController
        safeAddController(routesGet, routesPost, basePackage + ".UserController");
    }

    private void safeAddController(HashMap<String, Mapping> routesGet,
                                   HashMap<String, Mapping> routesPost,
                                   String className) {
        try {
            addController(routesGet, routesPost, className);
        } catch (ClassNotFoundException e) {
            // silencieux: controller non présent
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addController(HashMap<String, Mapping> routesGet,
                                HashMap<String, Mapping> routesPost,
                                String className) throws Exception {
        Class<?> clazz = Class.forName(className);

        System.out.println("Controller trouvé: " + clazz.getName());

        if (!clazz.isAnnotationPresent(Controller.class)) {
            System.out.println("Ignoré (pas @Controller): " + clazz.getName());
            return;
        }

        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(GetMapping.class)) {
                GetMapping gm = method.getAnnotation(GetMapping.class);
                String url = gm.value();
                routesGet.put(url, new Mapping(clazz.getName(), method.getName(), "GET"));
                System.out.println("Route: " + url + " [GET] -> " + clazz.getName() + "#" + method.getName());
            }

            if (method.isAnnotationPresent(PostMapping.class)) {
                PostMapping pm = method.getAnnotation(PostMapping.class);
                String url = pm.value();
                routesPost.put(url, new Mapping(clazz.getName(), method.getName(), "POST"));
                System.out.println("Route: " + url + " [POST] -> " + clazz.getName() + "#" + method.getName());
            }
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // rien
    }
}

