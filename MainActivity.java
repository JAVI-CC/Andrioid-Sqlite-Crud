package com.example.javi.javi_sqlite;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private EditText et1,et2,et3,et4;
    private  ListView lv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv1 =(ListView)findViewById(R.id.lv1);
        et1=(EditText)findViewById(R.id.et1);
        et2=(EditText)findViewById(R.id.et2);
        et3=(EditText)findViewById(R.id.et3);
        et4=(EditText)findViewById(R.id.et4);
    }

    public void alta(View v) {
        SqliteFutbol admin = new SqliteFutbol(this, "jugadores_futbol", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String dni = et1.getText().toString();
        String nom = et2.getText().toString();
        String equip = et3.getText().toString();
        String contracte = et4.getText().toString();
        ContentValues registro = new ContentValues();
        registro.put("dni", dni);
        registro.put("nom", nom);
        registro.put("equip", equip);
        registro.put("contracte", contracte);
        bd.insert("jugadores_futbol", null, registro);
        bd.close();
        et1.setText("");
        et2.setText("");
        et3.setText("");
        et4.setText("");
        Toast.makeText(this, "Se he insertado el jugador",
                Toast.LENGTH_SHORT).show();
    }

    public void consultapordni(View v) {
        SqliteFutbol admin = new SqliteFutbol(this, "jugadores_futbol", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String clave_dni = et1.getText().toString();
        Cursor fila = bd.rawQuery(
                "select nom,equip,contracte from jugadores_futbol where dni='" + clave_dni + "'", null);
        if (fila.moveToFirst()) {
            et2.setText(fila.getString(0));
            et3.setText(fila.getString(1));
            et4.setText(fila.getString(2));
        } else {
            Toast.makeText(this, "No existe un jugador con dicho dni",
                    Toast.LENGTH_SHORT).show();
        }
        bd.close();
    }

    public void eliminarpordni(View v) {

        AlertDialog.Builder myBuild = new AlertDialog.Builder(this);
        myBuild.setMessage("Eliminar el registro");
        myBuild.setTitle("¿ Esta seguro de eliminar el registro ?");
        myBuild.setPositiveButton("Si", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
            //Eliminar el jugador por DNI
            eliminarpordniyes();

            }
        });

        myBuild.setNegativeButton("No", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            }

            });

        AlertDialog dialog = myBuild.create();
        dialog.show();


    }


    public void eliminarpordniyes() {
        SqliteFutbol admin = new SqliteFutbol(this, "jugadores_futbol", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String clave_dni= et1.getText().toString();
        int cant = bd.delete("jugadores_futbol", "dni='" + clave_dni + "'", null);
        bd.close();
        et1.setText("");
        et2.setText("");
        et3.setText("");
        et4.setText("");
        if (cant == 1) {
            Toast.makeText(this, "Se borró el jugador con dicho dni",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No existe un jugador con dicho dni",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void modificarpordni(View v) {
        SqliteFutbol admin = new SqliteFutbol(this,
                "jugadores_futbol", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String clave_dni = et1.getText().toString();
        String nom = et2.getText().toString();
        String equip = et3.getText().toString();
        String contracte = et4.getText().toString();
        ContentValues registro = new ContentValues();
        registro.put("dni", clave_dni);
        registro.put("nom", nom);
        registro.put("equip", equip);
        registro.put("contracte", contracte);
        int cant = bd.update("jugadores_futbol", registro, "dni='" + clave_dni + "'", null);
        bd.close();
        if (cant == 1) {
            Toast.makeText(this, "se modificaron los datos", Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast.makeText(this, "no existe un jugador con el dni ingresado",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void listarjugadores(View v) {
        SqliteFutbol admin = new SqliteFutbol(this, "jugadores_futbol", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String clave_equip = et3.getText().toString();
        Cursor fila = bd.rawQuery(
                "select dni,nom,contracte from jugadores_futbol where equip='" + clave_equip + "' order by contracte DESC" , null);
        if (fila.moveToFirst()) {
            ArrayList <String> registros = new ArrayList<>();
            if(fila.moveToFirst()){
                do{
                    registros.add(fila.getString(0) + " - " + fila.getString(1) + " - " + fila.getString(2));
                }while(fila.moveToNext());
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, registros);
                lv1.setAdapter(adapter);
            }
            } else {
            Toast.makeText(this, "No existe un jugador con dicho nombre de equipo",
                    Toast.LENGTH_SHORT).show();
        }
        bd.close();

    }

}
