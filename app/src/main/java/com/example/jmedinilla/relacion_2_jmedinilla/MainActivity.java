package com.example.jmedinilla.relacion_2_jmedinilla;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button ej_Uno;
    Button ej_Dos;
    Button ej_Tres;
    Button ej_Cuatro;
    Button ej_Cinco;
    Button ej_Seis;
    Intent in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ej_Uno = (Button)findViewById(R.id.Ejercicio1);
        ej_Uno.setOnClickListener(this);
        ej_Dos = (Button)findViewById(R.id.Ejercicio2);
        ej_Dos.setOnClickListener(this);
        ej_Tres = (Button)findViewById(R.id.Ejercicio3);
        ej_Tres.setOnClickListener(this);
        ej_Cuatro = (Button)findViewById(R.id.Ejercicio4);
        ej_Cuatro.setOnClickListener(this);
        ej_Cinco = (Button)findViewById(R.id.Ejercicio5);
        ej_Cinco.setOnClickListener(this);
        ej_Seis = (Button)findViewById(R.id.Ejercicio6);
        ej_Seis.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == ej_Uno) {
            in = new Intent(this, Agenda_EJ1.class);
            startActivity(in);
        }
        if (v == ej_Dos) {
            in = new Intent(this, Alarma_EJ2.class);
            startActivity(in);
        }
        if (v == ej_Tres) {
            in = new Intent(this, Calendario_EJ3.class);
            startActivity(in);
        }
        if (v == ej_Cuatro) {
            in = new Intent(this, ConexionAsinc_EJ4.class);
            startActivity(in);
        }
        if (v == ej_Cinco) {
            in = new Intent(this, Imagenes_EJ5.class);
            startActivity(in);
        }
        if (v == ej_Seis) {
            in = new Intent(this, FTP_EJ6.class);
            startActivity(in);
        }
    }
}
