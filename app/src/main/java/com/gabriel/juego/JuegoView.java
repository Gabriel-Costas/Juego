package com.gabriel.juego;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class JuegoView extends SurfaceView implements SurfaceHolder.Callback, SensorEventListener {

    Vibrator vibrator;

    public MediaPlayer mediaPlayer;
    public AudioManager audioManager;
    MenuAyuda ma;
    private SurfaceHolder surfaceHolder; // Interfaz abstracta para manejar la superficie de dibujado
    private Context context; // Contexto de la aplicación
    private Bitmap bitmapFondo, fondoMedio, fondoCerca, fondoLuz; // Imagen de fondo
    private Bitmap spriteMC; //Personaje
    private Bitmap[] enemigo1, enemigo2, enemigo3, enemigo4, boss; //Enemigos
    private Bitmap enem1Mov, enem2Mov1, enem3Mov, enem4Mov, bossMov;
    private Bitmap mcRun1; //run cycle
    private Bitmap mca1, fondoOpciones, home, fondoMenu, imgCreditos;
    boolean prueba = false;
    private Bitmap btnAtacar, btnPocion, btnNoPocion, btnIzda, btnDer, vida, pocionDrop, btnContinuar, btnBorrar, btnConfirmar, fondoMuerte, playImg, chkbImg0, chkbImg1, panelImg, config, esp, eng, imgHelp;
    private int anchoPantalla = 1; // Ancho de la pantalla, su valor se actualiza en el método surfaceChanged
    private int altoPantalla = 1; // Alto de la pantalla, su valor se actualiza en el método surfaceChanged
    private Hilo hilo; // Hilo encargado de dibujar y actualizar la física
    private boolean funcionando = false, nuevoJuego = false, vibracion, idioma; // Control del hilo
    public Bitmap[] mcRunCycle, mcVidas, mcAtk1, mcAtk2, mcAtk3, letras;
    ArrayList<Items> items = new ArrayList<>();
    long tiempoGenera = 0;
    int tickGenera = 2000;
    Fuente fuente;
    boolean jugando = false;
    public SoundPool efectos;
    public Integer sonidoWoosh;
    Paint paint;
    Personaje mc;
    ArrayList<Enemigo> enemigo = new ArrayList<>();
    Fondo capa1, capa2, capa3, capa4, go, opciones, fondoMenuP;
    Boton btnAtk, btnPot, btnR, btnL, btnCont, btnBorra, btnConf, btnPlay, btnCheckBox, btnConfig, btnHome, btnCreditos, btnLang, btnHelp;
    Paint fade, fadeOut;
    int pantallaJuego = 0, tiempoVibra = 100;
    String recordNombre = "", claveRecord = "", clavePuntos = "";
    Rect pantalla=new Rect(0,anchoPantalla,0,altoPantalla);

    SensorManager sensorManager;
    Sensor sensorLuz;

    long tFade = 0;
    int tickFade = 20;
    int veloFade = -5;

    String[] nombres = new String[5];
    int[] puntos = new int[5];

    Teclado teclado;

    /**
     * Obtiene archivos de la carpeta de assets para usar en el proyecto.
     * @param fichero Dirección del archivo que queremos utilizar
     * @return archivo que hemos llamado desde la carpeta
     */
    public Bitmap getAsset(String fichero) {
        try {
            InputStream is = context.getAssets().open(fichero);
            return BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Devuelve el ancho de pantalla en pixeles
     *
     * @return ancho de la pantalla en pixeles
     */
    public static int getScreenWidth() {

        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    /**
     * Devuelve el alto de pantalla en pixeles
     *
     * @return alto de pantalla en pixeles
     */
    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    /**
     * Inicialización de la vista
     *
     * @param context contexto de la vista
     */
    public JuegoView(Context context) {
        super(context);
        audioManager=(AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        this.surfaceHolder = getHolder(); // Se obtiene el holder
        this.surfaceHolder.addCallback(this); // y se indica donde van las funciones callback
        this.context = context; // Obtenemos el contexto
        hilo = new Hilo(); // Inicializamos el hilo
        setFocusable(true); // Aseguramos que reciba eventos de toque
        bitmapFondo = BitmapFactory.decodeResource(context.getResources(), R.drawable.fondo);
        fondoMedio = BitmapFactory.decodeResource(context.getResources(), R.drawable.fondo_medio);
        fondoCerca = BitmapFactory.decodeResource(context.getResources(), R.drawable.fondo_cerca);
        fondoLuz = BitmapFactory.decodeResource(context.getResources(), R.drawable.fondo_luz);
        fade = new Paint();
        fade.setColor(Color.BLACK);
        fade.setAlpha(255);

        fadeOut = new Paint();
        fadeOut.setColor(Color.RED);
        fadeOut.setAlpha(0);

        idioma=Records.leeboolean("idioma",context);
        Log.i("idioma",idioma+"");
        if (idioma){
           // btnLang.imgBoton=esp;
            cambiaIdioma("es");
        }else{
            //btnLang.imgBoton=eng;
            cambiaIdioma("en");
        }
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensorLuz = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);


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

        ma=new MenuAyuda(context,anchoPantalla,altoPantalla, idioma);
    }

    /**
     * Gestiona el movimiento de los elementos del juego e interacciones entre ellos
     */
    public void actualizarFisica() { // Actualizamos la física de los elementos en pantalla
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
                                mc.recibeDaño(enemigo.get(i).ataque);
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
                } else {
                    if (System.currentTimeMillis() - tFade >= tickFade && fadeOut.getAlpha() < 255) {

                        fadeOut.setAlpha(fadeOut.getAlpha() - veloFade);

                        tFade = System.currentTimeMillis();

                    } else {

                        prueba = true;
                    }
                }
            }
        }

    }

    /**
     * Gestiona la creacion de nuevos enemigos en la partida
     *
     * @param totalVivos total de enemigos actualmente en el juego
     */
    public void spawnEnemigo(int totalVivos) {
        if (enemigo.size() < 10) {
            int spawn = new Random().nextInt(101);
            if (spawn < 50) {
            } else {
                if (System.currentTimeMillis() - tiempoGenera > tickGenera) {
                    if (spawn < 75) {
                        enemigo.add(new Enemigo1(enemigo1));
                    } else {
                        if (spawn < 85) {
                            enemigo.add(new Enemigo3(enemigo4));
                        } else {
                            if (spawn < 95) {
                                enemigo.add(new Enemigo4(enemigo3));
                            } else {
                                enemigo.add(new Enemigo2(enemigo2));
                            }
                        }
                    }
                    tiempoGenera = System.currentTimeMillis();
                }
            }
        }
        if (mc.bossCheck>=1000){
            enemigo.add(new Boss(boss));
            mc.bossCheck=0;
        }
    }

    /**
     * Dibuja en pantalla los elementos necesarios indicados
     *
     * @param c Canvas sobre el que dibuja
     */
    public void dibujar(Canvas c) { // Rutina de dibujo en el lienzo. Se le llamará desde el hilo

        if (pantallaJuego == 0) {
            fondoMenuP.dibujar(c);
            menuP(c);
        }

        if (pantallaJuego == 1) {
            btnAtk.setEnabled = true;
            btnPot.setEnabled = true;
            btnL.setEnabled = true;
            btnR.setEnabled = true;
            try {
                capa1.dibujar(c);
                capa2.dibujar(c);
                capa3.dibujar(c);
                capa4.dibujar(c);

                paint = new Paint();
                paint.setColor(mc.color);
                paint.setStyle(Paint.Style.STROKE);
                mc.dibujar(c);
                for (Enemigo e : enemigo) {
                    e.dibujar(c);
                }

                for (Items it : items) {
                    it.dibujar(c);
                }

                int x = 100;
                for (int i = 0; i < mc.vidas; i++) {
                    c.drawBitmap(i <= mc.vidaActual - 1 ? mcVidas[0] : mcVidas[1], x + i * mcVidas[0].getWidth(), 100, null);
                }

                fuente.dibujar(c, context.getResources().getString(R.string.score) + mc.pts, getScreenWidth() / 2, 100);

                btnAtk.dibujar(c);
                btnPot.dibujar(c);
                btnL.dibujar(c);
                btnR.dibujar(c);

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
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            btnAtk.setEnabled = false;
            btnPot.setEnabled = false;
            btnL.setEnabled = false;
            btnR.setEnabled = false;
            btnCont.setEnabled = false;
        }

        if (pantallaJuego == 2) {

            go.dibujar(c);
            Records(c);
            fuente.dibujar(c, recordNombre, anchoPantalla / 4, (altoPantalla / 5) * 2);
        }

        if (pantallaJuego == 3) {
            go.dibujar(c);
            verRecords(c);
        }

        if (pantallaJuego == 4) {
            menuOp(c);
        }

        if (pantallaJuego == 5) {
            fondoMenuP.dibujar(c);
            creditos(c);
        }

        if (pantallaJuego==6){
            ayuda(c);
        }
    }

    static boolean lado;
    static int combo = 0;

    /**
     * Gestiona los eventos de toque en pantalla y las funcionalidades de los botones
     *
     * @param event tipo de evento de toque
     * @return Devuelve true cuando se esta produciendo un evento
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
//        Log.i("arranco", "!pulso");
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
//                Log.i("atacamos", "!pulso 22222 " + btnAtk.hitbox.contains(x, y) + " " + mc.ataca);
                if (btnAtk.hitbox.contains(x, y) && !mc.ataca) {
                    combo++;
                    Log.i("atacamos", "!pulso 22222 atacooo");

                    Log.i("atacamos", "ataque: runcycle");
//Sonido
                    Log.i("atacamos", efectos.toString());
                    int v= audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    efectos.play(sonidoWoosh,v,v,1,0,1);
                    switch (combo) {
                        case 1:
                            if (!lado) {
                                mc.ataque(1, false);
                            } else {
                                mc.ataque(1, true);
                            }

                            break;

                        case 2:
                            if (!lado) {
                                mc.ataque(2, false);
                            } else {
                                mc.ataque(2, true);
                            }
                            break;

                        case 3:
                            if (!lado) {
                                mc.ataque(3, false);
                            } else {
                                mc.ataque(3, true);
                            }
                            combo = 0;
                            break;
                    }

                    for (int i = enemigo.size() - 1; i >= 0; i--) {
                        if (mc.golpea(enemigo.get(i).hitbox)) {
                            vibrator.vibrate(tiempoVibra);
                            boolean muere = enemigo.get(i).recibeDaño();
                            if (muere) {
                                mc.pts += enemigo.get(i).pts;
                                mc.bossCheck+=enemigo.get(i).pts;
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
                                if (btnCont.hitbox.contains(x, y) && pantallaJuego==1) {
                                    vibrator.vibrate(tiempoVibra);
                                    pantallaJuego = 2;
                                }
                            }
                        }

                    }
                }
                for (int i = 0; i < teclado.botones.size(); i++) {
                    if (teclado.botones.get(i).hitbox.contains(x, y) && pantallaJuego == 2) {
                        vibrator.vibrate(tiempoVibra);
                        recordNombre += teclado.botones.get(i).nombre;
                    }
                }
                if (pantallaJuego == 5 ||pantallaJuego==4|| pantallaJuego == 3) {
                    if (btnHome.hitbox.contains(x, y)) {
                        vibrator.vibrate(tiempoVibra);
                        pantallaJuego = 0;
                    }
                }
//todo btnHome aqui

                if (btnBorra.hitbox.contains(x, y) && pantallaJuego == 2) {
                    vibrator.vibrate(tiempoVibra);
                    char[] recordNombreaux = recordNombre.toCharArray();
                    recordNombre = "";
                    for (int i = 0; i < recordNombreaux.length - 1; i++) {
                        recordNombre += recordNombreaux[i];
                    }
                }
                if (btnConf.hitbox.contains(x, y) && pantallaJuego == 2) {
                    vibrator.vibrate(tiempoVibra);
                    Records.guardastring(claveRecord, recordNombre, context);
                    Records.guardaint(clavePuntos, mc.pts, context);
                    pantallaJuego = 3;
                }
                if (btnPlay.hitbox.contains(x, y) && pantallaJuego == 0) {
                    vibrator.vibrate(tiempoVibra);
                    nuevoJuego = true;
                    juegoReset();
                    pantallaJuego = 1;
                }

                if (btnConfig.hitbox.contains(x, y) && pantallaJuego == 0) {
                    vibrator.vibrate(tiempoVibra);
                    pantallaJuego = 4;
                }

                if (btnCheckBox.hitbox.contains(x, y) && pantallaJuego==4) {
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

                if(btnLang.hitbox.contains(x,y)&&pantallaJuego==4){
                    //Toast.makeText(context, "idioma", Toast.LENGTH_SHORT).show();
                    idioma=Records.leeboolean("idioma",context);
                    idioma=!idioma;
                    Log.i("idioma opc1",idioma+"");
                    if (idioma){
                        btnLang.imgBoton=esp;
                        cambiaIdioma("es");
                    }else{
                        btnLang.imgBoton=eng;
                        cambiaIdioma("en");
                    }
                    Records.guardaboolean("idioma", idioma, context);
                    Log.i("idioma opc2",Records.leeboolean("idioma",context)+"");
                }

                if (btnCreditos.hitbox.contains(x, y) && pantallaJuego == 0) {
                    pantallaJuego = 5;
                }

                if (btnHelp.hitbox.contains(x,y)&&pantallaJuego==4){
                    ma.imagen=-1;
                    pantallaJuego=6;
                }
                Log.i("imagen","antes"+pantallaJuego);
                if ( pantallaJuego==6){
                    Log.i("imagen","entro");
                    if(!ma.siguiente()){
                        pantallaJuego=4;
                    }
//                    ma.dibujaSiguiente(pantallaAyuda);
//                    if (pantallaAyuda>4){
//                        pantallaJuego=4;
//                    }else{
//                        pantallaAyuda++;
//                    }

                }

                return true;

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
        }
        return true;
    }

    public void cambiaIdioma(String codIdioma) {
        Resources res=context.getResources();
        DisplayMetrics dm=res.getDisplayMetrics();
        android.content.res.Configuration conf=res.getConfiguration();
        conf.locale=new Locale(codIdioma.toLowerCase());
        res.updateConfiguration(conf, dm);
    }

    /**
     * Gestiona cambios en elementos de pantalla
     *
     * @param holder Surfaceholder que ha cambiado
     * @param format nuevo pixelformat del surface
     * @param width  nuevo ancho de la pantalla. 0 o mayor
     * @param height nuevo alto de la pantalla. 0 o mayor
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        anchoPantalla = width;
        altoPantalla = height;
        //teclado = new Teclado(getAsset("Fuente/fuente.png"));
        teclado = new Teclado();
        teclado.add("qwertyuiop", (altoPantalla / 6) * 3);
        teclado.add("asdfghjkl", (altoPantalla / 6) * 4);
        teclado.add("zxcvbnm", (altoPantalla / 6) * 5);
        for (Boton b : teclado.botones) {
            b.setEnabled = false;
        }
        hilo.setSurfaceSize(width, height);
        capa1 = new Fondo(bitmapFondo, 0, width / 180, width);
        capa2 = new Fondo(fondoMedio, 0, width / 80, width);
        capa3 = new Fondo(fondoCerca, 0, width / 60, width);
        capa4 = new Fondo(fondoLuz, 0, width / 50, width);
        hilo.setFuncionando(true);
        if (hilo.getState() == Thread.State.NEW) hilo.start();
        if (hilo.getState() == Thread.State.TERMINATED) {
            hilo = new Hilo();
            hilo.start();
        }
    }

    /**
     * Gestiona la eliminacion de una pantalla o elemento
     *
     * @param surfaceHolder Surfaceholder del que se está destruyendo el surface. No puede ser nulo
     */
    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        hilo.setFuncionando(false);
        try {
            hilo.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (sensorLuz != null) {
            sensorManager.unregisterListener(this);
        }
    }

    /**
     * Gestiona creacion de elementos
     *
     * @param surfaceHolder Surfaceholder del que se esta creando la surface
     */
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
//        hilo.setFuncionando(true);
//        if (hilo.getState() == Thread.State.NEW) hilo.start();
//        if (hilo.getState() == Thread.State.TERMINATED) {
//            hilo = new Hilo();
//            hilo.start();
//        }
        if (sensorLuz != null) {
            sensorManager.registerListener(this, sensorLuz, SensorManager.SENSOR_DELAY_GAME);
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

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    /**
     * Hilo del juego
     */
    class Hilo extends Thread {
        public Hilo() {
        }

        @Override
        public void run() {
            while (funcionando) {
                Canvas c = null; //Siempre es necesario repintar t-odo el lienzo
                try {
                    if (!surfaceHolder.getSurface().isValid()) continue;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        c = surfaceHolder.lockHardwareCanvas(); // Obtenemos el lienzo con Aceleración Hw. Desde la API 26
                    }
                    if (c == null) {
                        c = surfaceHolder.lockCanvas();
                    }
                    synchronized (surfaceHolder) { // La sincronización es necesaria por ser recurso común
                        actualizarFisica(); // Movimiento de los elementos
                        dibujar(c); // Dibujamos los elementos
                    }
                } finally { // Haya o no excepción, hay que liberar el lienzo
                    if (c != null) {
                        surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
            }
        }

        /**
         * Activa o desactiva el funcionamiento del hilo
         *
         * @param flag indica si el juego esta funcionando
         */
        void setFuncionando(boolean flag) {
            funcionando = flag;
        }


        /**
         * Tamaño e inicializacion de los elementos de la pantalla
         *
         * @param width  ancho de la pantalla
         * @param height alto de la pantalla
         */
        public void setSurfaceSize(int width, int height) {
            synchronized (surfaceHolder) {
                if (bitmapFondo != null) { // Cambiamos el tamaño de la imagen de fondo al tamaño de la pantalla
                    bitmapFondo = Bitmap.createScaledBitmap(bitmapFondo, width, height, true);
                    fondoMedio = Bitmap.createScaledBitmap(fondoMedio, width, height, true);
                    fondoCerca = Bitmap.createScaledBitmap(fondoCerca, width, height, true);
                    fondoLuz = Bitmap.createScaledBitmap(fondoLuz, width, height, true);

                    spriteMC = BitmapFactory.decodeResource(context.getResources(), R.drawable.personaje_idle1);
                    spriteMC = Bitmap.createScaledBitmap(spriteMC, spriteMC.getWidth() / 3, spriteMC.getHeight() / 3, true);
                    mc = new Personaje(context/*spriteMC*/, width,height, getScreenWidth() / 6, getScreenHeight() / 4 * 2);

                    enemigo1 = new Bitmap[2];
                    for (int i = 0; i < 2; i++) {
                        enem1Mov = getAsset("enemigo/blob.png");
                        enem1Mov = Bitmap.createBitmap(enem1Mov, (enem1Mov.getWidth() / 2) * i, 0, (enem1Mov.getWidth() / 2), enem1Mov.getHeight());
                        enemigo1[i] = Bitmap.createScaledBitmap(enem1Mov, enem1Mov.getWidth() / 2, enem1Mov.getHeight() / 2, true);
                    }

                    enemigo2 = new Bitmap[5];
                    for (int i = 0; i < 5; i++) {
                        enem2Mov1 = getAsset("enemigo/horror walk.png");
                        enem2Mov1 = Bitmap.createBitmap(enem2Mov1, (enem2Mov1.getWidth() / 5) * i, 0, enem2Mov1.getWidth() / 5, enem2Mov1.getHeight());
                        enemigo2[i] = Bitmap.createScaledBitmap(enem2Mov1, enem2Mov1.getWidth() + anchoPantalla / 3, enem2Mov1.getHeight() + altoPantalla / 3, true);

                    }

                    enemigo3 = new Bitmap[3];
                    for (int i = 0; i < 3; i++) {
                        enem3Mov = getAsset("enemigo/zomb" + (i + 1) + ".png");
                        enemigo3[i] = Bitmap.createScaledBitmap(enem3Mov, enem3Mov.getWidth() * 5, enem3Mov.getHeight() * 5, true);
                    }
//
                    enemigo4 = new Bitmap[3];
                    for (int i = 0; i < 3; i++) {
                        enem4Mov = getAsset("enemigo/beetle" + (i + 1) + ".png");
                        enemigo4[i] = Bitmap.createScaledBitmap(enem4Mov, enem4Mov.getWidth() * 5, enem4Mov.getHeight() * 5, true);
                    }

                    boss=new Bitmap[3];
                    for (int i = 0; i < 3; i++) {
                        bossMov=getAsset("enemigo/archon"+(i+1)+".png");
                        boss[i]=Bitmap.createScaledBitmap(bossMov, bossMov.getWidth()*7,bossMov.getHeight()*7,true);
                    }

                    mcRunCycle = new Bitmap[8];
                    for (int i = 1; i <= 8; i++) {
                        mcRun1 = getAsset("personaje/personaje_run" + i + ".png");
                        mcRun1 = Bitmap.createScaledBitmap(mcRun1, mcRun1.getWidth(), mcRun1.getHeight(), true);
                        mcRunCycle[i - 1] = mcRun1;
                    }


                    Bitmap[] aux = new Bitmap[13];
                    for (int i = 1; i <= 13; i++) {
                        mca1 = getAsset("personaje/a" + i + ".png");
                        aux[i - 1] = mca1;

                    }
                    mcAtk1 = new Bitmap[5];
                    mcAtk1[0] = aux[0];
                    mcAtk1[1] = aux[1];
                    mcAtk1[2] = aux[2];
                    mcAtk1[3] = aux[3];
                    mcAtk1[4] = aux[4];

                    mcAtk2 = new Bitmap[5];
                    mcAtk2[0] = aux[5];
                    mcAtk2[1] = aux[4];
                    mcAtk2[2] = aux[6];
                    mcAtk2[3] = aux[7];
                    mcAtk2[4] = aux[8];

                    mcAtk3 = new Bitmap[6];
                    mcAtk3[0] = aux[9];
                    mcAtk3[1] = aux[5];
                    mcAtk3[2] = aux[4];
                    mcAtk3[3] = aux[10];
                    mcAtk3[4] = aux[11];
                    mcAtk3[5] = aux[12];

                    btnAtacar = getAsset("Botones/Attack.png");
                    btnAtacar = Bitmap.createScaledBitmap(btnAtacar, btnAtacar.getWidth(), btnAtacar.getHeight(), true);
                    btnAtk = new Boton("Atacar", btnAtacar, (getScreenWidth() / 10) * 9, (getScreenHeight() / 4) * 3);
                    btnAtk.setEnabled = false;

                    btnPocion = getAsset("Botones/Potion.png");
                    btnPocion = Bitmap.createScaledBitmap(btnPocion, btnPocion.getWidth(), btnPocion.getHeight(), true);
                    btnPot = new Boton("Pocion", btnPocion, (getScreenWidth() / 10) * 8, (getScreenHeight() / 4) * 3);
                    btnPot.setEnabled = false;

                    btnNoPocion = getAsset("Botones/PotionUsed.png");
                    btnNoPocion = Bitmap.createScaledBitmap(btnNoPocion, btnNoPocion.getWidth(), btnNoPocion.getHeight(), true);

                    btnIzda = getAsset("Botones/arrow_left.png");
                    btnIzda = Bitmap.createScaledBitmap(btnIzda, btnIzda.getWidth() * 2, btnIzda.getHeight() * 2, true);
                    btnL = new Boton("Izquierda", btnIzda, (getScreenWidth() / 10) * 1, (getScreenHeight() / 4) * 3);
                    btnL.setEnabled = false;

                    btnDer = getAsset("Botones/arrow_right.png");
                    btnDer = Bitmap.createScaledBitmap(btnDer, btnDer.getWidth() * 2, btnDer.getHeight() * 2, true);
                    btnR = new Boton("Izquierda", btnDer, (getScreenWidth() / 10) * 2, (getScreenHeight() / 4) * 3);
                    btnR.setEnabled = false;

                    btnContinuar = getAsset("Botones/continuar.png");
                    btnContinuar = Bitmap.createScaledBitmap(btnContinuar, btnContinuar.getWidth() * 2, btnContinuar.getHeight() * 2, true);
                    btnCont = new Boton("Continuar", btnContinuar, (anchoPantalla / 9) * 4, (altoPantalla / 5) * 3);
                    btnCont.setEnabled = false;

                    mcVidas = new Bitmap[2];
                    for (int i = 0; i < 2; i++) {
                        vida = getAsset("personaje/health.png");
                        vida = Bitmap.createBitmap(vida, (vida.getWidth() / 2) * i, 0, vida.getWidth() / 2, vida.getHeight());
                        mcVidas[i] = Bitmap.createScaledBitmap(vida, +anchoPantalla / 20, anchoPantalla / 20, true);
                    }

                    pocionDrop = getAsset("personaje/hpPotion.png");
                    pocionDrop = Bitmap.createScaledBitmap(pocionDrop, (pocionDrop.getWidth()) * 7, (pocionDrop.getHeight()) * 7, true);

                    fuente = new Fuente(getAsset("Fuente/fuente.png"));

                    btnBorrar = fuente.getTexto('<');
                    btnBorra = new Boton("Borrar", btnBorrar, (anchoPantalla / 10) * 9, (altoPantalla / 6) * 4);

                    btnConfirmar = fuente.getTexto('*');
                    btnConf = new Boton("Confirmar", btnConfirmar, (anchoPantalla / 10) * 9, (altoPantalla / 6) * 5);
                    btnConf.setEnabled = false;

                    fondoMuerte = getAsset("Fuente/gameover.png");
                    fondoMuerte = Bitmap.createScaledBitmap(fondoMuerte, anchoPantalla, altoPantalla, true);
                    go = new Fondo(fondoMuerte);

                    fondoOpciones = getAsset("Botones/woodbackground.png");
                    fondoOpciones = Bitmap.createScaledBitmap(fondoOpciones, anchoPantalla, altoPantalla, true);
                    opciones = new Fondo(fondoOpciones);

                    playImg = getAsset("Botones/play.png");
                    playImg = Bitmap.createScaledBitmap(playImg, playImg.getWidth() * 4, playImg.getHeight() * 4, true);
                    btnPlay = new Boton("Play", playImg, anchoPantalla / 3, altoPantalla / 4);
                    btnPlay.setEnabled = true;

                    config = getAsset("Botones/config.png");
                    config = Bitmap.createScaledBitmap(config, config.getWidth(), config.getHeight(), true);
                    btnConfig = new Boton("Config", config, anchoPantalla / 3, (altoPantalla / 4) * 2);
                    btnConfig.setEnabled = false;

                    imgHelp=getAsset("Botones/help.png");
                    imgHelp=Bitmap.createScaledBitmap(imgHelp, imgHelp.getWidth(), imgHelp.getHeight(),true);
                    btnHelp=new Boton("Ayuda", imgHelp,(anchoPantalla / 10) * 8, (altoPantalla / 10) * 2);

                    imgCreditos = getAsset("Botones/heart.png");
                    imgCreditos = Bitmap.createScaledBitmap(imgCreditos, imgCreditos.getWidth() * 2, imgCreditos.getHeight() * 2, true);
                    btnCreditos = new Boton("creditos", imgCreditos, anchoPantalla / 3, (altoPantalla / 4) * 3);

                    panelImg = getAsset("Botones/panel.png");
                    panelImg = Bitmap.createScaledBitmap(panelImg, panelImg.getWidth() * 3, panelImg.getHeight() * 3, true);

                    chkbImg0 = getAsset("Botones/checkbox0.png");
                    chkbImg0 = Bitmap.createScaledBitmap(chkbImg0, chkbImg0.getWidth() * 4, chkbImg0.getHeight() * 4, true);

                    chkbImg1 = getAsset("Botones/checkbox1.png");
                    chkbImg1 = Bitmap.createScaledBitmap(chkbImg1, chkbImg1.getWidth() * 4, chkbImg1.getHeight() * 4, true);

                    esp=getAsset("Botones/esp.jpg");
                    esp = Bitmap.createScaledBitmap(esp, esp.getWidth() /5, esp.getHeight() /5, true);

                    eng=getAsset("Botones/eng.jpg");
                    eng = Bitmap.createScaledBitmap(eng, esp.getWidth(), esp.getHeight(), true);

                    home = getAsset("Botones/home.png");
                    home = Bitmap.createScaledBitmap(home, home.getWidth(), home.getHeight(), true);
                    btnHome = new Boton("Home", home, (anchoPantalla / 10) * 8, (altoPantalla / 10) * 8);
                    btnHome.setEnabled = false;

                    btnCheckBox = new Boton("vibracion", chkbImg1, anchoPantalla / 2, altoPantalla / 2 + panelImg.getHeight() / 3);
                    btnLang =new Boton("españa",esp,anchoPantalla/2, altoPantalla/3+panelImg.getHeight()/3);
                    if (idioma){
                        btnLang.imgBoton=esp;

                    }else{
                        btnLang.imgBoton=eng;

                    }
                    fondoMenu = getAsset("Fuente/menuP.jpg");
                    fondoMenu = Bitmap.createScaledBitmap(fondoMenu, anchoPantalla, altoPantalla, true);
                    fondoMenuP = new Fondo(fondoMenu);
                }
            }
        }
    }

    /**
     * Pantalla de creacion de nuevo record
     *
     * @param c Canvas sobre el que dibuja
     */
    public void Records(Canvas c) {
        //2
        if (pantallaJuego == 2) {
            btnCont.setEnabled = false;
            btnConf.dibujar(c);
            btnBorra.dibujar(c);
            btnConf.setEnabled = true;
            btnBorra.setEnabled = true;
            if (mc.pts > Records.leeint("puntuacion0", context)) {
                for (Boton b : teclado.botones) {
                    b.setEnabled = true;
                }
                fuente.dibujar(c, "record", (anchoPantalla / 7) * 3, (altoPantalla / 11) * 2);
                Records.guardaint("puntuacion4", Records.leeint("puntuacion3", context), context);
                Records.guardastring("nombrepunt4", Records.leestring("nombrepunt3", context), context);
                Records.guardaint("puntuacion3", Records.leeint("puntuacion2", context), context);
                Records.guardastring("nombrepunt3", Records.leestring("nombrepunt2", context), context);
                Records.guardaint("puntuacion2", Records.leeint("puntuacion1", context), context);
                Records.guardastring("nombrepunt2", Records.leestring("nombrepunt1", context), context);
                Records.guardaint("puntuacion1", Records.leeint("puntuacion0", context), context);
                Records.guardastring("nombrepunt1", Records.leestring("nombrepunt0", context), context);
                claveRecord = "nombrepunt0";
                clavePuntos = "puntuacion0";

            } else {
                if (mc.pts > Records.leeint("puntuacion1", context)) {
                    for (Boton b : teclado.botones) {
                        b.setEnabled = true;
                    }
                    fuente.dibujar(c, "record", (anchoPantalla / 7) * 3, (altoPantalla / 11) * 2);
                    Records.guardaint("puntuacion4", Records.leeint("puntuacion3", context), context);
                    Records.guardastring("nombrepunt4", Records.leestring("nombrepunt3", context), context);
                    Records.guardaint("puntuacion3", Records.leeint("puntuacion2", context), context);
                    Records.guardastring("nombrepunt3", Records.leestring("nombrepunt2", context), context);
                    Records.guardaint("puntuacion2", Records.leeint("puntuacion1", context), context);
                    Records.guardastring("nombrepunt2", Records.leestring("nombrepunt1", context), context);
                    claveRecord = "nombrepunt1";
                    clavePuntos = "puntuacion1";
                } else {
                    if (mc.pts > Records.leeint("puntuacion2", context)) {
                        for (Boton b : teclado.botones) {
                            b.setEnabled = true;
                        }
                        fuente.dibujar(c, "record", (anchoPantalla / 7) * 3, (altoPantalla / 11) * 2);
                        Records.guardaint("puntuacion4", Records.leeint("puntuacion3", context), context);
                        Records.guardastring("nombrepunt4", Records.leestring("nombrepunt3", context), context);
                        Records.guardaint("puntuacion3", Records.leeint("puntuacion2", context), context);
                        Records.guardastring("nombrepunt3", Records.leestring("nombrepunt2", context), context);
                        claveRecord = "nombrepunt2";
                        clavePuntos = "puntuacion2";
                    } else {
                        if (mc.pts > Records.leeint("puntuacion3", context)) {
                            for (Boton b : teclado.botones) {
                                b.setEnabled = true;
                            }
                            fuente.dibujar(c, "record", (anchoPantalla / 7) * 3, (altoPantalla / 11) * 2);
                            Records.guardaint("puntuacion4", Records.leeint("puntuacion3", context), context);
                            Records.guardastring("nombrepunt4", Records.leestring("nombrepunt3", context), context);
                            claveRecord = "nombrepunt3";
                            clavePuntos = "puntuacion3";
                        } else {
                            if (mc.pts > Records.leeint("puntuacion4", context)) {
                                for (Boton b : teclado.botones) {
                                    b.setEnabled = true;
                                }
                                fuente.dibujar(c, "record", (anchoPantalla / 7) * 3, (altoPantalla / 11) * 2);
                                claveRecord = "nombrepunt4";
                                clavePuntos = "puntuacion4";
                            } else {
                                pantallaJuego = 3;
                            }
                        }
                    }
                }
            }
            teclado.dibujar(c);
        } else {
            for (Boton b : teclado.botones) {
                b.setEnabled = false;
            }
            btnConf.setEnabled = false;
            btnBorra.setEnabled = false;
        }

    }

    /**
     * Pantalla de muestra de puntuaciones mas altas
     *
     * @param c Canvas sobre el que dibuja
     */
    public void verRecords(Canvas c) {
        //3
        if (pantallaJuego == 3) {
            for (int i = 0; i < 5; i++) {
                nombres[i] = Records.leestring("nombrepunt" + i, context);
                puntos[i] = Records.leeint("puntuacion" + i, context);
            }
            for (int i = 0; i < 5; i++) {
                fuente.dibujar(c, nombres[i], anchoPantalla / 5, (altoPantalla / 10) * (2 + i));
                fuente.dibujar(c, puntos[i] + "", (anchoPantalla / 5) * 3, (altoPantalla / 10) * (2 + i));
            }
            btnHome.setEnabled = true;
            btnHome.dibujar(c);
        } else {
            btnHome.setEnabled = false;
        }

    }

    /**
     * Pantalla de menu principal del juego
     *
     * @param c Canvas sobre el que dibuja
     */
    public void menuP(Canvas c) {
        //0
        if (pantallaJuego == 0) {
            vibracion = Records.leeboolean("vibra", context);
            btnPlay.dibujar(c);
            btnPlay.setEnabled = true;
            fuente.dibujar(c, context.getResources().getString(R.string.play), btnPlay.Sx + playImg.getWidth(), btnPlay.Sy + playImg.getHeight() / 2);
            btnPlay.hitbox=new Rect(btnPlay.Sx,btnPlay.Sy,btnPlay.Sx+btnPlay.imgBoton.getWidth()*5,btnPlay.Sy+btnPlay.imgBoton.getHeight());

            btnConfig.dibujar(c);
            btnConfig.setEnabled = true;
            fuente.dibujar(c, context.getResources().getString(R.string.config), btnConfig.Sx + config.getWidth(), btnConfig.Sy + config.getHeight() / 2);
            btnConfig.hitbox=new Rect(btnConfig.Sx,btnConfig.Sy,btnConfig.Sx+btnConfig.imgBoton.getWidth()*5,btnConfig.Sy+btnConfig.imgBoton.getHeight());

            btnCreditos.dibujar(c);
            btnCreditos.setEnabled = true;
            fuente.dibujar(c, context.getResources().getString(R.string.creditos), btnCreditos.Sx + imgCreditos.getWidth(), btnCreditos.Sy + imgCreditos.getHeight() / 2);
            btnCreditos.hitbox=new Rect(btnCreditos.Sx,btnCreditos.Sy,btnCreditos.Sx+btnCreditos.imgBoton.getWidth()*5,btnCreditos.Sy+btnCreditos.imgBoton.getHeight());
        } else {
            btnConfig.setEnabled = false;
            btnPlay.setEnabled = false;
        }

    }

    /**
     * Pantalla de menu de opciones
     *
     * @param c Canvas sobre el que dibuja
     */
    public void menuOp(Canvas c) {
        //4
        if (pantallaJuego == 4) {
            opciones.dibujar(c);
            c.drawBitmap(panelImg, anchoPantalla / 10, altoPantalla / 2, null);
            c.drawBitmap(panelImg, anchoPantalla / 10, altoPantalla / 3, null);
            fuente.dibujar(c, context.getResources().getString(R.string.vibracion), (anchoPantalla / 30) * 5, (altoPantalla / 7) * 4);
            fuente.dibujar(c, context.getResources().getString(R.string.idioma), (anchoPantalla / 30) * 5, (int)(altoPantalla/2.5));

            btnCheckBox.dibujar(c);
            btnCheckBox.setEnabled = true;

            btnHelp.dibujar(c);
            btnHelp.setEnabled=true;

            btnLang.dibujar(c);
            btnLang.setEnabled=true;

            //TODO boton ayuda

            btnHome.dibujar(c);
            btnHome.setEnabled = true;
        } else {
            btnCheckBox.setEnabled = false;
            btnHome.setEnabled = false;
            btnLang.setEnabled=false;
            btnHelp.setEnabled=false;
        }
    }

    /**
     * Pantalla de creditos del juego
     *
     * @param c Canvas sobre el que dibuja
     */
    public void creditos(Canvas c) {
        //5
        if (pantallaJuego == 5) {
            fuente.dibujar(c, context.getResources().getString(R.string.idea), anchoPantalla / 10, altoPantalla / 4);
            fuente.dibujar(c, context.getResources().getString(R.string.desarrollo), anchoPantalla / 10, (altoPantalla / 4) * 2);
            fuente.dibujar(c, context.getResources().getString(R.string.assets), anchoPantalla / 10, (altoPantalla / 4) * 3);
            btnHome.dibujar(c);
            btnHome.setEnabled = true;
        } else {
            btnHome.setEnabled = false;
        }

    }

    public int pantallaAyuda=0;

    public void ayuda(Canvas c){
        //6
        if (pantallaJuego==6){

            ma.imagenesAyuda();
            ma.dibujaPantalla(c);
        }
    }

    /**
     * reinicializa parametros para una partida nueva
     */
    public void juegoReset() {
        spriteMC = BitmapFactory.decodeResource(context.getResources(), R.drawable.personaje_idle1);
        spriteMC = Bitmap.createScaledBitmap(spriteMC, spriteMC.getWidth() / 3, spriteMC.getHeight() / 3, true);
        mc = new Personaje(context,/*spriteMC*/ anchoPantalla, altoPantalla,getScreenWidth() / 6, getScreenHeight() / 4 * 2);
        recordNombre = "";
        enemigo.clear();

    }
}
