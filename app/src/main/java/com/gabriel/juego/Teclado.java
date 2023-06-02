package com.gabriel.juego;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Teclado extends Boton{

    int coorX=0;
    ArrayList<Boton> botones;
    Bitmap imagen;
    Context context;
    int altoPantalla;

    /**
     * Constructor de teclado

     */
    public Teclado(Context context, int altoPantalla){
        botones=new ArrayList<>();
        this.context=context;
        this.altoPantalla = altoPantalla;

        imagen=getAsset("Fuente/fuente.png");

        add("qwertyuiop", (altoPantalla / 6) * 3);
        add("asdfghjkl", (altoPantalla / 6) * 4);
        add("zxcvbnm", (altoPantalla / 6) * 5);

    }

    /**
     * Añade un string a una lista para separacion de caracteres
     * @param lista lista de caracteres a transformar
     * @param coorY ejeY donde empezar a dibujar
     */
    public void add(String lista, int coorY){
        char[] letras=lista.toCharArray();
        int coorX=JuegoView.getScreenWidth()/5;
        Fuente fuente=new Fuente(imagen);
        for(char ch:letras){
            coorX+=150;
            botones.add(new Boton(ch+"",fuente.getTexto(ch),coorX,coorY));
        }
    }

    /**
     * Dibuja los elementos
     * @param c Canvas sobre el que dibujar el texto
     */
    public void dibujar(Canvas c){
        for(Boton b:botones){
            b.dibujar(c);
        }
    }


    /**
     * Carga elementos desde la biblioteca de assets
     * @param fichero elemento a cargar
     * @return
     */
    public Bitmap getAsset(String fichero) {
        try {
            InputStream is = context.getAssets().open(fichero);
            return BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            return null;
        }
    }
}
