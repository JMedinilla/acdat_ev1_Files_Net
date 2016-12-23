package com.example.jmedinilla.relacion_2_jmedinilla;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class FTP_EJ6 extends AppCompatActivity implements View.OnClickListener {

    RadioButton deDolarAEuro,deEuroADolar;
    EditText Euros;
    EditText Dolares;
    double cambio;
    FTPConnection miConexion;

    //Constantes que definen el servidor al que vamos a conectarnos
    public static final String HOST = "192.168.1.151";
    public static final Integer PORT = 21;
    public static final String USER = "otrouser";
    public static final String PASSWORD = "malaga";
    public static final String CARPETA = "./ftp";
    public static final String ARCHIVO = "cambio.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ftp__ej6);
        this.deDolarAEuro = (RadioButton)findViewById(R.id.rbtDolarEuro_ej6);
        this.deEuroADolar = (RadioButton)findViewById(R.id.rbtEuroDolar_ej6);
        this.Euros = (EditText)findViewById(R.id.tbxEuros_ej6);
        this.Dolares = (EditText)findViewById(R.id.tbxDolares_ej6);

        cambio = 0;

        TareaAsincrona miTarea = new TareaAsincrona(this);
        miTarea.execute(ARCHIVO);
    }

    //Se hace lo mismo que en el ejercicio de la relación anterior, una conversión
    //de euros a dólares o viceversa, según el cambio de divisas establecidos en
    //el fichero que descargamos del servidor FTP
    @Override
    public void onClick(View v) {
        if(deDolarAEuro.isChecked() && (!Dolares.getText().toString().equals("")) && cambio != 0){
            double dinero = Double.parseDouble(Dolares.getText().toString()) * cambio;
            Euros.setText("" + dinero);
        }
        else if(deEuroADolar.isChecked() && (!Euros.getText().toString().equals("")) && cambio != 0){
            double dinero = Double.parseDouble(Euros.getText().toString()) / cambio;
            Dolares.setText("" + dinero);
        }
    }

    //Método llamado desde la tarea asíncrona para conectarse y descargar el
    //fichero desde el servidor detallado según las constantes que hay al principio
    //de la clase
    public void BajarFichero(String fichero){
        FTPConnection miConexion = new FTPConnection();
        if (miConexion.ftpConnect(HOST,PORT,USER,PASSWORD)) {
            if (!miConexion.ftpDownload(ARCHIVO, CARPETA, new File(getFilesDir(), ARCHIVO))) {
                Toast.makeText(this, "Error en la descarga del fichero", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Lee el valor introducido en el archivo descargado
    public void LeerArchivo(){
        File archivo = new File(getFilesDir(),ARCHIVO);
        try{
            BufferedReader lector = new BufferedReader(new FileReader(archivo));
            cambio = Double.parseDouble(lector.readLine().toString());
            lector.close();
        }
        catch (Exception e){}
    }

    public class TareaAsincrona extends AsyncTask<String, Integer, Resultado> {
        private ProgressDialog progreso;
        private Context context;

        public TareaAsincrona(Context context) {
            this.context = context;
        }

        protected Resultado doInBackground(String... cadena) {
            int i = 1;
            Resultado miresultado = new Resultado();
            FTP_EJ6 miActivity = FTP_EJ6.this;
            miActivity.BajarFichero(cadena[0]);
            miActivity.LeerArchivo();
            return miresultado;
        }
    }
}
