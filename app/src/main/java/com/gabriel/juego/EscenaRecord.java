package com.gabriel.juego;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;

public class EscenaRecord extends Escena {
int altoPantalla,anchoPantalla;
    Context context;
    String recordNombre = "", claveRecord = "", clavePuntos = "";
    Canvas c;
    int pts=Personaje.pts;
    Bitmap fondoMuerte;
    Fondo go;
    Teclado teclado=new Teclado();
int numEscena=3;

    public EscenaRecord(Context context, int numEscena, int anp, int alp){
        super(context, anp, alp, numEscena);
        this.numEscena = numEscena;
        this.anchoPantalla = anp;
        this.altoPantalla = alp;
        Log.i("atacamos", "!pulso 22222 ppppp");
        fondoMuerte = getAsset("Fuente/gameover.png");
        fondoMuerte = Bitmap.createScaledBitmap(fondoMuerte, anchoPantalla, altoPantalla, true);
        go = new Fondo(fondoMuerte);
        Log.i("atacamos", "!pulso 22222 ppppp 555");
    }

        public void dibujar(Canvas c) {
            Log.i("atacamos", "!pulso 22222 pp67yppp");
        go.dibujar(c);
// TODO Hacer que esto funcione


            if (pts > Records.leeint("puntuacion0", context)) {
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
                if (pts > Records.leeint("puntuacion1", context)) {
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
                    if (pts > Records.leeint("puntuacion2", context)) {
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
                        if (pts > Records.leeint("puntuacion3", context)) {
                            for (Boton b : teclado.botones) {
                                b.setEnabled = true;
                            }
                            fuente.dibujar(c, "record", (anchoPantalla / 7) * 3, (altoPantalla / 11) * 2);
                            Records.guardaint("puntuacion4", Records.leeint("puntuacion3", context), context);
                            Records.guardastring("nombrepunt4", Records.leestring("nombrepunt3", context), context);
                            claveRecord = "nombrepunt3";
                            clavePuntos = "puntuacion3";
                        } else {
                            if (pts > Records.leeint("puntuacion4", context)) {
                                for (Boton b : teclado.botones) {
                                    b.setEnabled = true;
                                }
                                fuente.dibujar(c, "record", (anchoPantalla / 7) * 3, (altoPantalla / 11) * 2);
                                claveRecord = "nombrepunt4";
                                clavePuntos = "puntuacion4";
                            } else {
                              /*todo pantallaJuego = 3;*/
                            }
                        }
                    }
                }
            }
            teclado.dibujar(c);
    }


}
