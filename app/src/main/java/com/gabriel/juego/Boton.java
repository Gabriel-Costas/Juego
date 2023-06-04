package com.gabriel.juego;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Gestion de los elementos tipo boton
 */
public class Boton {
    /**
     * Nombre del boton
     */
    String nombre;
    /**
     * Imagen del boton
     */
    Bitmap imgBoton;
    /**
     * Ancho del boton
     */
    int Sx;
    /**
     * Alto del boton
     */
    int Sy;
    /**
     * Rectangulo para Gestionar pulsaciones
     */
    Rect hitbox;
    /**
     * Dibuja la hitbox
     */
    Paint p;
    /**
     * Activa o desactiva el boton
     */
    boolean setEnabled =true;

    /**
     * Constructor vacío de elementos botón
     */
    public Boton(){
        this.nombre="";
        this.imgBoton=null;
        this.Sx=0;
        this.Sy=0;
    }

    /**
     * Constructor con parámetros de elementos botón
     * @param nombre Nombre del botón
     * @param img Bitmap asociado al botón
     * @param Sx Tamaño en el eje X
     * @param Sy Tamaño en el eje Y
     */
    public Boton(String nombre, Bitmap img,int Sx, int Sy){
        this.nombre=nombre;
        this.imgBoton=img;
        this.Sx=Sx;
        this.Sy=Sy;
        actualizaHitBox();
        p = new Paint();
        p.setColor(Color.TRANSPARENT);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(10);
    }

    /**
     * Dibujar el elemento en pantalla
     * @param c Canvas sobre el que dibuja
     */
    public void dibujar(Canvas c) {

        c.drawRect(hitbox, p);
        if(setEnabled){
            c.drawBitmap(imgBoton, Sx, Sy, null);
        }

    }


    /**
     * Gestiona el tamaño y posición de la hitbox del botón
     */
    public void actualizaHitBox() {
        hitbox = new Rect(Sx , Sy, Sx + imgBoton.getWidth(), Sy + imgBoton.getHeight());
    }
}
