## Création de l'arborescence du projet
    src/ 
        controller /
        framework /
            FrontController.java           
        views / 

## Note:
- chaque url peut etre associé à une servlet différente
- le FrontController:
    - recoit tous les appels
    - regarde l'URL demandé
    - décide quel controleur doit etre executé
    - renvoie la réponse

## sprint-0-
- tous URL doit passer dans le FrontController

## sprint-1-

## sprint-2-
- affichage des methodes utilisé d'une URL donnée 

## sprint-3-
- création annotation @Url qui supporte une deuxieme argument: @Url("test", GET )

## sprint-4-
- integration vues