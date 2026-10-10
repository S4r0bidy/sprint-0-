package framework;


public class StaticBindingDemo {

    // 1) Surcharge de méthodes : le choix se fait à la compilation,
    // d'après le type déclaré de l'argument.

    public static String overloaded(Object arg) {
        return "overloaded(Object)";
    }

    public static String overloaded(String arg) {
        return "overloaded(String)";
    }

    public static String overloaded(Integer arg) {
        return "overloaded(Integer)";
    }

    // 2) Méthode statique : liée à la classe, jamais override.

    public static String staticMethodInfo() {
        return "Une méthode static est résolue à la compilation, via la classe.";
    }


    public static String buildResponse() {
        // Le type DÉCLARÉ de la variable détermine la surcharge retenue.
        Object asObject = "une chaîne"; // type déclaré : Object
        String asString = "une chaîne"; // type déclaré : String
        Integer asInteger = 42; // type déclaré : Integer

        String overloadResult = overloaded(asObject) // -> overloaded(Object)
                + " / " + overloaded(asString) // -> overloaded(String)
                + " / " + overloaded(asInteger); // -> overloaded(Integer)

        return new JsonBuilder()
                .put("status", "success")
                .put("concept", "Binding statique")
                .toString();
    }
}
