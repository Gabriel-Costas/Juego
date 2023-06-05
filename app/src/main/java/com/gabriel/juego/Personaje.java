package com.gabriel.juego;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

public class Personaje {

    /**
     * Posicion en X e Y del personaje
     */
    public int Px, Py;
    /**
     * Comprueba si puede hacer spawn de un enemigo tipo boss
     */
    public int bossCheck;

    /**
     * Puntuacion del personaje
     */
    public static int pts = 0;
    /**
     * Vida total y actual del jugador
     */
    public int vidas, vidaActual = 5;
    /**
     * Cantidad inicial y maxima de pociones del jugador
     */
    public int pociones=1, maxPociones=3;
    /**
     * Hitboxes del perosnaje
     */
    public Rect hitbox, hurtbox, hbCentro;
    /**
     * Colores de las hitboxes
     */
    public Paint p, p2, p3;
    /**
     * Posicion de la imagen actual del vector de imagenes de movimiento
     */
    public int numFrame = 0;
    /**
     * Comprueba si el personaje cambia de sentido
     */
    public boolean cambia = false;
    /**
     * Comprueba hacia que lado mira el perosnaje
     */
    public boolean derecha;
    /**
     * Velocidad de cambio de frame de movimiento
     */
    public int tickFrame = 200;
    /**
     * Inicia la cuenta para el cambio de frame
     */
    public long tframe = 0;
    /**
     * Tiempo de inmunidad al recibir daño
     */
    public int tickvida=1500;
    /**
     * Inicia la cuenta de inmunidad
     */
    public long tvida=0;

    /**
     * Comprueba si el personaje esta quieto
     */
    public boolean parado = true;
    /**
     * Ancho y alto de la pantalla
     */
    public int anchoP, altoP;
    /**
     * Comprueba si el personaje esta atacando
     */
    public boolean ataca = false;
    /**
     * Comprueba si el personaje esta vivo actualmente
     */
    public boolean vivo = true;
    /**
     * vectores de imagenes de movimiento del personaje
     */
    public Bitmap[] mcRunCyclei, mcRunCycled, mcAtk1d, mcAtk2d, mcAtk3d, mcAtk1i, mcAtk2i, mcAtk3i, frames, parod, paroi;
    /**
     * Contexto de la aplicacion
     * Contexto de la aplicacion
     */
    public  Context context;


    /**
     * Invierte imagenes horizontalmente
     * @param imagenes array de imagenes
     * @return imagenes invertidas
     */
    public Bitmap[] generaEspejos(Bitmap[] imagenes) {
        Bitmap[] aux = new Bitmap[imagenes.length];
        for (int i = 0; i < aux.length; i++) {
            aux[i] = espejo(imagenes[i], true);
        }
        return aux;
    }

    /**
     * Creacion del personaje jugador
     *
     * @param anchoP ancho de pantalla
     * @param Px     tamaño en ejex
     * @param Py     tamaño en ejeY
     */
    public Personaje(Context context, int anchoP, int altoP, int Px, int Py) {
        this.context = context;
        this.Px = Px;
        this.Py = Py;
        this.anchoP = anchoP;
        this.altoP = altoP;

        p = new Paint();
        p.setColor(Color.TRANSPARENT);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(10);
        p3 = new Paint();
        p3.setColor(Color.TRANSPARENT);
        p3.setStyle(Paint.Style.STROKE);
        p3.setStrokeWidth(10);
        p2 = new Paint();
        p2.setColor(Color.TRANSPARENT);
        p2.setStyle(Paint.Style.STROKE);
        p2.setStrokeWidth(10);
        vidas = 5;
        derecha = true;

        Bitmap[] aux = new Bitmap[13];
        Bitmap baux;
        for (int i = 1; i <= 13; i++) {
            baux = getAsset("personaje/a" + i + ".png");

            baux = escalaAltura(baux, altoP / 2);
            aux[i - 1] = baux;
        }

        mcAtk1d = new Bitmap[5];
        mcAtk1d[0] = aux[0];
        mcAtk1d[1] = aux[1];
        mcAtk1d[2] = aux[2];
        mcAtk1d[3] = aux[3];
        mcAtk1d[4] = aux[4];
        mcAtk1i = generaEspejos(mcAtk1d);

        mcAtk2d = new Bitmap[5];
        mcAtk2d[0] = aux[5];
        mcAtk2d[1] = aux[4];
        mcAtk2d[2] = aux[6];
        mcAtk2d[3] = aux[7];
        mcAtk2d[4] = aux[8];
        mcAtk2i = generaEspejos(mcAtk2d);

        mcAtk3d = new Bitmap[6];
        mcAtk3d[0] = aux[9];
        mcAtk3d[1] = aux[5];
        mcAtk3d[2] = aux[4];
        mcAtk3d[3] = aux[10];
        mcAtk3d[4] = aux[11];
        mcAtk3d[5] = aux[12];
        mcAtk3i = generaEspejos(mcAtk3d);


        parod = new Bitmap[1];
        Bitmap auux = BitmapFactory.decodeResource(context.getResources(), R.drawable.personaje_idle1);
        parod[0] = escalaAltura(auux, altoP / 2);

        paroi = generaEspejos(parod);

        mcRunCycled = new Bitmap[8];
        for (int i = 0; i < 8; i++) {
            baux = getAsset("personaje/personaje_run" + (i + 1) + ".png");

            baux = escalaAltura(baux, altoP / 2);
            mcRunCycled[i] = baux;
        }
        mcRunCyclei = generaEspejos(mcRunCycled);


        frames = parod;

        actualizaHitBox();

    }

