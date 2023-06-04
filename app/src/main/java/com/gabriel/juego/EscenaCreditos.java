package com.gabriel.juego;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;

/**
 * Gestiona los elementos escena
 */
public class EscenaCreditos extends Escena {

    /**
     * Codigo de la escena actual
     */
    int numEscena=3;
    /**
     * Imagen de fondo de la escena
     */
    Bitmap fondoMenu;
    /**
     * Gestion del fondo de la escena
     */
    Fondo fondoMenuP;


    /**
     *
     * Constructor de la escena
     * @param context Contexto de la aplicación
     * @param numEscena Codigo de la escena
     * @param anp Alto de la pantalla
     * @param alp Ancho de la pantalla
     */
    public EscenaCreditos(Context context, int numEscena, int anp, int alp) {
        super(context,  anp, alp, numEscena);
        fondoMenu = getAsset("Fuente/menuP.jpg");
        fondoMenu = Bitmap.createScaledBitmap(fondoMenu, anchoPantalla, altoPantalla, true);
        fondoMenuP = new Fondo(fondoMenu);
        this.numEscena=numEscena;
    }

    /**
     * Dibuja en pantalla los elementos necesarios indicados
     * @param c Canvas sobre el que dibujar
     */
    public void dibujar(Canvas c){
        super.dibujar(c);
        fondoMenuP.dibujar(c);
        fuente.dibujar(c, context.getResources().getString(R.string.idea), anchoPantalla / 10, altoPantalla / 4);
        fuente.dibujar(c, context.getResources().getString(R.string.desarrollo), anchoPantalla / 10, (altoPantalla / 4) * 2);
        fuente.dibujar(c, context.getResources().getString(R.string.assets), anchoPantalla / 10, (altoPantalla / 4) * 3);
        btnHome.dibujar(c);
        btnHome.setEnabled = true;
    }

    /**
     * Gestiona eventos de toque en pantalla
     * @param event tipo de evento a gestionar
     * @return Devuelve el número de escena
     */
    int onTouchEvent(MotionEvent event){
        return super.onTouchEvent(event);
    }

}
