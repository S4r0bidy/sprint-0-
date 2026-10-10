# REST API Framework

Un framework API REST minimaliste en Java avec support des annotations pour le routage automatique.

## Architecture

### Composants clés

- **FrontController** (`framework.FrontController`) : Servlet principale qui intercepte toutes les requêtes
- **ControllerScannerListener** : Démarre au lancement et scanne les contrôleurs marqués avec `@ApiController`
- **ApiController** : Annotation pour marquer une classe comme contrôleur API
- **GetMapping / PostMapping** : Annotations pour mapper les routes HTTP
- **JsonBuilder** : Utilitaire pour construire les réponses JSON
- **StaticBindingDemo** : Illustration du concept de binding statique
- **DynamicBindingDemo** : Illustration du concept de binding dynamique

### Structure

```
src/
├── controller/
│   └── UserApiController.java    # Contrôleurs API
└── framework/
    ├── FrontController.java      # Servlet principale
    ├── ControllerScannerListener.java  # Scanner de routes
    ├── Mapping.java              # Classe pour stocker les mappings
    ├── JsonBuilder.java          # Constructeur JSON
    ├── StaticBindingDemo.java    # Démo binding statique
    ├── DynamicBindingDemo.java   # Démo binding dynamique
    └── annotation/
        ├── ApiController.java    # Annotation contrôleur
        ├── GetMapping.java       # Annotation GET
        └── PostMapping.java      # Annotation POST
```

## Utilisation

### Créer un contrôleur API

```java
import framework.annotation.ApiController;
import framework.annotation.GetMapping;
import framework.annotation.PostMapping;
import framework.JsonBuilder;
import java.util.Arrays;

@ApiController
public class UserApiController {

    @GetMapping("/api/users")
    public String listUsers() {
        return new JsonBuilder()
            .put("status", "success")
            .put("data", Arrays.asList("Alice", "Bob", "Charlie"))
            .toString();
    }

    @PostMapping("/api/users")
    public String createUser() {
        return new JsonBuilder()
            .put("status", "success")
            .put("id", 4)
            .put("message", "User created")
            .toString();
    }
}
```

## Routes disponibles

- `GET /api/users` - Récupère la liste des utilisateurs
- `GET /api/users/count` - Récupère le nombre d'utilisateurs
- `POST /api/users` - Crée un nouvel utilisateur
- `GET /api/static-binding` - Illustre le concept de binding statique
- `GET /api/dynamic-binding` - Illustre le concept de binding dynamique

## Concept : Binding statique

Le **binding statique** (ou *early binding*) désigne le fait que le compilateur
Java détermine **dès la compilation** quelle méthode (ou quel attribut) sera
utilisé. Il s'applique à :

- les méthodes **`static`** (liées à la classe, jamais redéfinies),
- les méthodes **`private`** (aucune override possible),
- les méthodes et classes **`final`** (non redéfinissables),
- la **surcharge** (overloading), décidée selon le **type déclaré**.

Exemple (surcharge résolue à la compilation) :

```java
public static String overloaded(Object arg)  { return "overloaded(Object)"; }
public static String overloaded(String arg)  { return "overloaded(String)"; }

Object a = "texte";   // type déclaré : Object
String b = "texte";   // type déclaré : String

overloaded(a);  // -> overloaded(Object)
overloaded(b);  // -> overloaded(String)
```

## Concept : Binding dynamique

Le **binding dynamique** (ou *late binding* / *dynamic dispatch*) désigne le fait
que la méthode réellement exécutée est choisie **à l'exécution**, en fonction du
**type réel** (la classe effective) de l'objet, et non du type déclaré de la
référence. Il s'applique aux méthodes **redéfinies** (`override`) — le contraire
du binding statique, qui concerne les méthodes `static`, `private`, `final` et la
surcharge.

Exemple (redéfinition résolue à l'exécution) :

```java
class Animal   { String makeSound() { return "un son generique"; } }
class Dog extends Animal { @Override String makeSound() { return "Woof"; } }
class Cat extends Animal { @Override String makeSound() { return "Miaou"; } }

Animal a = new Dog();  // type déclaré : Animal, type réel : Dog
a.makeSound();         // -> "Woof"   (choisi à l'exécution)
```

> Le binding statique (méthodes `static`, `private`, `final` et surcharge
> décidée à la compilation) est traité dans la section précédente.

## Réponses

Toutes les réponses sont au format JSON :

```json
{
  "status": "success",
  "data": ["Alice", "Bob", "Charlie"],
  "message": "Users retrieved successfully"
}
```

## Fonctionnalités

✅ Routage automatique via annotations
✅ Support GET et POST
✅ Génération JSON automatique
✅ Gestion des erreurs en JSON
✅ Logging des requêtes
✅ Démos des concepts de binding statique et dynamique
