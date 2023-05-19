package com.gabriel.juego;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {
    public MediaPlayer mediaPlayer;
    public AudioManager audioManager;

    /**
     * Gestiona la creación de la activity
     * @param savedInstanceState Información de la pantalla que se va a crear o null si es nueva
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        JuegoView JV=new JuegoView(this);
        JuegoSurfaceView JV=new JuegoSurfaceView(this);
        //SurfaceViewTeclado svt=new SurfaceViewTeclado(this);
        JV.setKeepScreenOn(true);
        setContentView(JV);
        audioManager=(AudioManager)getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        mediaPlayer= MediaPlayer.create(getApplicationContext(), R.raw.musica);
        int v= audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mediaPlayer.setVolume(2,2);
        mediaPlayer.start();

    }

    /**
     * Retoma la aplicación tras haber sido pausada
     */
    @Override
    protected void onResume() {
        if (Build.VERSION.SDK_INT < 16) { // versiones anteriores a Jelly Bean
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }else { // versiones iguales o superiores a Jelly Bean
            final int flags= View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION // Oculta la barra de navegación
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // Oculta la barra de navegación
                    | View.SYSTEM_UI_FLAG_IMMERSIVE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            final View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(flags);
            decorView.setOnSystemUiVisibilityChangeListener(visibility -> {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    decorView.setSystemUiVisibility(flags);
                }
            });
        }
        getSupportActionBar().hide(); // se oculta la barra de ActionBar
        super.onResume();
    }
}