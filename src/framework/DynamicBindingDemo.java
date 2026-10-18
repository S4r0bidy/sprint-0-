package framework;

public class DynamicBindingDemo {

    /** Classe de base dont la methode est destinee a etre redefinie (override). */
    public static class Animal {
        public String makeSound() {
            return "un son generique";
        }

        public String describe() {
            return "Animal";
        }
    }

    public static class Dog extends Animal {
        @Override
        public String makeSound() {
            return "Woof";
        }

        @Override
        public String describe() {
            return "Chien";
        }
    }

    public static class Cat extends Animal {
        @Override
        public String makeSound() {
            return "Miaou";
        }

        @Override
        public String describe() {
            return "Chat";
        }
    }

    public static String buildResponse() {

        Animal animal = new Animal();
        Animal dog = new Dog();
        Animal cat = new Cat();

        String animalResult = animal.makeSound();
        String dogResult = dog.makeSound();
        String catResult = cat.makeSound();

        return new JsonBuilder()
                .put("status", "success")
                .put("concept", "Binding dynamique")
                .put("description",
                        "La methode executee est choisie a l'execution selon le type reel de l'objet.")
                .put("animal", animalResult)
                .put("dog", dogResult)
                .put("cat", catResult)
                .toString();
    }
}
