1. Binding statique (Static / Early Binding)

• Définition : Le compilateur détermine quelle méthode appeler au moment de la compilation, en se basant sur le type de la référence (et non sur l'objet réel).

• Cas concernés : Les méthodes surchargées (overloaded), ainsi que les méthodes déclarées static, private ou final (car elles ne peuvent pas être redéfinies).

2. Binding dynamique (Dynamic / Late Binding)

• Définition : La méthode exécutée est déterminée à l'exécution, en se basant sur le type réel (la classe effective) de l'objet, et non sur le type déclaré de la référence.

• Cas concernés : Les méthodes redéfinies (override / polymorphisme d'héritage). La JVM choisit l'implémentation via la table virtuelle (vtable).

• Démo : GET /api/dynamic-binding (DynamicBindingDemo).

