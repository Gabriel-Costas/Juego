package com.gabriel.juego;

import android.app.Person;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class Enemigo {
    int Px, Py, pts;
    int velocidad;
    int ataque, ataqueBase;
    int vida, vidaActual, tipo, lootChance;
    public Bitmap imagen;
    public Rect hitbox;
    Paint p;
    Bitmap[] imagenes, imagenesDerecha, imagenesIzquierda;
    public int runCycle = 0;
    long tframe = 0;
    int tickFrame = 150;
    int velMax = 10;
    static int spawnTimer;
    static long spawnCooldown;
    boolean drop=false;



    public void enemigoSprites(){

    }

    /**
     * Constructor de nuevo elemento enemigo
     * @param tipo tipo de enemigo a crear
     * @param imagenes conjunto de frames de animacion del enemigo
     */
    public Enemigo(int tipo, Bitmap[] imagenes) {

                this.imagenes=imagenes;

        this.imagen = imagenes[0];
        this.tipo = tipo;
        this.Px = ladoSpawn();

        p = new Paint();
        p.setColor(Color.TRANSPARENT);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(10);

        this.imagenesIzquierda=new Bitmap[imagenes.length];
        this.imagenesDerecha=new Bitmap[imagenes.length];
        for (int i = 0; i < imagenes.length; i++) {
            this.imagenesDerecha[i]=espejo(imagenes[i],true);
        }
        this.imagenesIzquierda=imagenes;

        actualizaHitBox();
    }

    /**
     * crea un rectangulo auxiliar para comprobaciones de colision
     * @param r rectangulo original
     * @return rectangulo nuevo
     */
    public Rect clonaRect(Rect r){
        return new Rect(r.left,r.top,r.right,r.bottom);
    }

    /**
     * Dibuja en pantalla el enemigo creado
     * @param c Canvas sobre el que dibuja
     */
    public void dibujar(Canvas c) {
        c.drawBitmap(imagen, Px, Py, null);
        c.drawRect(hitbox, p);
    }

    /**
     * invierte las imagenes del enemigo para cambios de sentido
     * @param imagen imagen original
     * @param horizontal indica si se quiere invertir horizontal (true) o verticalmente
     * @return imagen invertida
     */
    public Bitmap espejo(Bitmap imagen, Boolean horizontal) {
        Matrix matrix = new Matrix();
        if (horizontal) matrix.preScale(-1, 1);
        else matrix.preScale(1, -1);
        return Bitmap.createBitmap(imagen, 0, 0, imagen.getWidth(),
                imagen.getHeight(), matrix, false);
    }

    /**
     * Gestiona movimiento del elemento enemigo
     * @param perso hitbox del personaje para comprobacion de colisiones y direccion de movimiento
     * @param velo velocidad de movimiento del enemigo para control de distancia con el personaje
     */
    public void mover(Rect perso, int velo) {
        Rect persoHitbox = clonaRect( perso);
        Rect enehit = clonaRect(hitbox);

        if (enehit.intersect(persoHitbox) && velo == 0) {
            if (Px < persoHitbox.centerX()) {
                if (velo == 0) Px = persoHitbox.left - imagen.getWidth();
            } else {
                if (velo == 0) Px = persoHitbox.right;
            }
            this.imagenes=imagenesIzquierda;
            actualizaHitBox();
            velocidad = 0 + velo;
        } else {
            if (Px < persoHitbox.centerX()) velocidad = velMax;
            else velocidad = -velMax;
            Px += velocidad + velo;
            this.imagenes=imagenesDerecha;
            actualizaHitBox();
        }

    }

    /**
     * Actualiza la hitbox del enemigo
     */
    public void actualizaHitBox() {
        hitbox = new Rect(Px, Py, Px + imagen.getWidth(), Py + imagen.getHeight());
    }

    /**
     * Gestiona movimiento del enemigo y frames de animacion
     */
    public void actualizaFisica() {

        if (imagenes != null) {
            if (System.currentTimeMillis() - tframe > tickFrame) {
                runCycle++;
                if (runCycle >= imagenes.length) {
                    runCycle = 0;
                }
                imagen = imagenes[runCycle];
                tframe = System.currentTimeMillis();
            }
        }
    }

    /**
     * Control de colision con el personaje jugador
     * @param perso hitbox del personaje para control de colisiones
     * @return true si se produce colisión
     */
    public boolean ataque(Rect perso) {
        Rect persoHitbox = clonaRect( perso);
        Rect enehit = clonaRect(hitbox);

        if (enehit.intersect(persoHitbox)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Gestiona el daño recibido
     * @return true si hubo daño
     */
    public boolean recibeDaño() {
        vidaActual--;
        Log.i("enemDaño", "  ataque: "+vidaActual);
        if (vidaActual <= 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Gestiona el lado de la pantalla por el que aparece el enemigo creado
     * @return 0 si aparece por la izquierda o 1 si aparece por la derecha
     */
    public int ladoSpawn(){
        int lS;
        Random rand=new Random();
        boolean izda=rand.nextBoolean();
        if (izda){
            lS=0 - imagen.getWidth();
        }else{
            lS=JuegoView.getScreenWidth();
        }
        return lS;
    }
}
