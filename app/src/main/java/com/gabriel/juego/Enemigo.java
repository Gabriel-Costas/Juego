package com.gabriel.juego;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import java.util.Random;

/**
 * Clase padre de los enemigos
 */
public class Enemigo {
    /**
     * Ancho y alto del enemigo
     */
    public int Px, Py;
    /**
     * Puntos que da el enemigo al ser eliminado
     */
    public int pts;
    /**
     * Velocidad de movimiento
     */
    public int velocidad;
    /**
     * Puntos de ataque actuales y originales del enemigo
     */
    public int ataque, ataqueBase;
    /**
     * Ancho y alto de la pantalla
     */
    public int anchopantalla, altopantalla;
    /**
     * Vida actual y original del enemigo
     */
    public int vida, vidaActual;
    /**
     * Numero identificador del tipo de enemigo
     */
    public int tipo;
    /**
     * Probabilidad de que el enemigo suelte un objeto al morir
     */
    public int lootChance;
    /**
     * Imagen del enemigo
     */
    public Bitmap imagen;
    /**
     * Hitbox del enemigo
     */
    public Rect hitbox;
    /**
     * Color de la hitbox
     */
    public Paint p;
    /**
     * Coleccion de imagenes del enemigo cuando mira hacia los lados
     */
    public Bitmap[] imagenes, imagenesDerecha, imagenesIzquierda;
    /**
     * posicion de la imagen en el vector para simular movimiento
     */
    public int runCycle = 0;
    /**
     * Tiempo inicial para el cambio de imagenes de movimiento
     */
    public long tframe = 0;
    /**
     * Tiempo al que se produce el cambio de la imagen
     */
    public int tickFrame = 150;
    /**
     * Velocidad de movimiento del enemigo
     */
    public int velMax = 10;
    /**
     * Checkea si el enemigo actual suelta un objeto al morir
     */
    public boolean drop = false;


    /**
     * Constructor de nuevo elemento enemigo
     *
     * @param tipo          tipo de enemigo a crear
     * @param imagenes      conjunto de frames de animacion del enemigo
     * @param anchopantalla ancho de la pantalla en pixeles
     * @param altopantalla  alto de la pantalla en pixeles
     */
    public Enemigo(int tipo, Bitmap[] imagenes, int anchopantalla, int altopantalla) {

        this.imagenes = imagenes;
        this.anchopantalla = anchopantalla;
        this.altopantalla = altopantalla;
        this.imagen = imagenes[0];
        this.tipo = tipo;
        this.Px = ladoSpawn();

        p = new Paint();
        p.setColor(Color.TRANSPARENT);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(10);

        this.imagenesIzquierda = new Bitmap[imagenes.length];
        this.imagenesDerecha = new Bitmap[imagenes.length];
        for (int i = 0; i < imagenes.length; i++) {
            this.imagenesDerecha[i] = espejo(imagenes[i], true);
        }
        this.imagenesIzquierda = imagenes;

        actualizaHitBox();
    }

    /**
     * crea un rectangulo auxiliar para comprobaciones de colision
     *
     * @param r rectangulo original
     * @return rectangulo nuevo
     */
    public Rect clonaRect(Rect r) {
        return new Rect(r.left, r.top, r.right, r.bottom);
    }

    /**
     * Dibuja en pantalla el enemigo creado
     *
     * @param c Canvas sobre el que dibuja
     */
    public void dibujar(Canvas c) {
        c.drawBitmap(imagen, Px, Py, null);
        c.drawRect(hitbox, p);
    }

    /**
     * invierte las imagenes del enemigo para cambios de sentido
     *
     * @param imagen     imagen original
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
     *
     * @param perso hitbox del personaje para comprobacion de colisiones y direccion de movimiento
     * @param velo  velocidad de movimiento del enemigo para control de distancia con el personaje
     */
    public void mover(Rect perso, int velo) {
        Rect persoHitbox = clonaRect(perso);
        Rect enehit = clonaRect(hitbox);

        if (enehit.intersect(persoHitbox) && velo == 0) {
            if (Px < persoHitbox.centerX()) {
                if (velo == 0) Px = persoHitbox.left - imagen.getWidth();
            } else {
                if (velo == 0) Px = persoHitbox.right;
            }
            this.imagenes = imagenesIzquierda;
            actualizaHitBox();
            velocidad = 0 + velo;
        } else {
            if (Px < persoHitbox.centerX()) velocidad = velMax;
            else velocidad = -velMax;
            Px += velocidad + velo;
            this.imagenes = imagenesDerecha;
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
     *
     * @param perso hitbox del personaje para control de colisiones
     * @return true si se produce colisión
     */
    public boolean ataque(Rect perso) {
        Rect persoHitbox = clonaRect(perso);
        persoHitbox.left -= 20;
        persoHitbox.right += 20;
        Rect enehit = clonaRect(hitbox);

        if (enehit.intersect(persoHitbox)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Gestiona el daño recibido
     *
     * @return true si hubo daño
     */
    public boolean recibeDano() {
        vidaActual--;
        Log.i("enemDaño", "  ataque: " + vidaActual);
        if (vidaActual <= 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Gestiona el lado de la pantalla por el que aparece el enemigo creado
     *
     * @return 0 si aparece por la izquierda o 1 si aparece por la derecha
     */
    public int ladoSpawn() {
        int lS;
        Random rand = new Random();
        boolean izda = rand.nextBoolean();
        if (izda) {
            lS = 0 - imagen.getWidth();
        } else {
            lS = anchopantalla;
        }
        return lS;
    }
}
