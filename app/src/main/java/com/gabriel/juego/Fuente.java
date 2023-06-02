package com.gabriel.juego;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.HashMap;
import java.util.Locale;

/**
 * Gestion de la fuente
 */
public class Fuente {
    Bitmap fuente;
    static HashMap<String, Bitmap> letras = new HashMap<>();

    /**
     * Añade nuevo elemento al hashmap
     * @param letra caracter
     * @param pos posicion en el corte del bitmap
     */
    public void addLetra(String letra, int pos) {
        // pos=pos-1;
        letras.put(letra, Bitmap.createBitmap(fuente, (fuente.getWidth() / 77) * pos, 0, fuente.getWidth() / 77, fuente.getHeight()));
        letras.put(letra, Bitmap.createScaledBitmap(letras.get(letra), letras.get(letra).getWidth() * 7, letras.get(letra).getHeight() * 7, true));

    }

    /**
     * Devuelve el bitmap asociado a un caracter dado
     * @param c caracter del que obtener el bitmap
     * @return imagen asociada al caracter
     */
    public Bitmap getTexto(char c) {
        return letras.get((c + "").toUpperCase());
    }

    /**
     * añade al Hashmap de caracteres disponibles
     * @param fuente bitmap con la fuente completa del que obtener los bitmap de caracteres individuales
     */
    public Fuente(Bitmap fuente) {
        this.fuente = fuente;

        addLetra("A", 33);
        addLetra("B", 34);
        addLetra("C", 35);
        addLetra("D", 36);
        addLetra("E", 37);
        addLetra("F", 38);
        addLetra("G", 39);
        addLetra("H", 40);
        addLetra("I", 41);
        addLetra("J", 42);
        addLetra("K", 43);
        addLetra("L", 44);
        addLetra("M", 45);
        addLetra("N", 46);
        addLetra("O", 47);
        addLetra("P", 48);
        addLetra("Q", 49);
        addLetra("R", 50);
        addLetra("S", 51);
        addLetra("T", 52);
        addLetra("U", 53);
        addLetra("V", 54);
        addLetra("W", 55);
        addLetra("X", 56);
        addLetra("Y", 57);
        addLetra("Z", 58);
        addLetra("0", 16);
        addLetra("1", 17);
        addLetra("2", 18);
        addLetra("3", 19);
        addLetra("4", 20);
        addLetra("5", 21);
        addLetra("6", 22);
        addLetra("7", 23);
        addLetra("8", 24);
        addLetra("9", 25);
        addLetra(":", 26);
        addLetra(" ", 0);
        addLetra("<", 6);
        addLetra("*", 10);
        addLetra(".", 14);
    }

    /**
     * Traduce texto a la serie de bitmaps asociados a los caracteres
     * @param c Canvas sobre el que dibujar el texto
     * @param txt cadena a transformar
     * @param posX eje x de inicio de dibujo
     * @param posY eje y de inicio de dibujo
     */
    public void dibujar(Canvas c, String txt, int posX, int posY) {
        for (int i = 0; i < txt.length(); i++) {
            c.drawBitmap(getTexto(txt.charAt(i)), posX, posY, null);
            posX += getTexto(txt.charAt(i)).getWidth();
        }
    }
}
