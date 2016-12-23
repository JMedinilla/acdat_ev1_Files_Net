package com.example.jmedinilla.relacion_2_jmedinilla;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class AnadirContactos extends AppCompatActivity implements View.OnClickListener {

    Button add;
    Button cancel;
    EditText name;
    EditText phone;
    EditText mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_contactos);


        add = (Button)findViewById(R.id.btnAnadir_ej1_anadirC);
        add.setOnClickListener(this);
        cancel = (Button)findViewById(R.id.btnCancelar_ej1_anadirC);
        cancel.setOnClickListener(this);
        name = (EditText)findViewById(R.id.tbxNombre_ej1_anadirC);
        phone = (EditText)findViewById(R.id.tbxTelefono_ej1_anadirC);
        mail = (EditText)findViewById(R.id.tbxEmail_ej1_anadirC);
    }

    //Al pulsar en añadir un contacto, siempre y cuando las cajas de texto no estén vacías,
    //se permitirá añadir a este contacto. Por ahora no se establece ningún tipo de
    //restricción a la hora de añadirlos
    @Override
    public void onClick(View v) {
        if(v == add){
            try {
                if(name.getText().toString().equals("") || phone.getText().toString().equals("") || mail.getText().toString().equals("")){
                    Toast.makeText(this, "Debes rellenar toda la información sobre el contacto", Toast.LENGTH_SHORT).show();
                }
                else {
                    anadirContacto();
                }
            }
            catch (Exception e){
                Toast.makeText(this, "No se ha podido añadir el contacto", Toast.LENGTH_SHORT).show();
            }
        }
        if(v == cancel){
            finish();
        }
    }

    //En caso de que se vaya a añadir un contacto que haya resutlado ser correcto,
    //se abre el fichero en modo de adición desde el final del fichero, y se
    //añade el nuevo contacto a la lista. Si el fichero no existe, se crea uno nuevo
    public void anadirContacto() {
        try {
            File fichero = new File(this.getApplicationContext().getFilesDir(),"contactos.txt");
            if(!fichero.exists())
                fichero.createNewFile();
            BufferedWriter escritor = new BufferedWriter(new FileWriter(fichero,true));

            escritor.write(name.getText().toString() + " /// " + phone.getText().toString() + " /// " + mail.getText().toString());
            escritor.newLine();
            escritor.close();

            Toast.makeText(this, "Contacto añadido con éxito", Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            //
        }
    }
}
