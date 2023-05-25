package com.gabriel.juego;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;

public class EscenaRecord extends Escena {
int altoPantalla,anchoPantalla;
    Context context;
    String recordNombre = "", claveRecord = "", clavePuntos = "";
    Canvas c;
   // int pts=Personaje.pts;
    Bitmap fondoMuerte,btnBorrar,btnConfirmar;
    Fondo go;
    Teclado teclado;
    Vibrator vibrator;
    Boton btnBorra,btnConf;
int numEscena=3;

    public EscenaRecord(Context context, int numEscena, int anp, int alp){
        super(context, anp, alp, numEscena);
        this.context=context;
        teclado=new Teclado(context);
        this.numEscena = numEscena;
        this.anchoPantalla = anp;
        this.altoPantalla = alp;
        fondoMuerte = getAsset("Fuente/gameover.png");
        fondoMuerte = Bitmap.createScaledBitmap(fondoMuerte, anchoPantalla, altoPantalla, true);
        go = new Fondo(fondoMuerte);
        vibrator=vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        btnBorrar = fuente.getTexto('<');
        btnBorra = new Boton("Borrar", btnBorrar, (anchoPantalla / 10) * 9, (altoPantalla / 6) * 4);

        btnConfirmar = fuente.getTexto('*');
        btnConf = new Boton("Confirmar", btnConfirmar, (anchoPantalla / 10) * 9, (altoPantalla / 6) * 5);
        btnConf.setEnabled = false;


        if (Personaje.pts > Records.leeint("puntuacion0", context)) {
            for (Boton b : teclado.botones) {
                b.setEnabled = true;
            }

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
            if (Personaje.pts > Records.leeint("puntuacion1", context)) {
                for (Boton b : teclado.botones) {
                    b.setEnabled = true;
                }
                Records.guardaint("puntuacion4", Records.leeint("puntuacion3", context), context);
                Records.guardastring("nombrepunt4", Records.leestring("nombrepunt3", context), context);
                Records.guardaint("puntuacion3", Records.leeint("puntuacion2", context), context);
                Records.guardastring("nombrepunt3", Records.leestring("nombrepunt2", context), context);
                Records.guardaint("puntuacion2", Records.leeint("puntuacion1", context), context);
                Records.guardastring("nombrepunt2", Records.leestring("nombrepunt1", context), context);
                claveRecord = "nombrepunt1";
                clavePuntos = "puntuacion1";
            } else {
                if (Personaje.pts > Records.leeint("puntuacion2", context)) {
                    for (Boton b : teclado.botones) {
                        b.setEnabled = true;
                    }
                    Records.guardaint("puntuacion4", Records.leeint("puntuacion3", context), context);
                    Records.guardastring("nombrepunt4", Records.leestring("nombrepunt3", context), context);
                    Records.guardaint("puntuacion3", Records.leeint("puntuacion2", context), context);
                    Records.guardastring("nombrepunt3", Records.leestring("nombrepunt2", context), context);
                    claveRecord = "nombrepunt2";
                    clavePuntos = "puntuacion2";
                } else {
                    if (Personaje.pts > Records.leeint("puntuacion3", context)) {
                        for (Boton b : teclado.botones) {
                            b.setEnabled = true;
                        }
                        Records.guardaint("puntuacion4", Records.leeint("puntuacion3", context), context);
                        Records.guardastring("nombrepunt4", Records.leestring("nombrepunt3", context), context);
                        claveRecord = "nombrepunt3";
                        clavePuntos = "puntuacion3";
                    } else {
                        if (Personaje.pts > Records.leeint("puntuacion4", context)) {
                            for (Boton b : teclado.botones) {
                                b.setEnabled = true;
                            }
                            claveRecord = "nombrepunt4";
                            clavePuntos = "puntuacion4";
                        } else {

                        }
                    }
                }
            }
        }


    }

        public void dibujar(Canvas c) {
            Log.i("atacamos", "!pulso 22222 pp67yppp");
            go.dibujar(c);
            teclado.dibujar(c);
            fuente.dibujar(c, "record", (anchoPantalla / 7) * 3, (altoPantalla / 11) * 2);
            fuente.dibujar(c, recordNombre, anchoPantalla / 4, (altoPantalla / 5) * 2);
// TODO Hacer que esto funcione

//            c.drawText("punto1 "+ Records.leeint("puntuacion1", context),100,100, p);
//            c.drawText("punto2 "+ Records.leeint("puntuacion2", context),100,200, p);
//            c.drawText(Records.leestring("nombrepunt3", context)+"\t"+ Records.leeint("puntuacion3", context),100,300, p);
//            c.drawText("punto4 "+ Records.leeint("puntuacion4", context),100,400, p);
//            c.drawText("punto5 "+ Records.leeint("puntuacion5", context),100,500, p);

    }


//TODO WTF???
    public int onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        for (int i = 0; i < teclado.botones.size(); i++) {
                    if (teclado.botones.get(i).hitbox.contains(x, y)) {
                        vibrator.vibrate(100);
                        recordNombre += teclado.botones.get(i).nombre;
                    }
                }

        if (btnBorra.hitbox.contains(x, y) ) {
            vibrator.vibrate(100);
            char[] recordNombreaux = recordNombre.toCharArray();
            recordNombre = "";
            for (int i = 0; i < recordNombreaux.length - 1; i++) {
                recordNombre += recordNombreaux[i];
            }
        }
              if (btnConf.hitbox.contains(x, y) ) {
                    vibrator.vibrate(100);
                    Records.guardastring(claveRecord, recordNombre, context);
                    Records.guardaint(clavePuntos, Personaje.pts, context);
                    numEscena = 1;
                }

        return numEscena;
    }

}
