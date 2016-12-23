package com.example.jmedinilla.relacion_2_jmedinilla;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class ConexionAsinc_EJ4 extends AppCompatActivity implements View.OnClickListener {

    EditText direccion;
    RadioButton radioJava;
    RadioButton radioApache;
    RadioButton radioAAHC;
    Button conectar;
    WebView web;
    TextView tiempo;
    EditText fichero;
    Button guardar;
    Memoria memoria;
    long inicio;
    long fin;
    public static final String JAVA = "Java";
    public static final String APACHE = "Apache";
    public static final String AAHC = "AAHC";
    public static String webToFichero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web__ej4);

        iniciar();
    }

    private void iniciar() {
        direccion = (EditText) findViewById(R.id.tbxDireccion_ej4);
        radioJava = (RadioButton) findViewById(R.id.radioJava_ej4);
        radioApache = (RadioButton) findViewById(R.id.radioApache_ej4);
        radioAAHC = (RadioButton) findViewById(R.id.radioAAHC_ej4);
        conectar = (Button) findViewById(R.id.btnConectar_ej4);
        conectar.setOnClickListener(this);
        web = (WebView) findViewById(R.id.webView_ej4);
        tiempo = (TextView) findViewById(R.id.resulMilis_ej4);
        fichero = (EditText) findViewById(R.id.tbxFichero_ej4);
        guardar = (Button) findViewById(R.id.btnGuardar_ej4);
        guardar.setOnClickListener(this);
        memoria = new Memoria(getApplicationContext());
    }

    //En primer lugar, utilizamos el método facilitado en el enunciado de la
    //relación de ejercicios para comprobar que existe conexión a internet
    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void onClick(View v) {

        String texto = direccion.getText().toString();
        Resultado resultado;
        String opcion = APACHE;
        TareaAsincrona tarea;

        //Si el botón pulsado es el de conexión, preguntará por la conexión
        //y en caso de que exista, se preguntará por la opción marcada en el
        //grupo de botones de radio
        //La conexicón se hará mediante una tarea asíncrona para Java y Apache,
        //mientras que AAHC usará una conexión HTTP distinta
        if (v == conectar) {
            if (isNetworkAvailable()) {
                if (radioAAHC.isChecked()) {
                    conectarAAHC();
                }
                else {
                    if (radioJava.isChecked()) {
                        opcion = JAVA;
                    }
                    inicio = System.currentTimeMillis();
                    tarea = new TareaAsincrona(this);
                    tarea.execute(opcion, texto);
                    tiempo.setText("Esperando . . .");
                    guardar.setEnabled(true);
                }
            } else {
                Toast.makeText(getApplicationContext(), "No hay conexión a internet disponible", Toast.LENGTH_SHORT).show();
            }
        }
        //Si el botón pulsado es el de guardar contenido, obtendrá el texto usado en el WebView
        //Si el contenido no es nulo, guardará correctamente la página introducida, mientras que
        //si el resultado ha obtenido un valor nulo, escribirá tres puntos en el fichero, a modo
        //de visualización de que algo ha fallado
        if (v == guardar) {
            try {
                if (fichero.getText().toString().equals("")) {
                    Toast.makeText(this, "No se ha introducido un nombre de fichero", Toast.LENGTH_SHORT).show();
                } else {
                    if (memoria.disponibleEscritura()) {
                        if (memoria.escribirExterna(fichero.getText().toString(), webToFichero, false, "utf-8")) {
                            Toast.makeText(this, "Fichero escrito correctamente", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "No se ha podido escribir el fichero", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Error al acceder a la tarjeta de memoria", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error al escribir el fichero", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void conectarAAHC() {
        String texto = direccion.getText().toString();
        final ProgressDialog progreso = new ProgressDialog(ConexionAsinc_EJ4.this);
        inicio = System.currentTimeMillis();

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(texto, new TextHttpResponseHandler() {
                    @Override
                    public void onStart() {
                        // called before request is started
                        progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progreso.setMessage("Conectando . . .");
                        progreso.setCancelable(false);
                        progreso.show();
                    }
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String response) {
                        // called when response HTTP status is "200 OK"
                        progreso.dismiss();
                        fin = System.currentTimeMillis();
                        web.loadDataWithBaseURL(null, response, "text/html", "UTF-8", null);
                        tiempo.setText("Duración: " + String.valueOf(fin - inicio) + " milisegundos");
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String response, Throwable t) {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                        progreso.dismiss();
                        fin = System.currentTimeMillis();
                    }
                }
        );
    }

    public class TareaAsincrona extends AsyncTask<String, Integer, Resultado> {
        private ProgressDialog progreso;
        private Context context;
        public TareaAsincrona(Context context){
            this.context = context;
        }

        protected void onPreExecute() {
            progreso = new ProgressDialog(context);
            progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progreso.setMessage("Conectando . . .");
            progreso.setCancelable(true);
            progreso.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface dialog) {
                    TareaAsincrona.this.cancel(true);
                }
            });
            progreso.show();
        }
        protected Resultado doInBackground(String... cadena) {
            int i = 1;
            Resultado resultado;
            //La posición 0 define si se abre con Java o Apache, y la 1 el contenido del mensaje
            publishProgress(i);
            if (cadena[0].equals(JAVA)) {
                resultado = Conexion_EJ4.conectarJava(cadena[1]);
            }
            else {
                resultado = Conexion_EJ4.conectarApache(cadena[1]);
            }
            return resultado;
        }
        protected void onProgressUpdate(Integer... progress) {
            progreso.setMessage("Conectando " + Integer.toString(progress[0]));
        }
        protected void onPostExecute(Resultado result) {
            progreso.dismiss();
            // mostrar el resultado
            fin = System.currentTimeMillis();
            if (result.getCodigo()) {
                if (!(result.getContenido() == null) || (result == null)) {
                    ConexionAsinc_EJ4.webToFichero = result.getContenido();
                }
                else {
                    webToFichero = ". . .";
                }
                web.loadDataWithBaseURL(null, result.getContenido(),"text/html", "UTF-8", null);
            }
            else
                web.loadDataWithBaseURL(null, result.getMensaje(),"text/html", "UTF-8", null);
            tiempo.setText("Duración: " + String.valueOf(fin - inicio) + " milisegundos");

        }
        protected void onCancelled() {
            progreso.dismiss();
            // mostrar cancelación
            web.loadDataWithBaseURL(null, "Cancelado","text/html", "UTF-8", null);
        }
    }
}
