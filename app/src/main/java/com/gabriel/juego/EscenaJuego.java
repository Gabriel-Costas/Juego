package com.gabriel.juego;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Random;

public class EscenaJuego extends Escena implements SensorEventListener {
    SensorManager sensorManager;
    Sensor sensorLuz;
    int numEscena = 1;
    Bitmap btnAtacar, btnPocion, btnNoPocion, btnIzda, btnDer, spriteMC, vida, pocionDrop, btnContinuar;
    Boton btnAtk, btnPot, btnR, btnL, btnCont;
    Bitmap[]  mcVidas;
    Paint boton;
    Personaje mc;
    int anp, alp;
    static boolean lado;
    static int combo = 0;
    Fondo capa1, capa2, capa3, capa4;
    public SoundPool efectos;
    public Integer sonidoWoosh;
    ArrayList<Enemigo> enemigo = new ArrayList<>();
    long tiempoGenera = 0;
    int tickGenera = 2000;
    Vibrator vibrator;
    int tiempoVibra = 100;
    boolean nuevoJuego = true;
    boolean jugando = true;
    ArrayList<Items> items = new ArrayList<>();
    Paint fade, fadeOut;
    long tFade = 0;
    int tickFade = 20;
    int veloFade = -5;
    boolean prueba = false;
    public AudioManager audioManager;
    private Bitmap bitmapFondo, fondoMedio, fondoCerca, fondoLuz; // Imagen de fondo
    Bitmap[] enemigo1;
    Bitmap[] enemigo2;
    Bitmap[] enemigo3;
    Bitmap[] enemigo4;
    Bitmap[] boss;

