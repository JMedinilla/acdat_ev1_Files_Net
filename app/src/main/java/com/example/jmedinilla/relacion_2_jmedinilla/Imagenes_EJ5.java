package com.example.jmedinilla.relacion_2_jmedinilla;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class Imagenes_EJ5 extends AppCompatActivity implements View.OnClickListener {

    EditText tbxDireccion;
    Button btnDescargar;
    ImageView imgVi;
    Button btnBajar;
    Button btnSubir;

    String[] listaLineasFichero;
    ArrayList<String> direcciones;
    Integer posicion;
    Memoria memoria;
    Resultado res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagenes__ej5);

        tbxDireccion = (EditText)findViewById(R.id.tbxDireccion_ej5);
        btnDescargar = (Button)findViewById(R.id.btnDescargar_ej5);
        btnDescargar.setOnClickListener(this);
        imgVi = (ImageView)findViewById(R.id.imgView_ej5);
        btnBajar = (Button)findViewById(R.id.btnBajar_ej5);
        btnBajar.setOnClickListener(this);
        btnSubir = (Button)findViewById(R.id.btnSubir_ej5);
        btnSubir.setOnClickListener(this);

        direcciones = new ArrayList<String>();
        posicion = 0;
        memoria = new Memoria(getApplicationContext());
        res = new Resultado();
    }

    @Override
    public void onClick(View v) {
        //Al pulsar en el botón de descarga, se realizan los métodos de escritura del
        //fichero desde la web y la lectura desde la memoria externa, además de mostrar
        //la primera imagen de la lista en el ImageView
        if(v == btnDescargar){

            leerDescarga();
            if (leerFichero()) {
                btnBajar.setEnabled(true);
                btnSubir.setEnabled(true);
                try {
                    Picasso.with(this).load(direcciones.get(0).toString()).into(imgVi);
                }
                catch (Exception e) {
                    //Si falla no se hace nada. Seguramente el archivo estaría
                    //vacío y por eso no hay posición 0 en la lista
                }
            }
        }

        //Al pulsar sobre los botones de bajar y subir, primero comprobarán que el índice siguiente
        //existe, en cuyo caso mostrarán la imagen que esté en él

        if(v == btnBajar) {
            try {
                if (--posicion <= 0) {
                    posicion = 0;
                }
                Picasso.with(this).load(direcciones.get(posicion).toString()).into(imgVi);
            }
            catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        if(v == btnSubir){
            try {
                if (++posicion >= direcciones.size()) {
                    posicion = direcciones.size() - 1;
                }
                Picasso.with(this).load(direcciones.get(posicion).toString()).into(imgVi);
            }
            catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Con el fichero ya descargado y almacenado en memoria externa, se procederá a
    //su lectura y se incluirán todas las filas del fichero en una lista de direcciones
    //que luego podremos usar mediante Picasso
    private boolean leerFichero() {
        boolean valido = false;
        direcciones.clear();

        if (memoria.disponibleEscritura()) {
            res = memoria.leerExterna("enlacesImagenes.txt", "UTF-8");
            if (res.getCodigo()) {
                listaLineasFichero = res.getContenido().split("\n");
                for (Integer i = 0; i < listaLineasFichero.length; i++) {
                    if (!listaLineasFichero[i].isEmpty()) {
                        direcciones.add(listaLineasFichero[i]);
                    }
                }
                valido = true;
                Toast.makeText(getApplicationContext(), "Hay: " + direcciones.size() + " imágenes", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, res.getMensaje(), Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "La memoria externa no está disponible", Toast.LENGTH_SHORT).show();
        }

        return valido;
    }

    //En primer lugar, lee la dirección y descarga el fichero que contiene las imágenes,
    //escribiéndolo en memoria externa
    private void leerDescarga() {
        String texto = tbxDireccion.getText().toString();

        AsyncHttpClient cliente = new AsyncHttpClient();
        cliente.get(texto, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
                String contenido = "Ha ocurrido un error:\n\n" + throwable.getMessage();
                Toast.makeText(getApplicationContext(), contenido, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                if (memoria.disponibleEscritura()) {
                    if (memoria.escribirExterna("enlacesImagenes.txt", response, false, "UTF-8")) {
                        Toast.makeText(getApplicationContext(), "Archivo creado correctamente", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Error al escribir el fichero", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Error al acceder a la memoria externa", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
