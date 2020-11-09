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
    EditText firstNameRegister, lastNameRegister,emailRegister,passwordRegister, jefaturaRegister, fecha, cargoRegister;
    ProgressBar progressRegister;
    Spinner myspinner, spinnerjefatura;
    DatePickerDialog.OnDateSetListener setListener;
    Calendar c;
    ArrayList idusuario, nombreusuario, apellidousuario, jefatura;
    public int ultimo;
    public String jefaturafinal = "";
    public String edad = "";

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
        idusuario = new ArrayList();
        nombreusuario = new ArrayList();
        apellidousuario = new ArrayList();
        jefatura = new ArrayList();
        
        c = Calendar.getInstance();
        final int year = c.get(Calendar.YEAR);
        final int month = c.get(Calendar.MONTH);
        final int day = c.get(Calendar.DAY_OF_MONTH);
        getSupportActionBar().hide();

        //CALCULAR EDAD
        String fechanacimiento = fecha.getText().toString();
        Date datenacimiento = stringToDate(fechanacimiento, "yyyy-MM-dd");
        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = sdf.format(date);
        //Date asd = dateString - datenacimiento;
        //edad = asd.toString();

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
                },year, month, day);
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
                if(empleador.equals(elemento)){
                    ultimo = 1;
                }else{
                    ultimo = 2;
                }
                //Toast.makeText(Register.this, "Seleccionaste: " + elemento, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(Register.this, "No ha seleccionado una opción", Toast.LENGTH_SHORT).show();
            }
        });

        //SPINNER JEFATURA
        /*
        ArrayList<String> elementos2 = new ArrayList<>();
        for(int k = 0; k < idusuario.size(); k++){
            if(jefatura.get(k) != "0"){
                elementos2.add(nombreusuario.get(k) + " " + apellidousuario.get(k));
                jefaturafinal = idusuario.get(k).toString();
            }
        }
        ArrayAdapter adp2 = new ArrayAdapter(Register.this, android.R.layout.simple_spinner_dropdown_item, elementos2);
        myspinner.setAdapter(adp2);
        myspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String elemento2 = (String) myspinner.getAdapter().getItem(position);
                }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(Register.this, "No ha seleccionado una opción", Toast.LENGTH_SHORT).show();
            }
        });*/

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
        String elemento = ultimo + "";
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

        ejecutarservicio("https://yotrabajoconpecs.ddns.net/saveUsuario.php", elemento, password, firstName, lastName, edad, email, jefaturafinal, cargo);
        //ejecutarservicio(URL, tipo, password, firstName, lastName, edad, email, jefaturafinal, cargo);
    }

    private void ejecutarservicio(String URL, String tipo, String contraseña, String nombre, String apellidos, String fecha, String correo, String jefe, String cargo){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getContext(), "OPERACION EXITOSA", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String,String>();
                parametros.put("id_usuario", "");
                parametros.put("tipo", tipo);
                parametros.put("contraseña", contraseña);
                parametros.put("nombre", nombre);
                parametros.put("apellidos", apellidos);
                parametros.put("fecha", fecha);
                parametros.put("correo", correo);
                parametros.put("jefatura", jefe);
                parametros.put("cargo", cargo);
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void descargarDatos(){
        idusuario.clear();
        nombreusuario.clear();
        apellidousuario.clear();
        jefatura.clear();
        final ProgressDialog progressDialog = new ProgressDialog(this);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("https://yotrabajoconpecs.ddns.net/query_tarea.php", new AsyncHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200){
                    progressDialog.dismiss();
                    try{
                        JSONArray jsonarray = new JSONArray(new String(responseBody));
                        for(int i = 0; i < jsonarray.length(); i++){
                            idusuario.add(jsonarray.getJSONObject(i).getString("id_usuario"));
                            nombreusuario.add(jsonarray.getJSONObject(i).getString("nombre_usuario"));
                            apellidousuario.add(jsonarray.getJSONObject(i).getString("apellido_usuario"));
                            jefatura.add(jsonarray.getJSONObject(i).getString("jefatura"));
                        }
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Context context = Register.this;
                CharSequence text = "Conexión fallida";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });
    }

    private Date stringToDate(String aDate,String aFormat) {
        if(aDate==null) return null;
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat simpledateformat = new SimpleDateFormat(aFormat);
        Date stringDate = simpledateformat.parse(aDate, pos);
        return stringDate;

    }
}
