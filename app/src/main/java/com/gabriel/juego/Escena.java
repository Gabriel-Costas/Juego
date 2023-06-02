package com.gabriel.juego;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import java.io.IOException;
import java.io.InputStream;

public class Escena {

    int numEscena=-1;
    int altoPantalla;
    int anchoPantalla;
    Context context;
    Boton btnHome;
    Paint p;
    Rect menu;
    Fuente fuente;

    /**
     * Constructor de la clase Escena
     * @param context contecxto de la aplicación
     * @param anchoPantalla ancho de la pantalla en pixeles
     * @param altoPantalla alto de la pantalla en pixeles
     * @param numEscena identificador de la escena
     */
    public Escena( Context context, int anchoPantalla, int altoPantalla, int numEscena) {
        this.altoPantalla = altoPantalla;
        this.anchoPantalla = anchoPantalla;
        this.context = context;
        this.fuente=fuente;
        btnHome = new Boton("Home", getAsset("Botones/home.png"), (anchoPantalla / 10) * 8, (altoPantalla / 10) * 8);
        p=new Paint();
        p.setTextSize(altoPantalla/10);
        p.setColor(Color.WHITE);
        p.setTextAlign(Paint.Align.CENTER);
        this.numEscena=numEscena;
        this.menu = new Rect(100, 100, 500, 400);
        fuente = new Fuente(getAsset("Fuente/fuente.png"));
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

    /**
     * Código que se ejecuta al crear una escena
     */
    public void onEscenaCreated () {
    }

    /**
     * Código que se ejecuta al destruir una escena
     */
    public void onEscenaDestroyed () {
    }

    /**
     * Dibuja en pantalla los elementos necesarios indicados
     * @param c Canvas sobre el que dibujar
     */
    public void dibujar(Canvas c){

//        if (numEscena>1) c.drawRect(botonIz,p);
//        if (numEscena<6)c.drawRect(botonDerecha,p);
//        if (numEscena!=1) c.drawRect(menu,p);
    }

    /**
     * Actualiza los elementos ejecutados
     */
    public void actualizarFisica(){

    }

    /**
     * Gestiona eventos de toque en pantalla
     * @param event tipo de evento a gestionar
     * @return Devuelve el número de escena
     */
    int onTouchEvent(MotionEvent event){
        int x=(int)event.getX();
        int y=(int)event.getY();
        if (numEscena!=1)   if (menu.contains(x,y)) return 1;

        if (numEscena == 5 || numEscena==4 || numEscena == 3) {
            if (btnHome.hitbox.contains(x, y)) {
                return 1;
            }
        }


        return -1;
    }

}
