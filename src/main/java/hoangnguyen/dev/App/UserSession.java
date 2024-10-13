package hoangnguyen.dev.App;

import java.util.prefs.Preferences;

public class UserSession {
    private static final String PREF_EMAIL = "user_email";
    private static final String PREF_PASSWORD = "user_password";
    private static final String PREF_REMEMBER = "remember_login";

    private static Preferences prefs = Preferences.userNodeForPackage(UserSession.class);

    public static void saveSession(String email, String password, boolean remember) {
        if (remember) {
            prefs.put(PREF_EMAIL, email);
            prefs.put(PREF_PASSWORD, password);
            prefs.putBoolean(PREF_REMEMBER, true);
        } else {
            clearSession();
        }
    }

    public static String getEmail() {
        return prefs.get(PREF_EMAIL, "");
    }

    public static String getPassword() {
        return prefs.get(PREF_PASSWORD, "");
    }

    public static boolean isRememberLogin() {
        return prefs.getBoolean(PREF_REMEMBER, false);
    }

    public static void clearSession() {
        prefs.remove(PREF_EMAIL);
        prefs.remove(PREF_PASSWORD);
        prefs.remove(PREF_REMEMBER);
    }
}