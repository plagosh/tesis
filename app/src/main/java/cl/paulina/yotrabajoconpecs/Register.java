package cl.paulina.yotrabajoconpecs;
import android.app.AppComponentFactory;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class Register extends AppCompatActivity {
    private EditText firstNameRegister, lastNameRegister,emailRegister,passwordRegister, jefaturaRegister, fecha, cargoRegister;
    ProgressBar progressRegister;
    Spinner myspinner, spinnerjefatura;
    DatePickerDialog.OnDateSetListener setListener;
    private Calendar c;
    private TextView mijefatura;
    private ArrayList idusuario, nombreusuario, apellidousuario, jefatura;
    private ArrayList elementos2;
    private ArrayList elementos3;
    public int tipousuario;
    public String jefaturafinal = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        firstNameRegister = findViewById(R.id.firstNameRegister);
        lastNameRegister = findViewById(R.id.lastNameRegister);
        emailRegister = findViewById(R.id.emailRegister);
        passwordRegister = findViewById(R.id.passwordRegister);
        progressRegister = findViewById(R.id.progressRegister);
        spinnerjefatura = findViewById(R.id.spinnerjefatura);
        fecha = findViewById(R.id.fechanacimiento);
        cargoRegister = findViewById(R.id.cargoRegister);
        myspinner = findViewById(R.id.spinner);
        mijefatura = findViewById(R.id.nombrejefatura);
        idusuario = new ArrayList();
        nombreusuario = new ArrayList();
        apellidousuario = new ArrayList();
        jefatura = new ArrayList();
        elementos2 = new ArrayList();
        elementos3 = new ArrayList();

        c = Calendar.getInstance();
        final int year = c.get(Calendar.YEAR);
        final int month = c.get(Calendar.MONTH);
        final int day = c.get(Calendar.DAY_OF_MONTH);
        getSupportActionBar().hide();

        //ELEGIR UN DIA DEL CALENDARIO PARA LA FECHA DE NACIMIENTO
        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = year + "-" + month + "-" + day;
                fecha.setText(date);
            }
        };

        //GUARDAR LA FECHA EN EL EDITTEXT
        fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(Register.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        String date = year + "-" + month + "-" + day;
                        fecha.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        //SPINNER EMPLEADOR, EMPLEADO
        ArrayList<String> elementos = new ArrayList<>();
        elementos.add("Empleador");
        elementos.add("Empleado");
        ArrayAdapter adp = new ArrayAdapter(Register.this, android.R.layout.simple_spinner_dropdown_item, elementos);
        myspinner.setAdapter(adp);
        myspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String elemento = (String) myspinner.getAdapter().getItem(position);
                String empleador = "empleador";
                if (empleador.equals(elemento)) {
                    tipousuario = 1;
                } else {
                    tipousuario = 2;
                    mijefatura.setVisibility(View.VISIBLE);
                    spinnerjefatura.setVisibility(View.VISIBLE);
                }
                //Toast.makeText(Register.this, "Seleccionaste: " + elemento, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(Register.this, "No ha seleccionado una opción", Toast.LENGTH_SHORT).show();
            }
        });

        //HACER LA DESCARGA DE LOS USUARIOS
        descargarDatos();
    }

    public void volverLogin(View view) {
        startActivity(new Intent(getApplicationContext(), Login.class));
    }

    public void registrarUsuario(View view) {
        c = Calendar.getInstance();
        final int year = c.get(Calendar.YEAR);
        final int month = c.get(Calendar.MONTH);
        final int day = c.get(Calendar.DAY_OF_MONTH);
        String date = year + "-" + month + "-" + day;
        String firstName = firstNameRegister.getText().toString();
        String lastName = lastNameRegister.getText().toString();
        String email = emailRegister.getText().toString();
        String password = passwordRegister.getText().toString();
        String elemento = tipousuario + "";
        String cargo = cargoRegister.getText().toString();

        if (TextUtils.isEmpty(email)) {
            emailRegister.setError("Ingrese un email.");
            return;
        }
        if (TextUtils.isEmpty(firstName)){
            firstNameRegister.setError("Ingrese su Nombre.");
            return;
        }

        if (TextUtils.isEmpty(lastName)){
            lastNameRegister.setError("Ingrese su Apellido.");
            return;
        }

        if (password.length() < 8 || TextUtils.isEmpty(password)){
            passwordRegister.setError("Ingrese una contraseña de al menos 8 caracteres.");
            return;
        }
        progressRegister.setVisibility(View.VISIBLE);

        ejecutarservicio("https://yotrabajoconpecs.ddns.net/saveUsuario.php?tipousuario_tipo=" + elemento + "&clave=" + password + "&nombre_usuario=" + firstName + "&apellido_usuario="+ lastName + "&edad=" + date + "&correo=" + email + "&jefatura=" + jefaturafinal + "&cargo=" + cargo);
        //ejecutarservicio(URL, tipo, password, firstName, lastName, edad, email, jefaturafinal, cargo);
    }

    private void ejecutarservicio(String URL){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(URL, new AsyncHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200){
                    Toast.makeText(Register.this, "Se ha guardado el usuario con éxito", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), Login.class);
                    startActivity(i);
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(Register.this, "No se ha podido guardar el usuario", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void descargarDatos(){
        idusuario.clear();
        nombreusuario.clear();
        apellidousuario.clear();
        jefatura.clear();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("https://yotrabajoconpecs.ddns.net/query_usuario.php", new AsyncHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200){
                    try{
                        JSONArray jsonarray = new JSONArray(new String(responseBody));
                        for(int i = 0; i < jsonarray.length(); i++){
                            idusuario.add(jsonarray.getJSONObject(i).getString("id_usuario"));
                            nombreusuario.add(jsonarray.getJSONObject(i).getString("nombre_usuario"));
                            apellidousuario.add(jsonarray.getJSONObject(i).getString("apellido_usuario"));
                            jefatura.add(jsonarray.getJSONObject(i).getString("jefatura"));
                            if(jefatura.get(i).toString().equals("0")){
                                elementos2.add(nombreusuario.get(i).toString() + " " + apellidousuario.get(i).toString());
                                elementos3.add(idusuario.get(i).toString());
                            }
                        }
                        //SPINNER JEFATURA
                        ArrayAdapter adp2 = new ArrayAdapter(Register.this, android.R.layout.simple_spinner_dropdown_item, elementos2);
                        spinnerjefatura.setAdapter(adp2);
                        spinnerjefatura.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                jefaturafinal = elementos3.get(position).toString();
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                Toast.makeText(Register.this, "No ha seleccionado una opción", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(Register.this, "Conexión fallida", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
