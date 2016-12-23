package com.example.jmedinilla.relacion_2_jmedinilla;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class Alarma_EJ2 extends AppCompatActivity {
    private int index;
    private CountDownTimer timerDown;

    //Se establece el nombre del fichero, el formato que va a tener y su contenido,
    //que en este caso serán 4 alarmas que luego tiene que leer correctamente.
    //Como carácter separador usamos el punto y coma
    private static final String FICHERO = "alarmas.txt";
    private static final String CODIFICACION = "UTF-8";
    private static final  String CONTENIDO = "1;Primera alarma\n2;Segunda alarma\n5;Tercera alarma\n7;Cuarta alarma";

    Memoria memoria;
    TextView txtSiguiente;
    TextView txtQuedan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarma__ej2);
        //
        index = 0;
        memoria = new Memoria(this);
        txtSiguiente = (TextView)findViewById(R.id.txtSiguiente_ej2);
        txtQuedan = (TextView)findViewById(R.id.txtQuedan_ej2);

        // Escribe el fichero en memoria externa con las alarmas como contenido
        memoria.escribirExterna(FICHERO,CONTENIDO,false,CODIFICACION);
        //Lee cada fila del fichero, separándolas por saltos
        String[] lineas = memoria.leerExterna(FICHERO,CODIFICACION).getContenido().split("\n");
        //Comienza un contador, basándose en las alarmas que se han obtenido del fichero
        Contador timerDown = new Contador(lineas, txtSiguiente, txtQuedan, this);

    }

}

class Contador {

    private int index = 0;
    private String[] lineas;
    private TextView txtSiguienteC;
    private TextView txtQuedanC;
    private Context contexto;

    public Contador(String[] lin, TextView txtTiempo, TextView txtQuedan, Context context) {
        this.lineas = lin;
        this.txtSiguienteC = txtTiempo;
        this.txtQuedanC = txtQuedan;
        this.contexto = context;

        //Se indican cuantas alarmas quedan por sonar en base a la cantidad de elementos
        //que tiene la lista de alarmas en su interios
        txtQuedanC.setText("Quedan " + (this.lineas.length - index) + " alarmas por activarse");
        nuevoTimerDescendente();
    }

    private void nuevoTimerDescendente(){
        new CountDownTimer(Long.parseLong(lineas[index].split(";")[0]) * 60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //En cada tick del contador, se calcula en formato texto lo que
                //queda para que la alarma suene y se muestra mediante un TextView
                int min = (int) (millisUntilFinished / 60000);
                int seg = (int) (millisUntilFinished % 60000) / 1000;
                txtSiguienteC.setText(String.format("%02d:%02d", min, seg));
            }

            @Override
            public void onFinish() {
                //Comienza a sonar la alarma cuando termina el contador
                MediaPlayer mp = MediaPlayer.create(contexto, R.raw.snd); // Sonido de alarma
                mp.start();

                //Muestra la frase correspondiente al índice de la alarma que ha sonado
                Toast.makeText(contexto, lineas[index].split(";")[1], Toast.LENGTH_LONG).show();

                //Si hay más alarmas, aumenta el índice y se iniciar un nuevo contador
                if(index<lineas.length-1){
                    index++;
                    nuevoTimerDescendente();
                }
                else{
                    //Si no queda ninguna alarma, aumenta el índice, pone a 0 el
                    //contador de la interfaz e indica que ya no quedan alarmas
                    index++;
                    txtSiguienteC.setText(String.format("%02d:%02d", 0, 0));
                }

                txtQuedanC.setText("Quedan " + (lineas.length - index) + " alarmas por activarse");
            }

        }.start();
    }
}