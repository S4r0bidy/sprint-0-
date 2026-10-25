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
import framework.annotation.Session;
import javax.servlet.http.HttpSession;


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
        if (url.isEmpty())
            url = "/";

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
            if (method.getName().equals(methodName))
                return method;
        }
        throw new IllegalArgumentException("Action introuvable: " + methodName);
    }

    private Object[] buildArguments(Method actionMethod, HttpServletRequest req,
            HttpServletResponse res) throws IOException {
        
private Object[] buildArguments(
        Method actionMethod,
        HttpServletRequest req,
        HttpServletResponse res
) throws IOException, ServletException {

    Parameter[] parameters = actionMethod.getParameters();
    Object[] arguments = new Object[parameters.length];

    for (int i = 0; i < parameters.length; i++) {
        Parameter parameter = parameters[i];
        Class<?> type = parameter.getType();

        // 1. Injection de HttpServletRequest
        if (type.equals(HttpServletRequest.class)) {
            arguments[i] = req;
        }

        // 2. Injection de HttpServletResponse
        else if (type.equals(HttpServletResponse.class)) {
            arguments[i] = res;
        }

        // 3. Injection d'un fichier uploadé
        else if (type.equals(UploadedFile.class)) {
            RequestFile annotation =
                    parameter.getAnnotation(RequestFile.class);

            String fieldName =
                    annotation == null ? "file" : annotation.value();

            Part part = req.getPart(fieldName);

            if (part == null || part.getSize() == 0) {
                throw new IllegalArgumentException(
                        "Fichier manquant : " + fieldName
                );
            }

            arguments[i] = UploadedFile.fromPart(part);
        }

        // 4. Injection d'un attribut de session
        else if (parameter.isAnnotationPresent(Session.class)) {
            Session annotation = parameter.getAnnotation(Session.class);

            HttpSession httpSession = req.getSession(false);

            if (httpSession == null) {
                throw new IllegalStateException(
                        "Aucune session active pour la clé : "
                                + annotation.value()
                );
            }

            Object value =
                    httpSession.getAttribute(annotation.value());

            if (value == null) {
                throw new IllegalStateException(
                        "Attribut absent de la session : "
                                + annotation.value()
                );
            }

            if (!type.isInstance(value)) {
                throw new IllegalArgumentException(
                        "Type incompatible pour l'attribut de session : "
                                + annotation.value()
                );
            }

            arguments[i] = value;
        }

        // 5. Paramètre non pris en charge
        else {
            throw new IllegalArgumentException(
                    "Paramètre non supporté dans l'action : "
                            + type.getName()
            );
        }
    }

    return arguments;
}

    }
}
