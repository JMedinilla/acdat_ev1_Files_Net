package com.example.jmedinilla.relacion_2_jmedinilla;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class Calendario_EJ3 extends AppCompatActivity implements View.OnClickListener {

    //Elementos del layout
    EditText tbxDiasPeriodo;
    Button btnCalculo;
    DatePicker datePick;
    TextView txtDias;
    TextView txtHoy;

    //Objetos que nos permitirán obtener fechas reales
    Calendar fecha;
    Calendar c;

    //Días fértiles, separados en variables cada uno de ellos
    String diaFertilCentral;
    String diaFertilUno;
    String diaFertilDos;
    String diaFertilCuatro;

    //Se almacenará en una variable el día de hoy para poder hacer comparaciones, así
    //como la variable que indicará si hoy es, o no, un día fértil
    String diaHoy;
    String hoyResultado;

    //A la hora de escribir en el fichero, estas variables contendrán
    //las fechas de inicio y fin del lapso de tiempo que corresponde al
    //periodo fértil, en lugar de escribir los cuatro días
    String diaUnoMes;
    String diaUnoAnio;
    String diaCuatroMes;
    String diaCuatroAnio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario__ej3);
        
        tbxDiasPeriodo = (EditText)findViewById(R.id.tbxDiasPeriodo_ej3);
        btnCalculo = (Button)findViewById(R.id.btnCalculo_ej3);
        btnCalculo.setOnClickListener(this);
        datePick = (DatePicker)findViewById(R.id.datePick_ej3);
        txtDias = (TextView)findViewById(R.id.txtDias_ej3);
        txtHoy = (TextView)findViewById(R.id.txtHoy_ej3);
        
        fecha = Calendar.getInstance();
        c = Calendar.getInstance();
        
    }

    @Override
    public void onClick(View v) {
        //Siempre que la caja de texto que indica la media de duración del periodo no esté vacía,
        //se comenzará con las comprobaciones y distintas asignaciones a las variables que
        //necesitamos para indicar cuándo será el periodo que buscamos
        if (!tbxDiasPeriodo.getText().toString().equals("")) {
            //En este caso establecemos dos condiciones para que el día seleccionado no sea incorrecto
            //
            //En primer lugar, se tiene que cumplir que tanto el día, como el mes y el año sean
            //inferiores a los actuales. Esto lo hacemos ya que si comprobamos únicamente por el
            //mes y el año, el usuario podría seleccionar un dia posterior al del día actual
            //dentro del mismo mes en el que está
            //
            //Por otro lado, si esto resulta ser falso, quiere decir, entre otras cosas, que
            //el día introducido podría ser mayor al actual, por lo que hacemos otra comprobación,
            //buscando que el mes sea como máximo, el anterior al actual
            //
            //
            //En caso de que el año sea mayor al actual, ninguna de las dos condiciones será cierta
            //En caso de que el mes sea mayor al actual, ninguna de las dos condiciones será cierta
            //En caso de que el día sea mayor al actual, se pasará a comprobar que el mes y el año son
            //ambos anteriores a los actuales
            if ((datePick.getYear() <= c.get(Calendar.YEAR)
                    && datePick.getMonth() <= c.get(Calendar.MONTH)
                    && datePick.getDayOfMonth() <= c.get(Calendar.DAY_OF_MONTH))
                    ||
                    (datePick.getYear() <= c.get(Calendar.YEAR)
                    && datePick.getMonth() < c.get(Calendar.MONTH))) {

                //Establecemos un objeto de tipo calendario con la fecha introducida en el DatePicker
                //y le sumamos la mitad de la cantidad introducida en la duración media del periodo
                fecha.set(datePick.getYear(), datePick.getMonth(), datePick.getDayOfMonth());
                fecha.add(Calendar.DAY_OF_MONTH, Integer.parseInt(tbxDiasPeriodo.getText().toString()) / 2);
                //El día que salga como resultado, será el día de mayor fertilidad, al cual hay
                //que extraer los dos días anteriores y el siguiente a este
                diaFertilCentral = Integer.toString(fecha.get(Calendar.DAY_OF_MONTH));

                //A la fecha que teníamos, le restamos 2 días y lo asignamos al primer día del
                //periodo de fertilidad
                fecha.add(Calendar.DAY_OF_MONTH, -2);
                diaFertilUno = Integer.toString(fecha.get(Calendar.DAY_OF_MONTH));
                diaUnoMes = Integer.toString(fecha.get(Calendar.MONTH));
                diaUnoAnio = Integer.toString(fecha.get(Calendar.YEAR));

                //Al sumar un día más, obtendremos el día anterior al principal, el cual solo
                //necesitamos para mostrar su número ante el usuario
                fecha.add(Calendar.DAY_OF_MONTH, 1);
                diaFertilDos = Integer.toString(fecha.get(Calendar.DAY_OF_MONTH));

                //A este día le sumamos 2 y obtendremos el día siguiente al día principal del
                //periodo, por lo cual, al ser el último día de este, también almacenaremos sus
                //datos completos para que aparezca en el fichero
                fecha.add(Calendar.DAY_OF_MONTH, 2);
                diaFertilCuatro = Integer.toString(fecha.get(Calendar.DAY_OF_MONTH));
                diaCuatroMes = Integer.toString(fecha.get(Calendar.MONTH));
                diaCuatroAnio = Integer.toString(fecha.get(Calendar.YEAR));

                //Ahora obtenemos el día de hoy
                diaHoy = Integer.toString(c.get(Calendar.DAY_OF_MONTH));

                //Guardamos en una lista los cuatro días que pertenecen al periodo
                //y los mostramos mediante un TextView
                ArrayList<String> dias = new ArrayList<String>();
                dias.add(diaFertilUno);
                dias.add(diaFertilDos);
                dias.add(diaFertilCentral);
                dias.add(diaFertilCuatro);
                txtDias.setText(dias.get(0) + ", " + dias.get(1) + ", " + dias.get(2)+ ", " + dias.get(3));

                //Habrá un TextView más para indicar di hoy es o no un día fértil, en base a que la
                //lista de días fértiles contenda o no el día de hoy.
                if (dias.contains(diaHoy)) {
                    txtHoy.setText("Hoy es un día fértil");
                }
                else {
                    txtHoy.setText("Hoy no es un día fértil");
                }
                hoyResultado = txtHoy.getText().toString();

                //Hechas todas las comprobaciones, guardamos la información en un fichero.
                guardarEnFichero();
            }
            else {
                Toast.makeText(getApplicationContext(), "El último día del ciclo anterior no puede ser superior al día de hoy", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "No has introducido la media de días que dura el ciclo", Toast.LENGTH_SHORT).show();
        }
    }

    private void guardarEnFichero() {
        Memoria memoria = new Memoria(getApplicationContext());
        String contenido = "";

        //En el contenido de la cadena que se va a guardar en memoria externa, se almacenan
        //el primer y último día que corresponden al periodo en el que dura la mayor fertilidad.
        //Para eso ya se han obtenido previamente el primer y cuarto día fértil, así como
        //el mes y el año de cada uno de ellos, por si acaso tienen lugar en meses y años distintos
        contenido += "Primer día fértil: " + diaFertilUno + "-" + diaUnoMes + "-" + diaUnoAnio;
        contenido += "\n";
        contenido += "Último día fértil: " + diaFertilCuatro + "-" + diaCuatroMes + "-" + diaCuatroAnio;
        contenido += "\n";
        contenido += "" + hoyResultado;

        Toast.makeText(getApplicationContext(), contenido, Toast.LENGTH_LONG).show();

        if (memoria.disponibleEscritura()) {
            if (memoria.escribirExterna("fertilidad.txt", contenido, false, "utf-8")) {
                Toast.makeText(getApplicationContext(), "Fichero escrito correctamente", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "No se ha podido escribir el fichero", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "Error al acceder a la tarjeta de memoria", Toast.LENGTH_SHORT).show();
        }
    }
}
