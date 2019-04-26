package com.example.PETBook;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.PETBook.Controllers.AsyncResult;
import com.example.PETBook.Models.PetModel;
import com.example.pantallafirstview.R;

import org.json.JSONObject;


public class EditPet extends AppCompatActivity implements AsyncResult {

    private PetModel petModel;
    private EditText nombre;
    private Spinner sexo;
    private Spinner especie;
    private EditText raza;
    private EditText edad;
    private EditText color;
    private EditText descripcion;
    private ImageButton acceptEditPet;
    private ImageButton cancelEditPet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
        Bundle eventEdit = getIntent().getExtras();

        nombre = (EditText) findViewById(R.id.editNamePet);
        sexo = (Spinner) findViewById(R.id.editSexPet);
        especie = (Spinner) findViewById(R.id.editTypePet);
        raza = (EditText) findViewById(R.id.editRacePet);
        edad = (EditText) findViewById(R.id.editEdadPet);
        color = (EditText) findViewById(R.id.editColorPet);
        descripcion = (EditText) findViewById(R.id.editDescriptionPet);


        acceptEditPet = (ImageButton) findViewById(R.id.imageButtonAcceptEdit);
        acceptEditPet.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog.Builder edit = new AlertDialog.Builder(EditPet.this);
                edit.setMessage("You want to confirm the data?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                editPet();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog errorE = edit.create();
                errorE.setTitle("Modify Pet");
                errorE.show();
            }
        });

        cancelEditPet = (ImageButton) findViewById(R.id.imageButtonCancelEdit);
        cancelEditPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder noedit = new AlertDialog.Builder(EditPet.this);
                noedit.setMessage("Do you want to cancel the changes?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(EditPet.this,EventInfo.class);
                                intent.putExtra("pet",petModel);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog errorE = noedit.create();
                errorE.setTitle("Cancel modify pet");
                errorE.show();
            }
        });


        recibirDatos();
    }


    private void recibirDatos(){
        Bundle datosRecibidos = this.getIntent().getExtras();
        if(datosRecibidos != null) {
            petModel = (PetModel) datosRecibidos.getSerializable("pet");
            System.out.print("La ventana recibe los datos ya que el bundle no es vacio\n");
            nombre.setText(petModel.getNombre());
            //sexo.setSelection;
            raza.setText(petModel.getRaza());
            edad.setText(petModel.getEdad());
            color.setText(petModel.getColor());
            descripcion.setText(petModel.getDescripcion());
        }
    }


    private void editPet(){
        SingletonUsuario su = SingletonUsuario.getInstance();
        String name = nombre.getText().toString();
        String race = raza.getText().toString();
        String age = edad.getText().toString();
        String col = color.getText().toString();
        String desc = descripcion.getText().toString();

            JSONObject jsonToSend = new JSONObject();
            try{
                jsonToSend.accumulate("color", col);
                jsonToSend.accumulate("descripcion", desc);
                jsonToSend.accumulate("edad", Integer.parseInt(age));
                jsonToSend.accumulate("email", su.getEmail());
                jsonToSend.accumulate("nombre", name);
                jsonToSend.accumulate("raza", race);
                System.out.print(jsonToSend);
            } catch (Exception e){
                e.printStackTrace();
            }

            Conexion con = new Conexion(EditPet.this);
            con.execute("http://10.4.41.146:9999/ServerRESTAPI/UpdatePet/", "PUT", jsonToSend.toString());
        }



    @Override
    public void OnprocessFinish(JSONObject json) {

        try{
            if (json.getInt("code") == 200) {
                System.out.print(json.getInt("code")+ "Correcto+++++++++++++++++++++++++++\n");
                Toast.makeText(this, "Pet modificado correctamente", Toast.LENGTH_SHORT).show();
                petModel.setNombre(nombre.getText().toString());
                petModel.setRaza(raza.getText().toString());
                petModel.setEdad(Integer.parseInt(edad.getText().toString()));
                petModel.setColor(color.getText().toString());
                petModel.setDescripcion(descripcion.getText().toString());

                Intent intent = new Intent(EditPet.this, PetInfo.class);
                intent.putExtra("pet",petModel);
                startActivity(intent);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}