    /**
     *
     * Constructor de la escena
     * @param context Contexto de la aplicación
     * @param numEscena Codigo de la escena
     * @param anp Alto de la pantalla
     * @param alp Ancho de la pantalla
     */
    public EscenaJuego(Context context, int numEscena, int anp, int alp) {
        super(context, anp, alp, numEscena);
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensorLuz = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        this.numEscena = numEscena;
        this.anp = anp;
        this.alp = alp;
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        mc = new Personaje(context,/*spriteMC,*/ anp, alp,anp / 6, alp / 4 * 2);

        bitmapFondo = BitmapFactory.decodeResource(context.getResources(), R.drawable.fondo);
        fondoMedio = BitmapFactory.decodeResource(context.getResources(), R.drawable.fondo_medio);
        fondoCerca = BitmapFactory.decodeResource(context.getResources(), R.drawable.fondo_cerca);
        fondoLuz = BitmapFactory.decodeResource(context.getResources(), R.drawable.fondo_luz);
        bitmapFondo = Bitmap.createScaledBitmap(bitmapFondo, anp, alp, true);
        fondoMedio = Bitmap.createScaledBitmap(fondoMedio, anp, alp, true);
        fondoCerca = Bitmap.createScaledBitmap(fondoCerca, anp, alp, true);
        fondoLuz = Bitmap.createScaledBitmap(fondoLuz, anp, alp, true);
        capa1 = new Fondo(bitmapFondo, 0, anp / 180, anp);
        capa2 = new Fondo(fondoMedio, 0, anp / 80, anp);
        capa3 = new Fondo(fondoCerca, 0, anp / 60, anp);

        capa4 = new Fondo(fondoLuz, 0, anp / 50, anp);

        mcVidas = new Bitmap[2];
        for (int i = 0; i < 2; i++) {
            vida = getAsset("personaje/health.png");
            vida = Bitmap.createBitmap(vida, (vida.getWidth() / 2) * i, 0, vida.getWidth() / 2, vida.getHeight());
            mcVidas[i] = Bitmap.createScaledBitmap(vida, +anchoPantalla / 20, anchoPantalla / 20, true);
        }

        pocionDrop = getAsset("personaje/hpPotion.png");
        pocionDrop = Bitmap.createScaledBitmap(pocionDrop, (pocionDrop.getWidth()) * 7, (pocionDrop.getHeight()) * 7, true);

        btnAtacar = getAsset("Botones/Attack.png");
        btnAtacar = Bitmap.createScaledBitmap(btnAtacar, btnAtacar.getWidth(), btnAtacar.getHeight(), true);
        btnAtk = new Boton("Atacar", btnAtacar, (anp / 10) * 9, (alp / 4) * 3);
        btnAtk.setEnabled = false;

        btnPocion = getAsset("Botones/Potion.png");
        btnPocion = Bitmap.createScaledBitmap(btnPocion, btnPocion.getWidth(), btnPocion.getHeight(), true);
        btnPot = new Boton("Pocion", btnPocion, (anp / 10) * 8, (alp / 4) * 3);
        btnPot.setEnabled = false;

        btnNoPocion = getAsset("Botones/PotionUsed.png");
        btnNoPocion = Bitmap.createScaledBitmap(btnNoPocion, btnNoPocion.getWidth(), btnNoPocion.getHeight(), true);

        btnContinuar = getAsset("Botones/continuar.png");
        btnContinuar = Bitmap.createScaledBitmap(btnContinuar, btnContinuar.getWidth() * 2, btnContinuar.getHeight() * 2, true);
        btnCont = new Boton("Continuar", btnContinuar, (anchoPantalla / 9) * 4, (altoPantalla / 5) * 3);

        btnIzda = getAsset("Botones/arrow_left.png");
        btnIzda = Bitmap.createScaledBitmap(btnIzda, btnIzda.getWidth() * 2, btnIzda.getHeight() * 2, true);
        btnL = new Boton("Izquierda", btnIzda, (anp / 10) * 1, (alp / 4) * 3);
        btnL.setEnabled = false;

        btnDer = getAsset("Botones/arrow_right.png");
        btnDer = Bitmap.createScaledBitmap(btnDer, btnDer.getWidth() * 2, btnDer.getHeight() * 2, true);
        btnR = new Boton("Izquierda", btnDer, (anp / 10) * 2, (alp / 4) * 3);
        btnR.setEnabled = false;

        fade = new Paint();
        fade.setColor(Color.BLACK);
        fade.setAlpha(255);

        fadeOut = new Paint();
        fadeOut.setColor(Color.RED);
        fadeOut.setAlpha(0);

        audioManager=(AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        enemigo1 = new Bitmap[2];
        Bitmap enem1Mov;
        for (int i = 0; i < 2; i++) {
            enem1Mov = getAsset("enemigo/blob.png");
            enem1Mov = Bitmap.createBitmap(enem1Mov, (enem1Mov.getWidth() / 2) * i, 0, (enem1Mov.getWidth() / 2), enem1Mov.getHeight());
            enemigo1[i] = Bitmap.createScaledBitmap(enem1Mov, enem1Mov.getWidth() / 2, enem1Mov.getHeight() / 2, true);
        }

        Bitmap enem2Mov1;
        enemigo2 = new Bitmap[5];
        for (int i = 0; i < 5; i++) {
            enem2Mov1 = getAsset("enemigo/horror walk.png");
            enem2Mov1 = Bitmap.createBitmap(enem2Mov1, (enem2Mov1.getWidth() / 5) * i, 0, enem2Mov1.getWidth() / 5, enem2Mov1.getHeight());
            enemigo2[i] = Bitmap.createScaledBitmap(enem2Mov1, enem2Mov1.getWidth() + anchoPantalla / 3, enem2Mov1.getHeight() + altoPantalla / 3, true);

        }

        Bitmap enem3Mov;
        enemigo3 = new Bitmap[3];
        for (int i = 0; i < 3; i++) {
            enem3Mov = getAsset("enemigo/zomb" + (i + 1) + ".png");
            enemigo3[i] = Bitmap.createScaledBitmap(enem3Mov, enem3Mov.getWidth() * 5, enem3Mov.getHeight() * 5, true);
        }
//
        Bitmap enem4Mov;
        enemigo4 = new Bitmap[3];
        for (int i = 0; i < 3; i++) {
            enem4Mov = getAsset("enemigo/beetle" + (i + 1) + ".png");
            enemigo4[i] = Bitmap.createScaledBitmap(enem4Mov, enem4Mov.getWidth() * 5, enem4Mov.getHeight() * 5, true);
        }
        Bitmap bossMov;
        boss=new Bitmap[3];
        for (int i = 0; i < 3; i++) {
            bossMov=getAsset("enemigo/archon"+(i+1)+".png");
            boss[i]=Bitmap.createScaledBitmap(bossMov, bossMov.getWidth()*7,bossMov.getHeight()*7,true);
        }

        if ((android.os.Build.VERSION.SDK_INT) >= 21) {
            SoundPool.Builder spb=new SoundPool.Builder();
            spb.setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build());
            spb.setMaxStreams(5);
            this.efectos=spb.build();
        } else {
            this.efectos=new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }
        sonidoWoosh=efectos.load(context, R.raw.swoshhh,1);


    }

