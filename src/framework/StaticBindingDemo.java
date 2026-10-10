package framework;


public class StaticBindingDemo {

    // 1) Surcharge de méthodes : le choix se fait à la compilation,
    // d'après le type DECLARE de l'argument (et non son type reel).

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

        // --- Cas 1 : même valeur, type DECLARE différent -> surcharge différente ---
        Object asObject = "une chaîne"; // type déclaré : Object
        String asString = "une chaîne"; // type déclaré : String
        Integer asInteger = 42; // type déclaré : Integer

        String objectResult = overloaded(asObject); // -> overloaded(Object)
        String stringResult = overloaded(asString); // -> overloaded(String)
        String integerResult = overloaded(asInteger); // -> overloaded(Integer)

        // --- Cas 2 : pas de surcharge exacte -> élargissement/boxing a la compilation ---
        // char n'a pas de surcharge dédiée : le compilateur choisit la meilleure
        // conversion possible -> boxing en Character, puis Object.
        char asChar = 'A';
        String charResult = overloaded(asChar); // -> overloaded(Object)

        return new JsonBuilder()
                .put("status", "success")
                .put("concept", "Binding statique")
                .put("description",
                        "La surcharge est choisie a la compilation selon le type DECLARE "
                        + "de l'argument, meme si le type reel de l'objet est different.")
                .put("overloaded_on_object", objectResult) // overloaded(Object)
                .put("overloaded_on_string", stringResult) // overloaded(String)
                .put("overloaded_on_integer", integerResult) // overloaded(Integer)
                .put("overloaded_on_char", charResult) // overloaded(Object)
                .put("static_method", staticMethodInfo())
                .toString();
    }
}
