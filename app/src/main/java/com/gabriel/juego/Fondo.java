package com.gabriel.juego;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.util.Log;

/**
 * Gestiona elementos tipo fondo
 */
public class Fondo {

    /**
     * posiccion de inicio y final de una imagen en su posicion x (ancho)
     */
    public int x, x2;
    /**
     * Imagen para el fondo
     */
    public Bitmap imagen;
    /**
     * Velocidades para el control de movimiento del fondo
     */
    public int velo, veloDibujo = 0;
    /**
     * Ancho de la pantalla
     */
    public int anchoPantalla;

    /**
     * Constructor de elemento fondo
     * @param imagen imagen de fondo
     * @param x origen x de la imagen
     * @param velo velocidad de desplazamiento
     * @param anchoPantalla ancho de la pantalla
     */
    public Fondo(Bitmap imagen, int x, int velo, int anchoPantalla) {
        this.imagen = imagen;
        this.x = x;
        this.x2 = x + imagen.getWidth();
        this.velo = velo;
        this.veloDibujo = 0;
        this.anchoPantalla = anchoPantalla;
    }

    /**
     * constructor de imagen fondo simplificado
     * @param imagen imagen de fondo
     */
    public Fondo(Bitmap imagen) {
        this.imagen = imagen;
    }

    /**
     * Gestiona el desplazamiento del fondo hacia la izquierda
     */
    public void mueveIz() {
        if (velo > 0) {
            this.velo = Math.abs(velo) * -1;
            this.veloDibujo = Math.abs(veloDibujo) * -1;
        }
    }

    /**
     * Gestiona el desplazamiento del fondo hacia la derecha
     */
    public void mueveDcha() {
        if (velo < 0) {
            this.velo = Math.abs(velo);
            this.veloDibujo = Math.abs(veloDibujo);
        }
    }

    /**
     * Para el movimiento del fondo
     */
    public void paro() {
        veloDibujo = 0;
    }

    /**
     * Asigna velocidad de movimiento al fondo cuando comienza a moverse
     */
    public void arranco() {
        Log.i("arranco", "!asdf " + veloDibujo + " " + velo);
        veloDibujo = velo;
    }

    /**
     * Dibuja el fondo
     * @param c Canvas sobre el que se va a dibujar
     */
    public void dibujar(Canvas c) {
        c.drawBitmap(imagen, x, 0, null);
        c.drawBitmap(imagen, x2, 0, null);
    }

    /**
     * Gestiona el movimiento y cambios de sentido del fondo
     */
    public void mover() {
        Log.i("arranco", "!pulso22 " + veloDibujo);
        x += veloDibujo;
        x2 += veloDibujo;
        if (veloDibujo > 0) {
            Log.i("cafe2", "pasa dibujo fonto");
            Log.i("cafe2", "" + anchoPantalla);
            Log.i("cafe2", "" + imagen.getWidth());
            Log.i("cafe2", "" + imagen.getHeight());
            Log.i("cafe2", "" + x);
            Log.i("cafe2", "" + x2);
            if (x >= anchoPantalla) {
                x = x2 - imagen.getWidth();
            }
            if (x2 >= anchoPantalla) {
                x2 = x - imagen.getWidth();
            }
        } else if (veloDibujo < 0) {
            if (x + imagen.getWidth() < 0) {
                x = x2 + imagen.getWidth();
            }
            if (x2 + imagen.getWidth() < 0) {
                x2 = x + imagen.getWidth();
            }
        }

    }
}