    /**
     * Dibuja en pantalla los elementos necesarios indicados
     * @param c Canvas sobre el que dibujar
     */
    public void dibujar(Canvas c) {
        c.drawColor(Color.BLACK);
        capa1.dibujar(c);
        capa2.dibujar(c);
        capa3.dibujar(c);
        capa4.dibujar(c);
        mc.dibujar(c);

        for (Items it : items) {
            it.dibujar(c);
        }

        for (Enemigo e : enemigo) {
            e.dibujar(c);
        }
        btnAtk.dibujar(c);
        btnPot.dibujar(c);
        btnL.dibujar(c);
        btnR.dibujar(c);



        for (int i = 0; i < mc.vidas; i++) {
            c.drawBitmap(i <= mc.vidaActual - 1 ? mcVidas[0] : mcVidas[1], 100 + i * mcVidas[0].getWidth(), 100, null);
        }

        fuente.dibujar(c, context.getResources().getString(R.string.score) + mc.pts, anchoPantalla / 2, 100);

        if (!jugando && mc.vivo) {
            c.drawRect(0, 0, anchoPantalla, altoPantalla, fade);
        } else {
            if (!jugando && !mc.vivo) {
                btnCont.setEnabled = true;
                c.drawRect(0, 0, anchoPantalla, altoPantalla, fadeOut);

                fuente.dibujar(c, context.getString(R.string.gameOver), ((anchoPantalla/5)*2), altoPantalla / 3);
                btnCont.dibujar(c);

            }

        }

        super.dibujar(c);

    }

    /**
     * Actualiza los elementos ejecutados
     */
    public void actualizarFisica() {
        if (nuevoJuego) {
            if (jugando) {

                if (capa1 != null) capa1.mover();
                if (capa2 != null) capa2.mover();
                if (capa3 != null) capa3.mover();
                if (capa4 != null) capa4.mover();

                if (items.size() > 0) {
                    for (int i = items.size() - 1; i >= 0; i--) {
                        items.get(i).mover();
                        if (mc.hitbox.intersect(items.get(i).hitbox)) {
                            mc.cojePocion();
                            items.remove(items.get(i));
                        }
                    }
                }

                mc.actualizaFisica();

                try {
                    if (enemigo.size() > 0) {
                        for (int i = enemigo.size() - 1; i >= 0; i--) {
                            enemigo.get(i).actualizaFisica();
                            enemigo.get(i).mover(mc.hbCentro, capa4.veloDibujo);
                            if (enemigo.get(i).ataque(mc.hitbox)) {
                                vibrator.vibrate(tiempoVibra * 2);
                                mc.recibeDano(enemigo.get(i).ataque);
                                if (mc.vidaActual <= 0) {
                                    //todo morir
                                    vibrator.vibrate(tiempoVibra * 10);
                                    jugando = false;
                                    mc.vivo = false;
                                }
                            }
                        }
                    }
                } catch (IndexOutOfBoundsException e) {

                }

                spawnEnemigo(enemigo.size());
            } else {
                if (!jugando && mc.vivo) {
                    if (System.currentTimeMillis() - tFade >= tickFade) {
                        fade.setAlpha(fade.getAlpha() + veloFade);
                        tFade = System.currentTimeMillis();
                    }
                    if (fade.getAlpha() < 10) {
                        jugando = true;
                    }
                } else if (!jugando && !mc.vivo) {
                    Log.i("chocolate", "WTF BRO WTF");
                    if (System.currentTimeMillis() - tFade >= tickFade && fadeOut.getAlpha() < 255) {
                        Log.i("cafe", "pasa if");
                        fadeOut.setAlpha(fadeOut.getAlpha() - veloFade);

                        tFade = System.currentTimeMillis();

                    } else {
                        Log.i("cafe", "pasa else");
                        prueba = true;
                    }
                }
            }
        }


    }

    /**
     * Código que se ejecuta al crear una escena
     */
    @Override
    public void onEscenaCreated() {
        Log.i("chocolate", "pasacreated");
        if (sensorLuz != null) {
            sensorManager.registerListener(this, sensorLuz, SensorManager.SENSOR_DELAY_GAME);
        }
    }

    /**
     * Código que se ejecuta al destruir una escena
     */
    @Override
    public void onEscenaDestroyed() {
        Log.i("chocolate", "pasaDestroyed");
        if (sensorLuz != null) {
            sensorManager.unregisterListener(this);
        }
    }

