package framework;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.Part;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

import framework.annotation.ApiController;
import framework.annotation.RequestFile;

@WebServlet("/*")
@MultipartConfig(maxFileSize = 10 * 1024 * 1024, maxRequestSize = 12 * 1024 * 1024)
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
            res.setContentType("application/json; charset=UTF-8");
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            res.getWriter().write("{\"status\":\"error\",\"message\":\"Route not found: " + method + " " + url + "\"}");
            return;
        }

        try {
            Mapping mapping = routes.get(url);
            Class<?> controllerClass = Class.forName(mapping.getClassName());

            if (!controllerClass.isAnnotationPresent(ApiController.class)) {
                res.setContentType("application/json; charset=UTF-8");
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                res.getWriter().write("{\"status\":\"error\",\"message\":\"Controller non autorisé: "
                        + controllerClass.getName() + " n'est pas annoté @ApiController\"}");
                return;
            }

            Object controller = controllerClass.getDeclaredConstructor().newInstance();

            Method actionMethod = findActionMethod(controllerClass, mapping.getMethodName());
            Object result = actionMethod.invoke(controller, buildArguments(actionMethod, req, res));

            // Réponse JSON
            res.setContentType("application/json; charset=UTF-8");
            res.getWriter().write((String) result);

        } catch (Exception e) {
            System.err.println("[FrontController] Error: " + e.getMessage());
            e.printStackTrace();
            res.setContentType("application/json; charset=UTF-8");
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            res.getWriter().write("{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    private Method findActionMethod(Class<?> controllerClass, String methodName) {
        for (Method method : controllerClass.getDeclaredMethods()) {
            if (method.getName().equals(methodName)) return method;
        }
        throw new IllegalArgumentException("Action introuvable: " + methodName);
    }

    private Object[] buildArguments(Method actionMethod, HttpServletRequest req,
                                    HttpServletResponse res) throws IOException {
        Parameter[] parameters = actionMethod.getParameters();
        Object[] arguments = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Class<?> type = parameters[i].getType();
            if (type.equals(HttpServletRequest.class)) {
                arguments[i] = req;
            } else if (type.equals(HttpServletResponse.class)) {
                arguments[i] = res;
            } else if (type.equals(UploadedFile.class)) {
                RequestFile annotation = parameters[i].getAnnotation(RequestFile.class);
                String fieldName = annotation == null ? "file" : annotation.value();
                Part part = req.getPart(fieldName);
                if (part == null || part.getSize() == 0) {
                    throw new IllegalArgumentException("Fichier manquant: " + fieldName);
                }
                arguments[i] = UploadedFile.fromPart(part);
            } else {
                throw new IllegalArgumentException("Paramètre non supporté dans l'action: " + type.getName());
            }
        }
        return arguments;
    }
}
