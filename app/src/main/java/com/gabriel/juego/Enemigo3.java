package com.gabriel.juego;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.Random;

public class Enemigo3 extends Enemigo{

    Random rand=new Random();

    /**
     * Constructor de enemigo tipo 3
     * @param imgs Conjunto de imagenes que representan al enemigo
     */
    public Enemigo3(Bitmap[] imgs) {
        super(1, imgs);
        this.vidaActual = vida;
        pts=30;
        velocidad = 10;
        vida = 3;
        ataque = 2;
        ataqueBase = 2;
        runCycle = 0;
        tframe = 0;
        tickFrame = 150;
        velMax = 10;
        Py = (JuegoView.getScreenHeight() / 4) * 3;
        Log.i("enemigo", "Enemigo3: "+Py);
        lootChance=65;
        int randaux= rand.nextInt(101);
        if (randaux>lootChance){
            drop=true;
        }
    }
}