    /**
     * Gestiona cambios en el sensor de claridad del dispositivo
     *
     * @param sensorEvent eventos del sensor de luminosidad
     */
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float luz = sensorEvent.values[0];
        Log.i("luz", "luz: " + String.valueOf(luz));
        for (Enemigo e : enemigo) {
            if (luz < 30) {
                e.ataque = e.ataqueBase + 2;
            } else {
                if (luz < 100) {
                    e.ataque = e.ataqueBase + 1;
                } else {
                    if (luz < 300) {
                        e.ataque = e.ataqueBase;
                    } else {
                        e.ataque = e.ataqueBase - 1;
                    }
                }
            }
        }
    }

    /**
     * Gestiona la sensibilidad del sensor de claridad
     * @param sensor Sensor de claridad
     * @param accuracy Precisión del sensor
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    /**
     * Gestiona eventos de toque en pantalla
     * @param event tipo de evento a gestionar
     * @return Devuelve el número de escena
     */
    int onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                if (btnCont.hitbox.contains(x,y)){
                    Log.i("atacamos", "!pulso 22222 ññññññ");
                    return 3;
                }

                if (btnAtk.hitbox.contains(x, y) && !mc.ataca) {
                    combo++;
                    Log.i("atacamos", "!pulso 22222 atacooo");

                    Log.i("atacamos", "ataque: runcycle");
//Sonido
                    Log.i("atacamos", efectos.toString());
                    int v = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    efectos.play(sonidoWoosh, v, v, 1, 0, 1);
                    switch (combo) {
                        case 1:
                            if (!lado) {
                                mc.ataque( 1,false);
                            } else {
                                mc.ataque(1, true);
                            }

                            break;

                        case 2:
                            if (!lado) {
                                mc.ataque( 2,false);
                            } else {
                                mc.ataque( 2,true);
                            }
                            break;

                        case 3:
                            if (!lado) {
                                mc.ataque( 3,false);
                            } else {
                                mc.ataque( 3,true);
                            }
                            combo = 0;
                            break;
                    }

                    for (int i = enemigo.size() - 1; i >= 0; i--) {
                        if (mc.golpea(enemigo.get(i).hitbox)) {
                            vibrator.vibrate(tiempoVibra);
                            boolean muere = enemigo.get(i).recibeDano();
                            if (muere) {
                                mc.pts += enemigo.get(i).pts;
                                mc.bossCheck += enemigo.get(i).pts;
                                if (enemigo.get(i).drop) {
                                    items.add(new Items(enemigo.get(i).Px, pocionDrop));
                                }
                                enemigo.remove(i);
                            }
                        }
                    }

                } else {
                    if (btnPot.hitbox.contains(x, y)) {
                        mc.bebePocion();
                        if (mc.pociones == 0) {
                            btnPot.imgBoton = btnNoPocion;
                        } else {
                            btnPot.imgBoton = btnPocion;
                        }
                    } else {
                        if (btnL.hitbox.contains(x, y)) {
                            Log.i("muevo", "onTouchEvent: ia");
                            capa1.mueveDcha();
                            capa1.arranco();

                            capa2.mueveDcha();
                            capa2.arranco();

                            capa3.mueveDcha();
                            capa3.arranco();

                            capa4.mueveDcha();
                            capa4.arranco();

                            for (Items it : items) {
                                it.arranco();
                                it.mueveDcha();
//                                it.mover();
                            }

                            mc.mueveIz();
                            lado = false;
                            combo = 0;
                        } else {
                            if (btnR.hitbox.contains(x, y)) {
                                Log.i("muevo", "onTouchEvent: de");
                                capa1.mueveIz();
                                capa1.arranco();


                                capa2.mueveIz();
                                capa3.mueveIz();
                                capa4.mueveIz();
                                capa2.arranco();

                                capa3.arranco();
                                capa4.arranco();

                                for (Items it : items) {
                                    it.arranco();
                                    it.mueveIz();
                                }

                                mc.mueveDcha();
                                lado = true;
                                combo = 0;
                            } else {
                            }
                        }

                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.i("Paro", "!paro 22222");
                capa1.paro();
                capa2.paro();
                capa3.paro();
                capa4.paro();
                for (Items it : items) {
                    it.paro();
                }
                mc.paro( lado);


                break;
        }
         return this.numEscena;
    }

    /**
     * Gestiona la creacion de nuevos enemigos en la partida
     *
     * @param totalVivos total de enemigos actualmente en el juego
     */
    public void spawnEnemigo ( int totalVivos){
        if (enemigo.size() < 10) {
            int spawn = new Random().nextInt(101);
            if (spawn < 50) {
            } else {

                if (System.currentTimeMillis() - tiempoGenera > tickGenera) {
                    if (spawn < 75) {
                        enemigo.add(new Enemigo1(enemigo1,anp,alp));
                    } else {
                        if (spawn < 85) {
                            enemigo.add(new Enemigo3(enemigo4,anp,alp));
                        } else {
                            if (spawn < 95) {
                                enemigo.add(new Enemigo4(enemigo3,anp,alp));
                            } else {
                                enemigo.add(new Enemigo2(enemigo2,anp,alp));
                            }
                        }
                    }
                    tiempoGenera = System.currentTimeMillis();
                }

            }
        }
        if (mc.bossCheck >= 1000) {
            enemigo.add(new Boss(boss,anp,alp));
            mc.bossCheck = 0;
        }
    }
}
