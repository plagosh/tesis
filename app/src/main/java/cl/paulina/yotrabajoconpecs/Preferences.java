package cl.paulina.yotrabajoconpecs;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
    public static final String STRING_PREFERENCES = "paulina.yotrabajoconpecs";
    public static final String PREFERENCES_ESTADO_BUTTON_SESION = "estado.button.sesion";
    public static final String PREFERENCE_USUARIO_LOGIN = "usuario.login";

    public static void savePreferenceBoolean(Context c, boolean b, String key){
        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFERENCES, c.MODE_PRIVATE);
        preferences.edit().putBoolean(key, b).apply();
    }

    public static void savePreferenceString(Context c, String b, String key){
        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFERENCES, c.MODE_PRIVATE);
        preferences.edit().putString(key, b).apply();
    }

    public static Boolean obtenerPreferenceBoolean(Context c, String key){
        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFERENCES, c.MODE_PRIVATE);
        return preferences.getBoolean(key, false); //si es que nunca se ha guardado nada en esta key retornará false
    }

    public static String obtenerPreferenceString(Context c, String key){
        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFERENCES, c.MODE_PRIVATE);
        return preferences.getString(key, ""); //si es que nunca se ha guardado nada en esta key retornará cadena vacía
    }
}
