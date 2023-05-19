package com.gabriel.juego;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import java.io.IOException;
import java.io.InputStream;

public class Escena {

    int numEscena=-1;
    int altoPantalla;
    int anchoPantalla;
    Context context;
    Rect botonIz, botonDerecha;
    Paint p;
    Rect menu;
    Fuente fuente;

    public Escena( Context context, int anchoPantalla, int altoPantalla, int numEscena) {
        this.altoPantalla = altoPantalla;
        this.anchoPantalla = anchoPantalla;
        this.context = context;
        this.fuente=fuente;
        p=new Paint();
        p.setTextSize(altoPantalla/10);
        p.setColor(Color.WHITE);
        p.setTextAlign(Paint.Align.CENTER);
        botonDerecha=new Rect(anchoPantalla-anchoPantalla/8,altoPantalla-anchoPantalla/8,anchoPantalla,altoPantalla);
        botonIz=new Rect(0,altoPantalla-anchoPantalla/8,anchoPantalla/8,altoPantalla);
        this.numEscena=numEscena;
        this.menu = new Rect(100, 100, 500, 400);
        fuente = new Fuente(getAsset("Fuente/fuente.png"));
    }


    public Bitmap getAsset(String fichero) {
        try {
            InputStream is = context.getAssets().open(fichero);
            return BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            return null;
        }
    }

    public void dibujar(Canvas c){

//        if (numEscena>1) c.drawRect(botonIz,p);
//        if (numEscena<6)c.drawRect(botonDerecha,p);
//        if (numEscena!=1) c.drawRect(menu,p);
    }

    public void actualizarFisica(){

    }

    int onTouchEvent(MotionEvent event){
        int x=(int)event.getX();
        int y=(int)event.getY();

        if (botonDerecha.contains(x,y)){
            if (numEscena<6) return numEscena+1;
        }else if(botonIz.contains(x,y)){
            if (numEscena>1)return numEscena-1;
        }
        if (numEscena!=1)   if (menu.contains(x,y)) return 1;




        return -1;
    }

}
