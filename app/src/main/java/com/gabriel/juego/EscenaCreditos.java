package com.gabriel.juego;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;

public class EscenaCreditos extends Escena {
    int numEscena=3;
    Bitmap fondoMenu;
    Fondo fondoMenuP;

    public EscenaCreditos(Context context, int numEscena, int anp, int alp) {
        super(context,  anp, alp, numEscena);
        fondoMenu = getAsset("Fuente/menuP.jpg");
        fondoMenu = Bitmap.createScaledBitmap(fondoMenu, anchoPantalla, altoPantalla, true);
        fondoMenuP = new Fondo(fondoMenu);
        this.numEscena=numEscena;
    }

    public void dibujar(Canvas c){
        super.dibujar(c);
        fondoMenuP.dibujar(c);
        fuente.dibujar(c, context.getResources().getString(R.string.idea), anchoPantalla / 10, altoPantalla / 4);
        fuente.dibujar(c, context.getResources().getString(R.string.desarrollo), anchoPantalla / 10, (altoPantalla / 4) * 2);
        fuente.dibujar(c, context.getResources().getString(R.string.assets), anchoPantalla / 10, (altoPantalla / 4) * 3);
        btnHome.dibujar(c);
        btnHome.setEnabled = true;
    }

    public void actualizarFisica(){

    }

    int onTouchEvent(MotionEvent event){
        return super.onTouchEvent(event);
    }

}
