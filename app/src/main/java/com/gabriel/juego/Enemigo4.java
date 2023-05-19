package com.gabriel.juego;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.Random;

public class Enemigo4 extends Enemigo {

    Random rand = new Random();

    /**
     * Constructor de enemigo tipo 4
     * @param imgs Conjunto de imagenes que representan al enemigo
     */
    public Enemigo4(Bitmap[] imgs) {
        super(1, imgs);
        this.vidaActual = vida;
        pts=50;
        velocidad = 5;
        vida = 5;
        ataque = 3;
        ataqueBase = 3;
        runCycle = 0;
        tframe = 0;
        tickFrame = 150;
        velMax = 5;
        Py = JuegoView.getScreenHeight() / 2;
        Log.i("enemigo", "Enemigo4: "+Py);
        lootChance = 60;
        int randaux = rand.nextInt(101);
        if (randaux > lootChance) {
            drop = true;
        }
    }
}