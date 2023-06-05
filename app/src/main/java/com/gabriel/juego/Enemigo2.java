package com.gabriel.juego;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.Random;

/**
 * Elementos tipo enemigo2
 */
public class Enemigo2 extends Enemigo{

    /**
     * Generador de numero aleatorio
     */
    public Random rand=new Random();

    /**
     * Constructor de enemigo tipo 2
     * @param imgs Conjunto de imagenes que representan al enemigo
     *  @param anp ancho de la pantalla en pixeles
     *  @param alp alto de la pantalla en pixeles
     */
    public Enemigo2(Bitmap[] imgs, int anp, int alp){
        super(1, imgs, anp, alp);
        this.vidaActual = vida;
        pts=20;
        velocidad = 10;
        vida = 2;
        ataque = 1;
        ataqueBase = 1;
        runCycle = 0;
        tframe = 0;
        tickFrame = 150;
        velMax = 10;
        Py = alp/2;
        Log.i("enemigo", "Enemigo2: "+Py);
        lootChance=70;
        int randaux= rand.nextInt(101);
        if (randaux>lootChance){
            drop=true;
        }
    }
}
