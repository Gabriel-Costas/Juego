package com.gabriel.juego;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Vibrator;
import android.view.MotionEvent;

/**
 * Gestiona el menu principal
 */
public class EscenaMenu extends Escena{
    /**
     * Codigo identificador de la escena actual
     */
    public int numEscena=0;
    /**
     * Dibuja el botón de play
     */
    public Boton btnPlay;
    /**
     * dibuja el configuracion
     */
    public Boton btnConfig;
    /**
     * Dibuja el botón creditos
     */
    public Boton btnCreditos;
    /**
     * Imagen de btnPlay
     */
    public Bitmap playImg;
    /**
     * Imagen de btnConfig
     */
    public Bitmap config;
    /**
     * Imagen de btnCreditos
     */
    public Bitmap imgCreditos;
    /**
     * Imagen de fondo de la pantalla
     */
    public Bitmap fondoMenu;
    /**
     * Gestiona el fondo de la pantalla
     */
    public Fondo fondoMenuP;
    /**
     * Gestiona la vibracion en la pantalla
     */
    public Vibrator vibrator;
    /**
     * Duracion de la vibración en ms
     */
    public int tiempoVibra=100;
    /**
     * Checkea el comienzo de una partida nueva
     */
    public boolean nuevoJuego = false;

    /**
     *
     * Constructor de la escena
     * @param context Contexto de la aplicación
     * @param numEscena Codigo de la escena
     * @param anp Alto de la pantalla
     * @param alp Ancho de la pantalla
     */
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
        btnConfig.hitbox=new Rect(btnConfig.Sx,btnConfig.Sy,btnConfig.Sx+btnConfig.imgBoton.getWidth()*10,btnConfig.Sy+btnConfig.imgBoton.getHeight());

        btnCreditos.setEnabled = true;
        btnCreditos.hitbox=new Rect(btnCreditos.Sx,btnCreditos.Sy,btnCreditos.Sx+btnCreditos.imgBoton.getWidth()*8,btnCreditos.Sy+btnCreditos.imgBoton.getHeight());

        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    /**
     * Dibuja en pantalla los elementos necesarios indicados
     * @param c Canvas sobre el que dibujar
     */
    public void dibujar(Canvas c){
        fondoMenuP.dibujar(c);
        btnPlay.dibujar(c);
        fuente.dibujar(c, context.getResources().getString(R.string.play), btnPlay.Sx + playImg.getWidth(), btnPlay.Sy + (playImg.getHeight() / 5)*2);
        btnConfig.dibujar(c);
        fuente.dibujar(c, context.getResources().getString(R.string.config), btnConfig.Sx + config.getWidth(), btnConfig.Sy + (config.getHeight() / 5)*2);
        btnCreditos.dibujar(c);
        fuente.dibujar(c, context.getResources().getString(R.string.creditos), btnCreditos.Sx + imgCreditos.getWidth(), btnCreditos.Sy + (imgCreditos.getHeight() /5)* 2);
    }


    /**
     * Gestiona eventos de toque en pantalla
     * @param event tipo de evento a gestionar
     * @return Devuelve el número de escena
     */
    public int onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        if (btnPlay.hitbox.contains(x, y)) {
            vibrator.vibrate(tiempoVibra);
            nuevoJuego = true;
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
