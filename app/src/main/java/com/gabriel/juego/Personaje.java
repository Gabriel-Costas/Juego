package com.gabriel.juego;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.DimenRes;

import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
// TODO AL DEJAR ATAQUE PULSADO: ATAQUES LOCOS
public class Personaje {
    int color = Color.GREEN;
    int Px, Py,bossCheck;

    static int pts=0;
    int vidas, vidaActual = 5, pociones=1, maxPociones=3;
    //public Bitmap imagen, imgParado,mcRun1,mca1;
    public Rect hitbox, hurtbox, hbCentro,iFrames;
    Paint p, p2, p3;
    Bitmap[] imagenes, temp, indicaVida;
    public int numFrame = 0;
    boolean cambia = false;
    boolean derecha;
    int tickFrame = 150, tickvida = 1500;
    long tframe = 0, tvida = 0;
    long tframeCambiaSentido = 0;
    boolean parado = true;
    int anchoP,altoP;
    boolean ataca = false, vivo=true;
    public Bitmap[] mcRunCyclei,mcRunCycled, mcAtk1d, mcAtk2d, mcAtk3d, mcAtk1i, mcAtk2i, mcAtk3i,letras,frames,parod,paroi;
    Context context;


    public Bitmap[] generaEspejos(Bitmap[] imagenes){
        Bitmap[] aux=new Bitmap[imagenes.length];
        for (int i = 0; i < aux.length ; i++) {
            aux[i]=espejo(imagenes[i],true);
        }
        return aux;
    }
    /**
     * Creacion del personaje jugador

     * @param anchoP ancho de pantalla
     * @param Px tamaño en ejex
     * @param Py tamaño en ejeY
     */
    public Personaje(Context context /*, Bitmap imagen*/, int anchoP,int altoP, int Px, int Py) {
        this.context=context;
        this.Px = Px;
        this.Py = Py;
       // this.imagen = imagen;
    //    this.imgParado = imagen;
        this.anchoP = anchoP;
        this.altoP=altoP;

        p = new Paint();
        p.setColor(Color.BLUE);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(10);
        p3 = new Paint();
        p3.setColor(Color.GREEN);
        p3.setStyle(Paint.Style.STROKE);
        p3.setStrokeWidth(10);
        p2 = new Paint();
        p2.setColor(Color.RED);
        p2.setStyle(Paint.Style.STROKE);
        p2.setStrokeWidth(10);
        vidas = 5;
        derecha=true;



//ME QUIERO MORIR
        Bitmap[] aux = new Bitmap[13];
        Bitmap baux;
        for (int i = 1; i <= 13; i++) {
            baux = getAsset("personaje/a" + i + ".png");

            baux = escalaAltura(baux,altoP/2);
            aux[i - 1] = baux;
        }

        mcAtk1d = new Bitmap[5];
        mcAtk1d[0] = aux[0];
        mcAtk1d[1] = aux[1];
        mcAtk1d[2] = aux[2];
        mcAtk1d[3] = aux[3];
        mcAtk1d[4] = aux[4];
        mcAtk1i = generaEspejos(mcAtk1d);

        mcAtk2d = new Bitmap[5];
        mcAtk2d[0] = aux[5];
        mcAtk2d[1] = aux[4];
        mcAtk2d[2] = aux[6];
        mcAtk2d[3] = aux[7];
        mcAtk2d[4] = aux[8];
        mcAtk2i = generaEspejos(mcAtk2d);

        mcAtk3d = new Bitmap[6];
        mcAtk3d[0] = aux[9];
        mcAtk3d[1] = aux[5];
        mcAtk3d[2] = aux[4];
        mcAtk3d[3] = aux[10];
        mcAtk3d[4] = aux[11];
        mcAtk3d[5] = aux[12];
        mcAtk3i = generaEspejos(mcAtk3d);



        parod=new Bitmap[1];
        Bitmap auux=BitmapFactory.decodeResource(context.getResources(), R.drawable.personaje_idle1);;
        parod[0]= escalaAltura(auux,altoP/2);

        paroi=generaEspejos(parod);

        mcRunCycled= new Bitmap[8];
        for (int i = 0; i < 8; i++) {
            baux = getAsset("personaje/personaje_run" + (i+1) + ".png");

            baux = escalaAltura(baux,altoP/2);
            mcRunCycled[i] = baux;
        }
        mcRunCyclei=generaEspejos(mcRunCycled);


        frames=parod;

        actualizaHitBox();

    }

    public Bitmap escala(int res, int nuevoAncho, int nuevoAlto){
        Bitmap bitmapAux=BitmapFactory.decodeResource(context.getResources(), res);
        return bitmapAux.createScaledBitmap(bitmapAux,nuevoAncho, nuevoAlto, true);
    }

