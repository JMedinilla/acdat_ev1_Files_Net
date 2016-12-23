package com.example.jmedinilla.relacion_2_jmedinilla;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.CharacterPickerDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class Agenda_EJ1 extends AppCompatActivity {

    Button anadir;
    ListView miLista;
    ArrayList<String> listaContactos;
    ArrayAdapter<String> adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda__ej1);

        anadir = (Button)findViewById(R.id.btnAnadir_ej1);
        //Si se pulsa sobre el  botón de añadir un contacto, se abre una nueva actividad
        anadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Agenda_EJ1.this, AnadirContactos.class);
                startActivity(i);
            }
        });

        miLista = (ListView)findViewById(R.id.contenedor_ej1);
        listaContactos = new ArrayList<String>();
        adaptador = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listaContactos);
        miLista.setAdapter(adaptador);
        actualizar();
    }

    //Al volver a la actividad principal desde la de adición de
    //contactos, se actualiza la lista para que los cambios se
    //puedan ver inmediatamente, sin tener que reiniciar
    @Override
    protected void onResume() {
        super.onResume();
        actualizar();
        adaptador.notifyDataSetChanged();
    }

    //Se lee cada línea del fichero que almacena los contactos
    //y se añaden a una lista que luego servirá para mostrarse
    //al usuario mediante un ListView
    public void actualizar(){
        try {
            File fichero = new File(getApplicationContext().getFilesDir(), "contactos.txt");

            if(fichero.exists())
            {
                BufferedReader lector = new BufferedReader(new FileReader(fichero));
                String linea;

                while((linea = lector.readLine()) != null) {

                    if(!listaContactos.contains(linea)) {
                        listaContactos.add(linea);
                    }
                }
            }
        }
        catch (Exception e){
            //
        }

    }
}
