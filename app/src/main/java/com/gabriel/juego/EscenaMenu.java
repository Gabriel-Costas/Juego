package com.gabriel.juego;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Vibrator;
import android.view.MotionEvent;

public class EscenaMenu extends Escena{
    int numEscena=0;
    Boton btnPlay, btnConfig,btnCreditos;
    Bitmap playImg, config, imgCreditos, fondoMenu;
    Fondo fondoMenuP;
    Vibrator vibrator;
    int tiempoVibra=100;
    boolean nuevoJuego = false;


    public EscenaMenu(Context context, int numEscena, int anp, int alp){
        super(context, anp, alp, numEscena);
        this.numEscena=numEscena;

        fondoMenu = getAsset("Fuente/menuP.jpg");
        fondoMenu = Bitmap.createScaledBitmap(fondoMenu, anchoPantalla, altoPantalla, true);
        fondoMenuP = new Fondo(fondoMenu);
        playImg = getAsset("Botones/play.png");
        playImg = Bitmap.createScaledBitmap(playImg, playImg.getWidth() * 4, playImg.getHeight() * 4, true);
        config=getAsset("Botones/config.png");
        config = Bitmap.createScaledBitmap(config, config.getWidth(), config.getHeight(), true);
        imgCreditos = getAsset("Botones/heart.png");
        imgCreditos = Bitmap.createScaledBitmap(imgCreditos, imgCreditos.getWidth() * 2, imgCreditos.getHeight() * 2, true);

        btnPlay = new Boton("Play", playImg, anchoPantalla / 3, altoPantalla / 4);
        btnConfig = new Boton("Config", config, anchoPantalla / 3, (altoPantalla / 4) * 2);
        btnCreditos = new Boton("creditos", imgCreditos, anchoPantalla / 3, (altoPantalla / 4) * 3);

        btnPlay.setEnabled = true;
        btnPlay.hitbox=new Rect(btnPlay.Sx,btnPlay.Sy,btnPlay.Sx+btnPlay.imgBoton.getWidth()*5,btnPlay.Sy+btnPlay.imgBoton.getHeight());

        btnConfig.setEnabled = true;
        btnConfig.hitbox=new Rect(btnConfig.Sx,btnConfig.Sy,btnConfig.Sx+btnConfig.imgBoton.getWidth()*5,btnConfig.Sy+btnConfig.imgBoton.getHeight());

        btnCreditos.setEnabled = true;
        btnCreditos.hitbox=new Rect(btnCreditos.Sx,btnCreditos.Sy,btnCreditos.Sx+btnCreditos.imgBoton.getWidth()*5,btnCreditos.Sy+btnCreditos.imgBoton.getHeight());

        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void dibujar(Canvas c){
        fondoMenuP.dibujar(c);
        btnPlay.dibujar(c);
        fuente.dibujar(c, context.getResources().getString(R.string.play), btnPlay.Sx + playImg.getWidth(), btnPlay.Sy + playImg.getHeight() / 2);
        btnConfig.dibujar(c);
        fuente.dibujar(c, context.getResources().getString(R.string.config), btnConfig.Sx + config.getWidth(), btnConfig.Sy + config.getHeight() / 2);
        btnCreditos.dibujar(c);
        fuente.dibujar(c, context.getResources().getString(R.string.creditos), btnCreditos.Sx + imgCreditos.getWidth(), btnCreditos.Sy + imgCreditos.getHeight() / 2);
    }

    public void actualizarFisica(){

    }

    public int onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        if (btnPlay.hitbox.contains(x, y)) {
            vibrator.vibrate(tiempoVibra);
            nuevoJuego = true;
//            juegoReset();
            return 2;
        }

        if (btnConfig.hitbox.contains(x, y)) {
            vibrator.vibrate(tiempoVibra);
           return 4;
        }

        if (btnCreditos.hitbox.contains(x, y)) {
           return 5;
        }

        return numEscena;
    }
}
