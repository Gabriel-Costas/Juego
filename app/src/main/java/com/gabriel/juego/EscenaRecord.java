package com.gabriel.juego;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class EscenaRecord extends Escena {
    int altoPantalla, anchoPantalla;
    Context context;
    String recordNombre = "";
    int claveRecord;
    Canvas c;
    // int pts=Personaje.pts;
    Bitmap fondoMuerte, btnBorrar, btnConfirmar;
    Fondo go;
    Teclado teclado;
    Vibrator vibrator;
    Boton btnBorra, btnConf;
    Boolean introducirDatos = true;
    Gson gson;
    ArrayList<String> nombres;
    ArrayList<Integer> puntuaciones;

    int numEscena = 3;

    public EscenaRecord(Context context, int numEscena, int anp, int alp) {
        super(context, anp, alp, numEscena);
        gson = new Gson();
        this.context = context;
        teclado = new Teclado(context, alp);
        this.numEscena = numEscena;
        this.anchoPantalla = anp;
        this.altoPantalla = alp;
        fondoMuerte = getAsset("Fuente/gameover.png");
        fondoMuerte = Bitmap.createScaledBitmap(fondoMuerte, anchoPantalla, altoPantalla, true);
        go = new Fondo(fondoMuerte);
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        btnBorrar = fuente.getTexto('<');
        btnBorra = new Boton("Borrar", btnBorrar, (anchoPantalla / 10) * 9, (altoPantalla / 6) * 4);

        btnConfirmar = fuente.getTexto('*');
        btnConf = new Boton("Confirmar", btnConfirmar, (anchoPantalla / 10) * 9, (altoPantalla / 6) * 5);

        Type typeString = new TypeToken<ArrayList<String>>() {}.getType();
        nombres = gson.fromJson(Records.leestring("nombres", context), typeString);
        if (nombres == null) {
            nombres = new ArrayList<String>();
        }


        Type typeNumber = new TypeToken<ArrayList<Integer>>() {}.getType();
        puntuaciones = gson.fromJson(Records.leestring("puntuaciones", context), typeNumber);
        if (puntuaciones == null) {
            puntuaciones = new ArrayList<Integer>();
        }
        Log.i("vainilla", puntuaciones.toString());
        claveRecord = -1;
        if (puntuaciones.size() < 5 || (Personaje.pts > puntuaciones.get(puntuaciones.size() - 1))) {
            claveRecord = 0;
            for (int i = 0; i < puntuaciones.size(); i++) {
                Log.i("vainilla", "test" );
                Log.i("vainilla", "" + puntuaciones.get(i));
                Log.i("vainilla", "" + Personaje.pts);
                if (puntuaciones.get(i) > Personaje.pts) {
                    claveRecord = i+1;
                    Log.i("vainilla", "" + i);
                }
            }
            if (puntuaciones.size() < 5 && puntuaciones.size() > 0 && claveRecord == 0 && Personaje.pts < puntuaciones.get(puntuaciones.size() - 1)) { claveRecord = puntuaciones.size(); }

            for (Boton b : teclado.botones) {
                b.setEnabled = true;
            }
            Log.i("vainilla", "pasa ifff");

        } else {
            introducirDatos = false;
            teclado.setEnabled = false;
            btnBorra.setEnabled = false;
        }


    }

    public void dibujar(Canvas c) {
        go.dibujar(c);
        if (introducirDatos) {
            teclado.dibujar(c);
            btnBorra.dibujar(c);
            fuente.dibujar(c, "record", (anchoPantalla / 7) * 3, (altoPantalla / 11) * 2);
            fuente.dibujar(c, recordNombre, anchoPantalla / 4, (altoPantalla / 5) * 2);
        } else {
            int imprimeY = (altoPantalla / 11) * 2;
            for (int i = 0; i < nombres.size(); i++) {
                fuente.dibujar(c, nombres.get(i) + ": " + puntuaciones.get(i), (anchoPantalla / 7), imprimeY);
                imprimeY += 100;
            }
        }
        btnConf.dibujar(c);

// TODO Hacer que esto funcione

    }

    public int onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                int x = (int) event.getX();
                int y = (int) event.getY();

                for (int i = 0; i < teclado.botones.size(); i++) {
                    if (teclado.botones.get(i).hitbox.contains(x, y)) {
                        vibrator.vibrate(100);
                        recordNombre += teclado.botones.get(i).nombre;
                    }
                }

                if (btnBorra.hitbox.contains(x, y)) {
                    vibrator.vibrate(100);
                    char[] recordNombreaux = recordNombre.toCharArray();
                    recordNombre = "";
                    for (int i = 0; i < recordNombreaux.length - 1; i++) {
                        recordNombre += recordNombreaux[i];
                    }
                }
                if (btnConf.hitbox.contains(x, y)) {
                    vibrator.vibrate(100);
                    if (introducirDatos) {
                        nombres.add(claveRecord, recordNombre);
                        puntuaciones.add(claveRecord, Personaje.pts);
                        if(nombres.size() > 5) {
                            nombres.remove(nombres.size()-1);
                            puntuaciones.remove(puntuaciones.size()-1);
                        }
                        Records.guardastring("nombres", gson.toJson(nombres), context);
                        Records.guardastring("puntuaciones", gson.toJson(puntuaciones), context);


                        teclado.setEnabled = false;
                        btnBorra.setEnabled = false;
                        introducirDatos = false;
                    } else {
                        numEscena = 1;
                    }

                }

                break;
        }
        return numEscena;
    }
}