    /**
     * Escala la altura de la imagen
     * @param bitmapAux copia de la imagen original
     * @param nuevoAlto altura nueva
     * @return imagen escalada
     */
    public Bitmap escalaAltura(Bitmap bitmapAux, int nuevoAlto) {

        if (nuevoAlto == bitmapAux.getHeight()) return bitmapAux;
        return bitmapAux.createScaledBitmap(bitmapAux, (bitmapAux.getWidth() * nuevoAlto) /
                bitmapAux.getHeight(), nuevoAlto, true);
    }

    /**
     * Gestiona la posición y tamaño de las hitbox del personaje
     */
    public void actualizaHitBox() {
        hitbox = new Rect(Px + frames[numFrame].getWidth() / 3, Py + frames[numFrame].getHeight() / 6, Px + (frames[numFrame].getWidth() / 3) * 2, Py + frames[numFrame].getHeight());
        int auxh = (hitbox.right - hitbox.left) / 20;

        hbCentro = new Rect(hitbox.centerX() - auxh * 2, hitbox.top, hitbox.centerX() + auxh * 2, hitbox.bottom);
        if (derecha) {
            hurtbox = new Rect(hitbox.right, hitbox.top, hitbox.right + (hitbox.right - hitbox.left), hitbox.bottom);
        } else {
            hurtbox = new Rect(hitbox.left - (hitbox.right - hitbox.left), hitbox.top, hitbox.left, hitbox.bottom);
        }
    }

    /**
     * Gestiona movimiento y cambio de frames de animacion
     */
    public void actualizaFisica() {

        if (System.currentTimeMillis() - tframe > tickFrame) {
            numFrame++;
            if (numFrame >= frames.length) numFrame = 0;
        }

    }

    /**
     * Invierte las imagenes
     *
     * @param imagen     imagen a invertir
     * @param horizontal direccion de la inversion
     * @return nueva imagen invertida
     */
    public Bitmap espejo(Bitmap imagen, Boolean horizontal) {
        Matrix matrix = new Matrix();
        if (horizontal) matrix.preScale(-1, 1);
        else matrix.preScale(1, -1);
        return Bitmap.createBitmap(imagen, 0, 0, imagen.getWidth(),
                imagen.getHeight(), matrix, false);
    }

    /**
     * Gestiona cambio de dirección del personaje hacia la izquierda
     */
    public void mueveIz() {
        parado = false;

        this.frames = mcRunCyclei;
        // numFrame=0;
        cambia = true;
        derecha = false;
        actualizaHitBox();
    }

    /**
     * Gestiona cambio de dirección del personaje hacia la derecha
     */
    public void mueveDcha() {
        parado = false;

        this.frames = mcRunCycled;
        //numFrame=0;
        cambia = true;
        derecha = true;
        actualizaHitBox();
    }

    /**
     * Gestiona la dirección de la imagen del personaje al dejar de moverse
     *
     * @param derecha Dirección hacia la que está mirando el personaje. True para derecha, false para izquierda
     */
    public void paro(boolean derecha) {
        Log.i("chocolate", "pasa PARA");
        if (derecha) this.frames = parod;
        else this.frames = paroi;
        this.numFrame = 0;
        parado = true;
        cambia = false;
        ataca = false;
    }

    /**
     * Dibuja el personaje y sus hitboxes
     *
     * @param c Canvas sobre el que dibujar
     */
    public void dibujar(Canvas c) {
        c.drawBitmap(frames[numFrame], Px, Py, null);
        c.drawRect(hitbox, p);
        c.drawRect(hurtbox, p2);
        c.drawRect(hbCentro, p3);
//        c.drawRect(hurtbox,p);
        Log.i("22", "mover perso: " + Px + " " + Py + " ->" + numFrame);
    }


    /**
     * Gestiona la accion de ataque del personaje
     *
     * @param derecha direccion del ataque
     */
    public void ataque(int tipo, boolean derecha) {
        if (!ataca) {
            switch (tipo) {
                case 1:
                    if (derecha) this.frames = mcAtk1d;
                    else this.frames = mcAtk1i;
                    break;
                case 2:
                    if (derecha) this.frames = mcAtk2d;
                    else this.frames = mcAtk2i;
                    break;
                case 3:
                    if (derecha) this.frames = mcAtk3d;
                    else this.frames = mcAtk3i;
                    break;
            }
            numFrame = 0;
            ataca = true;
        }
    }

    boolean activo = true;

    /**
     * Comprueba si el ataque golpea al enemigo
     *
     * @param hbEnem hitbox del enemigo
     * @return
     */
    public boolean golpea(Rect hbEnem) {
        Rect aux = new Rect(hbEnem.left, hbEnem.top, hbEnem.right, hbEnem.bottom);
        Rect auxPerso = new Rect(hurtbox.left, hurtbox.top, hurtbox.right, hurtbox.bottom);
        return auxPerso.intersect(aux);
    }

    /**
     * gestion de daño recibido del personaje
     *
     * @param atkEnemigo daño recibido
     */
    public void recibeDano(int atkEnemigo) {
        if (activo) {
            Log.i("TAG", "recibeDaño: " + (System.currentTimeMillis() - tvida) + " " + tickvida);
            if (System.currentTimeMillis() - tvida > tickvida) {
                vidaActual -= atkEnemigo;
                Log.i("TAG", "recibeDaño: entro ");
                tvida = System.currentTimeMillis();
            }
        }
    }

    /**
     * Uso de un objeto de recuperacion
     */
    public void bebePocion() {
        if (pociones > 0) {
            this.vidaActual = vidas;
            pociones--;
        }
    }

    /**
     * Recibir objeto pocion
     */
    public void cojePocion() {
        if (pociones < maxPociones) {
            pociones++;
        }
        pts += 10;
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
