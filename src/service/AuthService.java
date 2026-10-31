package service;

import model.User;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Service d'authentification en mémoire.
 *
 * <p>Une seule instance est partagée par l'application (singleton), conformément
 * au cycle de vie unique du {@code FrontController}. Les comptes sont statiques :
 * ce service sert de base de démonstration et pourra être remplacé par un
 * repository (base de données) sans changer le contrôleur.</p>
 */
public final class AuthService {

    private static final AuthService INSTANCE = new AuthService();

    private final Map<String, Account> accounts = new HashMap<String, Account>();

    private AuthService() {
        register(1, "admin", "admin123", "ADMIN");
        register(2, "sarobidy", "sarobidy123", "USER");
    }

    public static AuthService getInstance() {
        return INSTANCE;
    }

    /**
     * Vérifie les identifiants fournis.
     *
     * @return l'utilisateur correspondant, ou {@code null} si les identifiants
     *         sont invalides.
     */
    public User authenticate(String username, String password) {
        if (username == null || username.trim().isEmpty()
                || password == null || password.isEmpty()) {
            return null;
        }

        Account account = accounts.get(
                username.trim().toLowerCase(Locale.ROOT));

        if (account == null) {
            return null;
        }

        byte[] expected = account.passwordHash.getBytes(StandardCharsets.UTF_8);
        byte[] actual = hash(password).getBytes(StandardCharsets.UTF_8);

        // Comparaison à temps constant
        return MessageDigest.isEqual(expected, actual)
                ? account.user
                : null;
    }

    private void register(int id, String username, String password, String role) {
        String key = username.toLowerCase(Locale.ROOT);
        accounts.put(key, new Account(new User(id, username, role), hash(password)));
    }

    private static String hash(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(
                    password.getBytes(StandardCharsets.UTF_8));

            StringBuilder hex = new StringBuilder(bytes.length * 2);
            for (byte b : bytes) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(
                    "Algorithme SHA-256 indisponible", e);
        }
    }

    private static final class Account {
        private final User user;
        private final String passwordHash;

        private Account(User user, String passwordHash) {
            this.user = user;
            this.passwordHash = passwordHash;
        }
    }
}
