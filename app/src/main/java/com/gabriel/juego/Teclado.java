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
    public Teclado(){
        botones=new ArrayList<>();


        this.imagen=getAsset("Fuente/fuente.png");
int i=0;
        String cad="qwertyuiop";
        int posx=50;
        int posy=(altoPantalla / 6) * 3;
        for (i=0;i<cad.length();i++){
            botones.add(new Boton(cad.charAt(i)+"",Fuente.letras.get(cad.charAt(i)),posx, posy) );
            posx=posx+Fuente.letras.get(cad.charAt(i)).getWidth();
        }

        cad="asdfghjkl";
         posx=50;
         posy=(altoPantalla / 6) * 4;
        for (i=0;i<cad.length();i++){
            botones.add(new Boton(cad.charAt(i)+"",Fuente.letras.get(cad.charAt(i)),posx, posy) );
            posx=posx+Fuente.letras.get(cad.charAt(i)).getWidth();
        }

        cad="zxcvbnm";
        posx=50;
        posy=(altoPantalla / 6) * 5;
        for (i=0;i<cad.length();i++){
            botones.add(new Boton(cad.charAt(i)+"",Fuente.letras.get(cad.charAt(i)),posx, posy) );
            posx=posx+Fuente.letras.get(cad.charAt(i)).getWidth();
        }

//        botones.add("qwertyuiop", (altoPantalla / 6) * 3);
//        botones.add("asdfghjkl", (altoPantalla / 6) * 4);
//        botones.add("zxcvbnm", (altoPantalla / 6) * 5);
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



    public Bitmap getAsset(String fichero) {
        try {
            InputStream is = context.getAssets().open(fichero);
            return BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            return null;
        }
    }
}
