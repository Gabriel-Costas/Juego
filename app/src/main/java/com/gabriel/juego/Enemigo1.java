package com.gabriel.juego;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import java.util.Random;

public class Enemigo1 extends Enemigo {

    Random rand=new Random();

    /**
     * Constructor de enemigo tipo 1
     * @param imgs Conjunto de imagenes que representan al enemigo
     */
    public Enemigo1(Bitmap[] imgs, int anp, int alp) {
        super(1,imgs,anp,alp);
        this.vidaActual = vida;
        pts=10;
        velocidad = 15;
        vida = 1;
        ataque = 1;
        ataqueBase = 1;
        runCycle = 0;
        tframe = 0;
        tickFrame = 150;
        velMax = 15;
        Py = (alp / 5) * 4;
        Log.i("enemigo", "Enemigo1: "+Py);
        lootChance=75;
        int randaux= rand.nextInt(101);
        if (randaux>lootChance){
            drop=true;
        }
    }


}
