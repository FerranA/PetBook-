package com.example.PETBook;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.PETBook.Controllers.AsyncResult;
import com.example.PETBook.Models.EventModel;
import com.example.pantallafirstview.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EventInfo extends AppCompatActivity implements AsyncResult {

    private String Titulo;
    private String Descripcion;
    private Integer Localizacion;
    private String[] Fecha;
    private ArrayList<String> Miembros;
    private String Creador;
    private TextView txtTitle;
    private TextView txtDescription;
    private TextView txtLoc;
    private TextView txtFecha;
    private TextView txtHora;
    private TextView txtMiembros;
    private TextView txtCreador;
    private ImageButton editButton;
    private ImageButton deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);

        txtTitle = (TextView) findViewById(R.id.textTitulo);
        txtDescription = (TextView) findViewById(R.id.textDesc);
        txtLoc = (TextView) findViewById(R.id.textLoc);
        txtFecha = (TextView) findViewById(R.id.textFecha);
        txtHora = (TextView) findViewById(R.id.textHora);
        txtMiembros = (TextView) findViewById(R.id.textNumPart);
        txtCreador = (TextView) findViewById(R.id.textCreador);


        /*editButton = (Button) findViewById(R.id.buttonEditEvent);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editEvent();
            }
        });*/


        deleteButton = (ImageButton) findViewById(R.id.imageButtonDelete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder error = new AlertDialog.Builder(EventInfo.this);
                error.setMessage("Esta seguro que quiere eliminar el evento?")
                        .setCancelable(false)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteEvent();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog errorE = error.create();
                errorE.setTitle("Eliminar evento");
                errorE.show();
            }
        });


        recibirDatos();
    }

    private void recibirDatos(){
        Bundle datosRecibidos = this.getIntent().getExtras();
        if(datosRecibidos != null) {
            EventModel e = (EventModel) datosRecibidos.getSerializable("event");
            Titulo = e.getTitulo();
            Descripcion = e.getDescripcion();
            Localizacion = e.getLocalizacion();
            Fecha = e.getFecha().split(" ");
            Miembros = e.getMiembros();
            Creador = e.getCreador();
            System.out.print("La ventana recibe los datos ya que el bundle no es vacio\n");
            System.out.print(Titulo + "\n");
            System.out.print(Descripcion + "\n");
            System.out.print(Localizacion + "\n");
            System.out.print(Fecha[0] + "\n");
            System.out.print(Fecha[1] + "\n");
            txtTitle.setText(Titulo);
            txtDescription.setText(Descripcion);
            txtLoc.setText(String.valueOf(Localizacion));
            txtFecha.setText(Fecha[0]);
            txtHora.setText(Fecha[1]);
            txtMiembros.setText(Miembros.size() + " usuarios participarán en este evento");
            txtCreador.setText("Creado por: " + Creador);
        }
    }

    private void deleteEvent(){
        SingletonUsuario su = SingletonUsuario.getInstance();
        String localizacion = txtLoc.getText().toString();
        String titulo = txtTitle.getText().toString();
        String descripcion = txtDescription.getText().toString();
        Boolean publico = true;
        String Fecha = txtFecha.getText().toString() + "T" + txtHora.getText().toString() + ":00.000Z";
        String user = su.getEmail();

        JSONObject jsonToSend = new JSONObject();
        try {
            jsonToSend.accumulate("coordenadas", Integer.parseInt(localizacion)/10); //cambiar cuando se implemente MAPS
            jsonToSend.accumulate("descripcion", descripcion);
            jsonToSend.accumulate("fecha", Fecha); //2019-05-24T19:13:00.000Z formato fecha
            jsonToSend.accumulate("publico", publico);
            jsonToSend.accumulate("radio", 0); //No se trata el valor por Google Maps
            jsonToSend.accumulate("titulo", titulo);
            jsonToSend.accumulate("userEmail", user);
            System.out.print(jsonToSend);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Conexion con = new Conexion(this);
        con.execute("http://10.4.41.146:9999/ServerRESTAPI/DeleteEvent/", "DELETE", jsonToSend.toString());
    }

    @Override
    public void OnprocessFinish(JSONObject json) {

        try {
            if (json.getInt("code") == 200) {
                System.out.print(json.getInt("code")+ "Correcto+++++++++++++++++++++++++++\n");
                Toast.makeText(this, "Evento correctamente eliminado", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("fragment", "events");
                startActivity(intent);
            } else {
                System.out.print(json.getInt("code")+ "Mal+++++++++++++++++++++++++++\n");
                Toast.makeText(this, "El usuario no existe", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}