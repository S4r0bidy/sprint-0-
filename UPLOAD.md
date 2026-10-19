# Upload de fichier

Les actions `POST` peuvent recevoir un fichier `multipart/form-data` via
`UploadedFile`. Le champ s'appelle `file` par défaut et sa taille est limitée
à 10 Mo.

```java
@PostMapping("/api/upload")
public String upload(@RequestFile("file") UploadedFile file) {
    // file.getBytes() ou file.saveTo(new File("chemin/vers/fichier"))
    return new JsonBuilder()
        .put("filename", file.getOriginalFilename())
        .put("size", file.getSize())
        .toString();
}
```

La route de démonstration est `POST /api/upload`. Le framework injecte aussi
automatiquement `HttpServletRequest` et `HttpServletResponse` lorsqu'ils sont
déclarés comme paramètres d'une action.
