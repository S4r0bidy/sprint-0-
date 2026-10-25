
package framework;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public final class SessionData {

    private static final String SESSION_KEY =
            SessionData.class.getName() + ".DATA";

    private final Map<String, Object> data;

    private SessionData(Map<String, Object> data) {
        this.data = data;
    }

    public static SessionData get(HttpServletRequest request) {
        HttpSession session = request.getSession();

        synchronized (session) {
            @SuppressWarnings("unchecked")
            Map<String, Object> stored =
                    (Map<String, Object>) session.getAttribute(SESSION_KEY);

            if (stored == null) {
                stored = new HashMap<String, Object>();
                session.setAttribute(SESSION_KEY, stored);
            }

            return new SessionData(stored);
        }
    }

    public void set(String key, Object value) {
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "La clé de session est obligatoire");
        }

        if (value == null) {
            data.remove(key);
        } else {
            data.put(key, value);
        }
    }

    public Object get(String key) {
        return data.get(key);
    }

    public String getString(String key) {
        Object value = data.get(key);
        return value instanceof String ? (String) value : null;
    }

    public boolean contains(String key) {
        return data.containsKey(key);
    }

    public void remove(String key) {
        data.remove(key);
    }

    public void clear() {
        data.clear();
    }
}
