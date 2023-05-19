package com.gabriel.juego;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.Random;

public class Enemigo2 extends Enemigo{

    Random rand=new Random();

    /**
     * Constructor de enemigo tipo 2
     * @param imgs Conjunto de imagenes que representan al enemigo
     */
    public Enemigo2(Bitmap[] imgs){
        super(1, imgs);
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
        Py = JuegoView.getScreenHeight()/2;
        Log.i("enemigo", "Enemigo2: "+Py);
        lootChance=70;
        int randaux= rand.nextInt(101);
        if (randaux>lootChance){
            drop=true;
        }
    }
}
