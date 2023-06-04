package com.gabriel.juego;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;

/**
 * Gestion de los items
 */
public class Items {

    /**
     * Imagen del item
     */
    Bitmap imagen;
    /**
     * Hitbox del item
     */
    Rect hitbox;

    /**
     * posicion horizontal del item en pantalla
     */
    int Px;
    /**
     * Puntos que añade el item al coleccionarlo
     */
    int pts;
    /**
     * Alto de la pantalla
     */
    int altopantalla;

    /**
     * Posicion vertical del item en pantalla
     */
    int Py = (altopantalla / 10) * 8;
    /**
     * Velocidad del item al moverse la pantalla
     */
    int velo = 40, veloDibujo = 0;
    /**
     * Color de la hitbox
     */
    Paint p;

    /**
     * Constructor de objetos y power ups
     * @param Px posicion de inicio de dibujo
     * @param imagen imagen del objeto
     */
    public Items(int Px, Bitmap imagen, int altopantalla) {
        this.Px = Px;
        this.imagen = imagen;
        this.altopantalla=altopantalla;
        p = new Paint();
        p.setColor(Color.TRANSPARENT);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(10);
        actualizaHitBox();
        pts=10;
    }

    /**
     * Gestiona la posicion de la hitbox del objeto
     */
    public void actualizaHitBox() {
        hitbox = new Rect(Px, Py, Px + imagen.getWidth(), Py + imagen.getHeight());
    }

    /**
     * Dibuja el objeto
     * @param c Canvas sobre el que dibujar
     */
    public void dibujar(Canvas c) {
        c.drawBitmap(imagen, Px, Py, null);
        c.drawRect(hitbox, p);
    }

    /**
     * Asigna velocidad cuando el objeto comienza a moverse
     */
    public void arranco() {
        Log.i("arranco", "!asdf " + veloDibujo + " " + velo);
        veloDibujo = velo;

    }

    /**
     * Gestiona velocidad del objeto cuando se mueve hacia la izquierda
     */
    public void mueveIz() {
        if (velo > 0) {
            this.velo = Math.abs(velo) * -1;
            this.veloDibujo = Math.abs(veloDibujo) * -1;
        }
    }

    /**
     * Gestiona velocidad del objeto cuando se mueve hacia la derecha
     */
    public void mueveDcha() {
        if (velo < 0) {
            this.velo = Math.abs(velo);
            this.veloDibujo = Math.abs(veloDibujo);

        }
    }

    /**
     * Detiene el movimiento del objeto
     */
    public void paro() {
        veloDibujo = 0;
    }

    /**
     * Gestiona que el objeto se mueva segun la velocidad asignada previamente para la dirección
     */
    public void mover() {
        Px+=veloDibujo;
        actualizaHitBox();
    }
}