    public Bitmap escalaAnchura(int res, int nuevoAncho) {
        Bitmap bitmapAux=BitmapFactory.decodeResource(context.getResources(), res);
        if (nuevoAncho==bitmapAux.getWidth()) return bitmapAux;
        return bitmapAux.createScaledBitmap(bitmapAux, nuevoAncho, (bitmapAux.getHeight() * nuevoAncho) /
                bitmapAux.getWidth(), true);
    }
    public Bitmap escalaAltura(int res, int nuevoAlto ) {
        Bitmap bitmapAux = BitmapFactory.decodeResource(context.getResources(), res);
        if (nuevoAlto==bitmapAux.getHeight()) return bitmapAux;
        return bitmapAux.createScaledBitmap(bitmapAux, (bitmapAux.getWidth() * nuevoAlto) /
                bitmapAux.getHeight(), nuevoAlto, true);
    }
    public Bitmap escalaAltura(Bitmap bitmapAux, int nuevoAlto ) {

        if (nuevoAlto==bitmapAux.getHeight()) return bitmapAux;
        return bitmapAux.createScaledBitmap(bitmapAux, (bitmapAux.getWidth() * nuevoAlto) /
                bitmapAux.getHeight(), nuevoAlto, true);
    }

    /**
     * Gestiona la posición y tamaño de las hitbox del personaje
     */
    public void actualizaHitBox() {
        Log.i("chocolate", "pasa actualiza hitbox");
        Log.i("chocolate", derecha+"");
        hitbox = new Rect(Px + frames[numFrame].getWidth() / 3, Py + frames[numFrame].getHeight() / 6, Px + (frames[numFrame].getWidth() / 3) * 2, Py + frames[numFrame].getHeight());
        int auxh=(hitbox.right-hitbox.left)/20;

        hbCentro=new Rect(hitbox.centerX()-auxh*2,hitbox.top,hitbox.centerX()+auxh*2,hitbox.bottom);
        if (derecha) {
            hurtbox = new Rect(hitbox.right, hitbox.top, hitbox.right + (hitbox.right - hitbox.left), hitbox.bottom);
        } else {
            hurtbox = new Rect(hitbox.left - (hitbox.right - hitbox.left), hitbox.top, hitbox.left, hitbox.bottom);
        }
    }

    /**
     * Gestiona movimiento y cambio de frames de animacion
     */
    public void actualizaFisica() {

        if (System.currentTimeMillis() - tframe > tickFrame) {
            numFrame++;
            if (numFrame>=frames.length) numFrame=0;
        }


   /*     if (imagenes != null && !parado) {
            if (System.currentTimeMillis() - tframe > tickFrame) {
                runCycle++;
                if (runCycle >= imagenes.length) {
                    runCycle = 0;

                }
                imagen = imagenes[runCycle];
                tframe = System.currentTimeMillis();
            }

        } else {
            if (imagenes != null && ataca) {
                if (System.currentTimeMillis() - tframe > tickFrame) {
                    runCycle++;
                    if (runCycle >= imagenes.length) {
                        runCycle = 0;
                        ataca = false;

                    }
                    if (!ataca) {
                        this.imagen = imgParado;
                        this.imagenes = temp;
                       // this.imagen = temp[0];
                    } else {
                        imagen = imagenes[runCycle];
                    }

                    tframe = System.currentTimeMillis();
                }
            }
        }
        cambiaSentido();
        */

    }

    /**
     * Invierte las imagenes
     * @param imagen imagen a invertir
     * @param horizontal direccion de la inversion
     * @return nueva imagen invertida
     */
    public Bitmap espejo(Bitmap imagen, Boolean horizontal) {
        Matrix matrix = new Matrix();
        if (horizontal) matrix.preScale(-1, 1);
        else matrix.preScale(1, -1);
        return Bitmap.createBitmap(imagen, 0, 0, imagen.getWidth(),
                imagen.getHeight(), matrix, false);
    }

    /**
     * Gestiona cambio de dirección del personaje hacia la izquierda

     */
    public void mueveIz() {
        parado = false;

        this.frames=mcRunCyclei;
       // numFrame=0;
        cambia = true;
        derecha = false;
        actualizaHitBox();
    }

    /**
     * Gestiona cambio de dirección del personaje hacia la derecha

     */
    public void mueveDcha() {
        parado = false;

        this.frames=mcRunCycled;
        //numFrame=0;
        cambia = true;
        derecha = true;
        actualizaHitBox();
    }

