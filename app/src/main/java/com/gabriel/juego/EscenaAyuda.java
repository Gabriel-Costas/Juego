package com.gabriel.juego;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import java.io.IOException;
import java.io.InputStream;

public class EscenaAyuda extends Escena{

    int numEscena=6;
    private Context context;
    public int heigth, width;
    public Canvas canv;
    public Bitmap ayuda1, ayuda2, ayuda3, ayuda4, ayuda5;
    Bitmap[] pantallasAyuda;
    boolean idioma;
    int imagen = 0;


//    /**
//     * Obtiene archivos de la carpeta de assets para usar en el proyecto.
//     *
//     * @param fichero Dirección del archivo que queremos utilizar
//     * @return archivo que hemos llamado desde la carpeta
//     */
//    public Bitmap getAsset(String fichero) {
//        try {
//            InputStream is = context.getAssets().open(fichero);
//            return BitmapFactory.decodeStream(is);
//        } catch (IOException e) {
//            return null;
//        }
//    }

    /**
     * Setter del contexto de la aplicación
     * @param context Contexto de la aplicación
     */
    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * Getter del contexto de la aplicación
     * @return Contexto de la aplicación
     */
    public Context getContext() {
        return context;
    }

    /**
     * Constructor de la escena
     * @param context Contexto de la aplicación
     * @param width Ancho de la pantalla
     * @param height Alto de la pantalla
     * @param numEscena Codigo de la escena
     */
    public EscenaAyuda(Context context, int width, int height, int numEscena) {

        super(context, width, height, numEscena); Log.i("ayuda", "" );
        this.numEscena=numEscena;
        this.idioma = Records.leeboolean("idioma", context);
        pantallasAyuda = new Bitmap[5];
        imagenesAyuda();

    }

    /**
     * Carga las imagenes del menú de ayuda
     */
    public void imagenesAyuda() {

        if (idioma) {
            ayuda1 = getAsset("menuAyuda/ayuda1.png");
            ayuda2 = getAsset("menuAyuda/ayuda2.png");
            ayuda3 = getAsset("menuAyuda/ayuda3.png");
            ayuda4 = getAsset("menuAyuda/ayuda4.png");
            ayuda5 = getAsset("menuAyuda/ayuda5.png");
        } else {
            ayuda1 = getAsset("menuAyuda/ayuda1EN.png");
            ayuda2 = getAsset("menuAyuda/ayuda2EN.png");
            ayuda3 = getAsset("menuAyuda/ayuda3EN.png");
            ayuda4 = getAsset("menuAyuda/ayuda4EN.png");
            ayuda5 = getAsset("menuAyuda/ayuda5EN.png");
        }

        pantallasAyuda[0] = ayuda1;
        pantallasAyuda[1] = ayuda2;
        pantallasAyuda[2] = ayuda3;
        pantallasAyuda[3] = ayuda4;
        pantallasAyuda[4] = ayuda5;
        //ayuda1=Bitmap.createScaledBitmap(ayuda1,width,heigth,true);
        //ayuda2=Bitmap.createScaledBitmap(ayuda2,width,heigth,true);
        //ayuda3=Bitmap.createScaledBitmap(ayuda3,width,heigth,true);
        //ayuda4=Bitmap.createScaledBitmap(ayuda4,width,heigth,true);
        //ayuda5=Bitmap.createScaledBitmap(ayuda5,width,heigth,true);
    }

    /**
     * Dibuja en pantalla los elementos necesarios indicados
     * @param c Canvas sobre el que dibujar
     */
    public void dibujar(Canvas c) {
        Log.i("imagen", "" + imagen);
        c.drawBitmap(pantallasAyuda[imagen], width, heigth, null);
    }

    /**
     * Avanza las imagenes de ayuda
     * @return true si no hay más imagenes que mostrar
     */
    public boolean siguiente() {
        if (this.imagen >= pantallasAyuda.length - 1) {
            return false;
        } else {
            this.imagen++;
            return true;
        }


    }

    /**
     * Gestiona eventos de toque en pantalla
     * @param event tipo de evento a gestionar
     * @return Devuelve el número de escena
     */
    @Override
    int onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
//
            case MotionEvent.ACTION_DOWN:
                this.imagen++;
                if (this.imagen>=pantallasAyuda.length)return 4;
                break;
        }
        return numEscena;
    }
}
