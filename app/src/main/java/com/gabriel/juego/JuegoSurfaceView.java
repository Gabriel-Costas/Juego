package com.gabriel.juego;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Build;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.util.Locale;

// TODO sensor luz
// TODO MOVER ESCENAS
public class JuegoSurfaceView extends SurfaceView implements SurfaceHolder.Callback, SensorEventListener {

    Vibrator vibrator;

    Escena escenaActual;
    private SurfaceHolder surfaceHolder; // Interfaz abstracta para manejar la superficie de dibujado
    private Context context; // Contexto de la aplicación


    private int anchoPantalla = 1; // Ancho de la pantalla, su valor se actualiza en el método surfaceChanged
    private int altoPantalla = 1; // Alto de la pantalla, su valor se actualiza en el método surfaceChanged
    private Hilo hilo; // Hilo encargado de dibujar y actualizar la física
    private boolean funcionando = false; // nuevoJuego = false; // vibracion, idioma; // Control del hilo



    /**
     * Inicialización de la vista
     *
     * @param context contexto de la vista
     */
    public JuegoSurfaceView(Context context) {
        super(context);
        this.context = context;

        this.surfaceHolder = getHolder(); // Se obtiene el holder
        this.surfaceHolder.addCallback(this); // y se indica donde van las funciones callback
        this.context = context; // Obtenemos el contexto
        hilo = new Hilo(); // Inicializamos el hilo
        setFocusable(true); // Aseguramos que reciba eventos de toque

    }


    /**
     * Gestiona los eventos de toque en pantalla y las funcionalidades de los botones
     *
     * @param event tipo de evento de toque
     * @return Devuelve true cuando se esta produciendo un evento
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int nuevaEscena = escenaActual.onTouchEvent(event);
        cambiaEscena(nuevaEscena);
        Log.i("escena ", "" + nuevaEscena);

        return true;
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
        Log.i("chocolate", "surfacechanged");
        this.anchoPantalla = width;
        this.altoPantalla = height;
        hilo.setSurfaceSize(width, height);
        escenaActual = new EscenaMenu(context, 1, width, height);
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
        Log.i("chocolate", "surfacedestroyed");
        hilo.setFuncionando(false);
        try {
            hilo.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gestiona creacion de elementos
     *
     * @param surfaceHolder Surfaceholder del que se esta creando la surface
     */
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {


    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    public void cambiaEscena(int nuevaEscena) { //TODO cambia escena
        if (escenaActual.numEscena != nuevaEscena) {
            escenaActual.onEscenaDestroyed();
            switch (nuevaEscena) {
                case 1:
                    escenaActual = new EscenaMenu(context, 1, anchoPantalla, altoPantalla);
                    break;
                case 2:
                    escenaActual = new EscenaJuego(context, 2, anchoPantalla, altoPantalla);
                    break;
                case 3:
                    escenaActual = new EscenaRecord(context, 3, anchoPantalla, altoPantalla);
                    break;
                case 4:
                    escenaActual = new EscenaConfig(context, 4, anchoPantalla, altoPantalla);
                    break;
                case 5:
                    escenaActual = new EscenaCreditos(context, 5, anchoPantalla, altoPantalla);
                    break;
                case 6:
                    escenaActual = new EscenaAyuda(context, 6, anchoPantalla, altoPantalla);
                    break;
            }
            escenaActual.onEscenaCreated();
        }
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
                        escenaActual.actualizarFisica(); // Movimiento de los elementos
                        escenaActual.dibujar(c); // Dibujamos los elementos
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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
            }
        }
    }
}
