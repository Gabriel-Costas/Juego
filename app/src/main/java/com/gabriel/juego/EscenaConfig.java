package com.gabriel.juego;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;

import java.util.Locale;

/**
 * Gestiona los elementos escena
 */
public class EscenaConfig extends Escena {

    /**
     * Codigo de la escena actual
     */
    int numEscena = 2;
    /**
     * Duración de la vibración
     */
    int tiempoVibra;
    /**
     * Imagen de fondo de la escena
     */
    Bitmap fondoOpciones;
    /**
     * Imagen del panel de texto
     */
    Bitmap panelImg;
    /**
     * Imagen Checkbox de vibracion desmarcado
     */
    Bitmap chkbImg0;
    /**
     * Imagen checkbox de vibración marcado
     */
    Bitmap chkbImg1;
    /**
     * Imagen bandera española
     */
    Bitmap esp;
    /**
     * Imagen bandera Inglesa
     */
    Bitmap eng;
    /**
     * Imagen boton de ayuda
     */
    Bitmap imgHelp;
    /**
     * Gestiona el Fondo de la escena
     */
    Fondo opciones;
    /**
     * Gestiona la seleccion de la vibracion
     */
    Boton btnCheckBox;
    /**
     * Muestra las imagenes del vector de imagenes de ayuda
     */
    Boton btnHelp;
    /**
     * Gestiona la selección de idioma
     */
    Boton btnLang;
    /**
     * Checkea la seleccion de idioma y vibracion actuales
     */
    boolean idioma, vibracion;
    /**
     * Gestor de vibrador
     */
    Vibrator vibrator;

    /**
     *
     * Constructor de la escena
     * @param context Contexto de la aplicación
     * @param numEscena Codigo de la escena
     * @param anp Alto de la pantalla
     * @param alp Ancho de la pantalla
     */
    public EscenaConfig(Context context, int numEscena, int anp, int alp) {
        super(context, anp, alp, numEscena);
        vibrator=(Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
        this.numEscena = numEscena;
        fondoOpciones = getAsset("Botones/woodbackground.png");
        fondoOpciones = Bitmap.createScaledBitmap(fondoOpciones, anp, alp, true);

        panelImg = getAsset("Botones/panel.png");
        panelImg = Bitmap.createScaledBitmap(panelImg, panelImg.getWidth() * 3, panelImg.getHeight() * 3, true);

        opciones = new Fondo(fondoOpciones);
        chkbImg0 = getAsset("Botones/checkbox0.png");
        chkbImg0 = Bitmap.createScaledBitmap(chkbImg0, chkbImg0.getWidth() * 4, chkbImg0.getHeight() * 4, true);
        chkbImg1 = getAsset("Botones/checkbox1.png");
        chkbImg1 = Bitmap.createScaledBitmap(chkbImg1, chkbImg1.getWidth() * 4, chkbImg1.getHeight() * 4, true);

        esp=getAsset("Botones/esp.jpg");
        esp = Bitmap.createScaledBitmap(esp, esp.getWidth() /5, esp.getHeight() /5, true);

        eng=getAsset("Botones/eng.jpg");
        eng = Bitmap.createScaledBitmap(eng, esp.getWidth(), esp.getHeight(), true);

        btnCheckBox = new Boton("vibracion", chkbImg0, anchoPantalla / 2, altoPantalla / 2 + panelImg.getHeight() / 3);
        btnLang = new Boton("españa", esp, anchoPantalla / 2, altoPantalla / 3 + panelImg.getHeight() / 3);

        idioma = Records.leeboolean("idioma", context);

        imgHelp=getAsset("Botones/help.png");
        imgHelp=Bitmap.createScaledBitmap(imgHelp, imgHelp.getWidth(), imgHelp.getHeight(),true);
        btnHelp=new Boton("Ayuda", imgHelp,(anchoPantalla / 10) * 8, (altoPantalla / 10) * 2);
//
        if (idioma) {
            btnLang.imgBoton=esp;
            cambiaIdioma("es");
        } else {
            btnLang.imgBoton=eng;
            cambiaIdioma("en");
        }
    }

    /**
     * Dibuja en pantalla los elementos necesarios indicados
     * @param c Canvas sobre el que dibujar
     */
    public void dibujar(Canvas c) {
        super.dibujar(c);
        opciones.dibujar(c);
        c.drawBitmap(panelImg, anchoPantalla / 10, altoPantalla / 2, null);
        c.drawBitmap(panelImg, anchoPantalla / 10, altoPantalla / 3, null);
        fuente.dibujar(c, context.getResources().getString(R.string.vibracion), (anchoPantalla / 30) * 5, (altoPantalla / 7) * 4);
        fuente.dibujar(c, context.getResources().getString(R.string.idioma), (anchoPantalla / 30) * 5, (int) (altoPantalla / 2.5));

        btnCheckBox.dibujar(c);
        btnLang.dibujar(c);

        btnHome.dibujar(c);
        btnHome.setEnabled = true;
        btnHelp.dibujar(c);
    }


    /**
     * Gestiona eventos de toque en pantalla
     * @param event tipo de evento a gestionar
     * @return Devuelve el número de escena
     */
    int onTouchEvent(MotionEvent event) {

        int aux = super.onTouchEvent(event);
        int x=(int)event.getX();
        int y=(int)event.getY();
        if (aux != this.numEscena && aux != -1) return aux;
        switch (event.getAction()) {
//
            case MotionEvent.ACTION_DOWN:

                if (btnLang.hitbox.contains(x, y)) {
                    Log.i("Coor", "Coordenadas Idioma: "+btnLang.Sx+" "+btnLang.Sy);
                    idioma = Records.leeboolean("idioma", context);
                    idioma = !idioma;
                    if (idioma) {
                        btnLang.imgBoton = esp;
                        cambiaIdioma("es");
                    } else {
                        btnLang.imgBoton = eng;
                        cambiaIdioma("en");
                    }
                    Records.guardaboolean("idioma", idioma, context);
                }

                if (btnCheckBox.hitbox.contains(x, y)) {
                    Log.i("Coor", "Coordenadas Check: "+btnCheckBox.Sx+" "+btnCheckBox.Sy);
                    if (!Records.leeboolean("vibra", context)) {
                          btnCheckBox.imgBoton = chkbImg1;
                        tiempoVibra = 100;
                        vibrator.vibrate(tiempoVibra);
                        vibracion = true;
                    } else {
                        tiempoVibra = 0;
                          btnCheckBox.imgBoton = chkbImg0;
                        vibracion = false;
                    }
                    Records.guardaboolean("vibra", vibracion, context);
                }

                if (btnHelp.hitbox.contains(x,y)){
                    return 6;
                }
        }
        return super.onTouchEvent(event);



    }

    /**
     * Gestiona la selección del idioma de la aplicación
     * @param codIdioma código del idioma selecccionado
     */
    public void cambiaIdioma (String codIdioma){
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = new Locale(codIdioma.toLowerCase());
        res.updateConfiguration(conf, dm);
    }
}
