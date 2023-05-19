package com.gabriel.juego;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

public class MenuAyuda {

    private Context context;
    public int heigth, width;
    public Canvas canv;
    public Bitmap ayuda1, ayuda2, ayuda3, ayuda4, ayuda5;
    Bitmap[] pantallasAyuda;
    boolean idioma;
    int imagen = 0;


    /**
     * Obtiene archivos de la carpeta de assets para usar en el proyecto.
     *
     * @param fichero Dirección del archivo que queremos utilizar
     * @param context contexto de la aplicación
     * @return archivo que hemos llamado desde la carpeta
     */
    public Bitmap getAsset(String fichero, Context context) {
        try {
            InputStream is = context.getAssets().open(fichero);
            return BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            return null;
        }
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public MenuAyuda(Context context, int width, int height, boolean idioma) {
        Log.i("aaaa", "" + (context == null));
        setContext(context);
        //   this.context=
        this.width = width;
        this.heigth = height;
        this.idioma = idioma;
        pantallasAyuda = new Bitmap[5];
        imagenesAyuda();

    }

    public void imagenesAyuda() {

        if (idioma) {
            ayuda1 = getAsset("menuAyuda/ayuda1.png", context);
            ayuda2 = getAsset("menuAyuda/ayuda2.png", context);
            ayuda3 = getAsset("menuAyuda/ayuda3.png", context);
            ayuda4 = getAsset("menuAyuda/ayuda4.png", context);
            ayuda5 = getAsset("menuAyuda/ayuda5.png", context);
        } else {
            ayuda1 = getAsset("menuAyuda/ayuda1EN.png", context);
            ayuda2 = getAsset("menuAyuda/ayuda2EN.png", context);
            ayuda3 = getAsset("menuAyuda/ayuda3EN.png", context);
            ayuda4 = getAsset("menuAyuda/ayuda4EN.png", context);
            ayuda5 = getAsset("menuAyuda/ayuda5EN.png", context);
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

    public void dibujaPantalla(Canvas c) {
        Log.i("imagen", "" + imagen);
        c.drawBitmap(pantallasAyuda[imagen], width, heigth, null);
    }

    public boolean siguiente() {
        if (this.imagen >= pantallasAyuda.length - 1) {
            return false;
        } else {
            this.imagen++;
            return true;
        }


    }


}
