package com.gabriel.juego;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.Random;

public class Boss extends Enemigo{

    Random rand=new Random();

    /**
     * Constructor de nuevo elemento jefe
     *
     * @param imagenes conjunto de frames de animacion del enemigo
     */
    public Boss(Bitmap[] imagenes, int anp, int alp) {
        super(0, imagenes,anp,alp);
        this.vidaActual = vida;
        pts=100;
        velocidad = 5;
        vida = 10;
        ataque = 1;
        ataqueBase = 3;
        runCycle = 0;
        tframe = 0;
        tickFrame = 150;
        velMax = 5;
        Py = (alp / 3);
        Log.i("enemigo", "Enemigo1: "+Py);
        lootChance=100;
        int randaux= rand.nextInt(101);
        if (randaux>lootChance){
            drop=true;
        }
    }
}
