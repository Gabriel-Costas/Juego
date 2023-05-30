package com.gabriel.juego;

import android.content.Context;
import android.content.SharedPreferences;

public class Records {

    /**
     * Guarda un valor int para permanencia de datos
     * @param clave clave del valor para lectura
     * @param valor valor a guardar
     * @param context contexto de la aplicacion
     * @return el guardado de los datos fue exitoso
     */
    public static Boolean guardaint(String clave, int valor, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(clave, valor);

        return editor.commit();
    }

    /**
     * Guarda un string para permanencia de datos
     * @param clave clave del valor para lectura
     * @param valor valor a guardar
     * @param context contexto de la aplicacion
     * @return el guardado de los datos fue exitoso
     */
    public static Boolean guardastring(String clave, String valor, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(clave, valor);
        return editor.commit();
    }

    /**
     * Guarda el estado de un boolean
     * @param clave clave del valor para lectura
     * @param valor valor a guardar
     * @param context contexto de la aplicacion
     * @return el guardado de los datos fue exitoso
     */
    public static Boolean guardaboolean(String clave, boolean valor, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(clave, valor);
        return editor.commit();
    }

    /**
     * Recupera el valor de una variable integer guardada
     * @param clave codigo de la variable guardada que quieres leer
     * @param context contexto de la aplicacion
     * @return valor de la variable guardada
     */
    public static int leeint(String clave, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("datos", Context.MODE_PRIVATE);
        return sharedPreferences.getInt(clave, 0);
    }

    /**
     * Recupera el valor de una variable string guardada
     * @param clave codigo de la variable guardada que quieres leer
     * @param context contexto de la aplicacion
     * @return valor de la variable guardada
     */
    public static String leestring(String clave, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("datos", Context.MODE_PRIVATE);
        return sharedPreferences.getString(clave, "");
    }

    /**
     * Recupera el valor de una variable boolean guardada
     * @param clave codigo de la variable guardada que quieres leer
     * @param context contexto de la aplicacion
     * @return valor de la variable guardada
     */
    public static boolean leeboolean(String clave, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("datos", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(clave, false);
    }
}