    /**
     * Gestiona la dirección de la imagen del personaje al dejar de moverse

     * @param derecha Dirección hacia la que está mirando el personaje. True para derecha, false para izquierda
     */
    public void paro( boolean derecha) {
        Log.i("chocolate", "pasa PARA");
       if (derecha) this.frames=parod;
        else this.frames=paroi;
        this.numFrame=0;
        parado = true;
        cambia = false;
        ataca = false;
    }

    /**
     * Dibuja el personaje y sus hitboxes
     * @param c Canvas sobre el que dibujar
     */
    public void dibujar(Canvas c) {
        c.drawBitmap(frames[numFrame], Px, Py, null);
        c.drawRect(hitbox, p);
        c.drawRect(hurtbox, p2);
        c.drawRect(hbCentro, p3);
//        c.drawRect(hurtbox,p);
        Log.i("22", "mover perso: "+Px+" "+Py+" ->"+numFrame);
    }

    /**
     * Gestiona la posicion del personaje en pantalla cuando cambia de dirección de movimiento
     */
    public void cambiaSentido() {
        if (cambia) {
            if (derecha) {
                if (System.currentTimeMillis() - tframeCambiaSentido > tickFrame) {
                    Px -= anchoP / 80;
                    if (Px <= anchoP / 6) {
                        cambia = false;
                        Px = anchoP / 6;
                    }
                    tframe = System.currentTimeMillis();
                    actualizaHitBox();
                }

            } else {
                if (System.currentTimeMillis() - tframeCambiaSentido > tickFrame) {
                    Px += anchoP / 80;

                    if (Px >= ((anchoP / 6) * 5) - frames[numFrame].getWidth()) {
                        cambia = false;
                        Px = ((anchoP / 6) * 5) - frames[numFrame].getWidth();
                    }
                    tframe = System.currentTimeMillis();
                    actualizaHitBox();
                }
            }
        }
    }

    /**
     * Gestiona la accion de ataque del personaje

     * @param derecha direccion del ataque
     */
    public void ataque(int tipo, boolean derecha) {
        if (!ataca){
            switch (tipo) {
                case 1:
                    if (derecha) this.frames = mcAtk1d;
                    else this.frames = mcAtk1i;
                    break;
                case 2:
                    if (derecha) this.frames = mcAtk2d;
                    else this.frames = mcAtk2i;
                    break;
                case 3:
                    if (derecha) this.frames = mcAtk3d;
                    else this.frames = mcAtk3i;
                    break;
            }
            numFrame=0;

//            tframe=System.currentTimeMillis();
            Log.i("atacamos", "ataque: entro");
//            runCycle = 0;

//            temp = this.imagenes;

            if (derecha) {
//                this.imagen = imagenes[0];
//                this.imagenes = imagenes;


            } else {
//                Bitmap[] imagenesAux = new Bitmap[imagenes.length];
//                for (int i = 0; i < imagenes.length; i++) {
//                    imagenesAux[i] = espejo(imagenes[i], true);
//                }
//                this.imagen = imagenesAux[0];
//                this.imagenes = imagenesAux;
//                paro(imgParado,false);
            }
            ataca = true;
        }
    }

    boolean activo = true;

    /**
     * Comprueba si el ataque golpea al enemigo
     * @param hbEnem hitbox del enemigo
     * @return
     */
    public boolean golpea(Rect hbEnem){
        Rect aux=new Rect(hbEnem.left,hbEnem.top,hbEnem.right,hbEnem.bottom);
        Rect auxPerso=new Rect(hurtbox.left,hurtbox.top,hurtbox.right,hurtbox.bottom);
        return auxPerso.intersect(aux);
    }

    /**
     * gestion de daño recibido del personaje
     * @param atkEnemigo daño recibido
     */
    public void recibeDaño(int atkEnemigo) {
        if (activo) {
            Log.i("TAG", "recibeDaño: " + (System.currentTimeMillis() - tvida) + " " + tickvida);
            if (System.currentTimeMillis() - tvida > tickvida) {
                vidaActual-=atkEnemigo;
                Log.i("TAG", "recibeDaño: entro ");
                tvida = System.currentTimeMillis();
            }
        }
    }

    /**
     * Uso de un objeto de recuperacion
     */
    public void bebePocion(){
        if (pociones>0){
            this.vidaActual=vidas;
            pociones--;
        }
    }

    /**
     * Recibir objeto pocion
     */
    public void cojePocion(){
        if (pociones<maxPociones){
            pociones++;
        }
        pts+=10;
    }


    public Bitmap getAsset(String fichero) {
        try {
            InputStream is = context.getAssets().open(fichero);
            return BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            return null;
        }
    }
}
