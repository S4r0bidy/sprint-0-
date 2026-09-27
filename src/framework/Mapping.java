package framework;

public class Mapping {
    private String className;
    private String methodName;
    private String httpMethod;
    private String controllerType; // "view" ou "api"

    public Mapping(String className, String methodName, String httpMethod, String controllerType) {
        this.className = className;
        this.methodName = methodName;
        this.httpMethod = httpMethod;
        this.controllerType = controllerType;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public String getControllerType() {
        return controllerType;
    }

    public void setControllerType(String controllerType) {
        this.controllerType = controllerType;
    }
}