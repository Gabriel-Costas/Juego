package com.gabriel.juego;

import android.content.Context;
import android.graphics.Canvas;

public class EscenaRecord extends Escena {
int altoPantalla,anchoPantalla;
    Context context;
    String recordNombre = "", claveRecord = "", clavePuntos = "";
int numEscena;
    public EscenaRecord(Context context, int numEscena, int anp, int alp){
        super(context, anp, alp, numEscena);
        this.numEscena = numEscena;
        this.anchoPantalla = anp;
        this.altoPantalla = alp;

    }

        public void Records(Canvas c,int pts) {


        Teclado teclado=new Teclado();

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
