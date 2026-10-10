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

> Le **binding dynamique** (méthodes `override` choisies selon le type réel à
> l'exécution) n'est pas traité dans cette version.

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
