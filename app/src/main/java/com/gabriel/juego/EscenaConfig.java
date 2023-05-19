package com.gabriel.juego;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;

public class EscenaConfig extends Escena {
    int numEscena=2;

    public EscenaConfig(Context context, int numEscena, int anp, int alp) {
        super(context,  anp, alp, numEscena);
        this.numEscena=numEscena;
    }
    public void dibujar(Canvas c){
        c.drawColor(Color.BLUE);
        super.dibujar(c);
        c.drawText("Escena "+numEscena,anchoPantalla/2, altoPantalla/10,p);
    }

    public void actualizarFisica(){

    }

    int onTouchEvent(MotionEvent event){

        int aux=super.onTouchEvent(event);
        if (aux!=this.numEscena && aux!=-1) return aux;
        return this.numEscena;
    }

}
