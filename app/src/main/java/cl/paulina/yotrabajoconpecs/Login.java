package cl.paulina.yotrabajoconpecs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class Login extends AppCompatActivity {
    EditText emailLogin,passwordLogin;
    ProgressBar progressLogin;
    public LinearLayout layout;
    private ArrayList correo, clave, nombre, apellidos, tipousuario;
    private RadioButton RBsesion;
    private static final String IP_TOKEN = "https://yotrabajoconpecs.ddns.net/Token_INSERTandUPDATE.php";
    Bundle datos;
    public String emailArray;
    public String emailemail = "";
    public String nombresnombres = "";
    public String nombresinespacios = "";
    private boolean isActivateRadioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        if(Preferences.obtenerPreferenceBoolean(this, Preferences.PREFERENCES_ESTADO_BUTTON_SESION)){
            Intent i = new Intent(getApplicationContext(), menu_lateral.class);
            startActivity(i);
        }
        emailLogin = findViewById(R.id.userLogin);
        passwordLogin = findViewById(R.id.passwordLogin);
        progressLogin = findViewById(R.id.progressLogin);
        RBsesion = findViewById(R.id.RBsesion);
        getSupportActionBar().hide();
        correo = new ArrayList();
        clave = new ArrayList();
        nombre = new ArrayList();
        apellidos = new ArrayList();
        tipousuario = new ArrayList();
        datos = new Bundle();
        correo.clear();
        clave.clear();
        nombre.clear();
        apellidos.clear();
        tipousuario.clear();

        isActivateRadioButton = RBsesion.isChecked(); //Desactivado
        RBsesion.setOnClickListener(new View.OnClickListener() {
            //Activado
            @Override
            public void onClick(View v) {
                if(isActivateRadioButton){
                    RBsesion.setChecked(false);
                }
                isActivateRadioButton = RBsesion.isChecked();
            }
        });

        final ProgressDialog progressDialog = new ProgressDialog(getApplication());
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("https://yotrabajoconpecs.ddns.net/usuario.php", new AsyncHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200){
                    progressDialog.dismiss();
                    try{
                        JSONArray jsonarray = new JSONArray(new String(responseBody));
                        for(int i = 0; i < jsonarray.length(); i++){
                            correo.add(jsonarray.getJSONObject(i).getString("correo"));
                            clave.add(jsonarray.getJSONObject(i).getString("clave"));
                            nombre.add(jsonarray.getJSONObject(i).getString("nombre_usuario"));
                            apellidos.add(jsonarray.getJSONObject(i).getString("apellido_usuario"));
                            tipousuario.add(jsonarray.getJSONObject(i).getString("tipousuario_tipo"));
                        }
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Context context = getApplicationContext();
                CharSequence text = "Conexión fallida";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });
    }

    public void iniciarSesion(View view) {
        String password = passwordLogin.getText().toString().trim();
        String email = emailLogin.getText().toString().trim();
        if(TextUtils.isEmpty(email)) {
            emailLogin.setError("Debe ingresar correo");
            return;
        }
        if(TextUtils.isEmpty(password)){
            passwordLogin.setError("Debe ingresar una contraseña");
            return;
        }
        progressLogin.setVisibility(View.VISIBLE);

        for(int i = 0; i < correo.size(); i++) {
            emailArray = correo.get(i).toString();
            String tipo = tipousuario.get(i).toString();
            String nombres = nombre.get(i).toString() + " " + apellidos.get(i).toString();
            String apellidosinespacio = apellidos.get(i).toString();
            String nombresdos = nombre.get(i).toString() + apellidosinespacio.replace(" ", "");
            if (emailArray.equals(email)) {
                emailemail = emailArray;
                        //Preferences.obtenerPreferenceString(this, Preferences.PREFERENCE_USUARIO_LOGIN);
                nombresnombres = nombres;
                nombresinespacios = nombresdos;
                ejecutarservicio("https://yotrabajoconpecs.ddns.net/loginreciclado.php", nombres, tipo, emailArray);
            }
        }
        if(emailemail == ""){
            progressLogin.setVisibility(View.INVISIBLE);
            Toast.makeText(Login.this, "El correo no esta registrado", Toast.LENGTH_SHORT).show();
        }else{
            String Token = FirebaseInstanceId.getInstance().getToken();;
            if(Token != null){
                //Toast.makeText(Login.this, "El token no es nulo", Toast.LENGTH_SHORT).show();
                SubirToken(Token);
            }else{
                Toast.makeText(Login.this, "El token es nulo", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void SubirToken(String Token){
        //Toast.makeText(Login.this, "entre a SubirToken", Toast.LENGTH_SHORT).show();
        HashMap<String,String> hashMapToken = new HashMap<>();
        hashMapToken.put("id", emailemail);
        hashMapToken.put("nombre", nombresnombres);
        hashMapToken.put("token", FirebaseInstanceId.getInstance().getToken());
        //Toast.makeText(Login.this, emailemail + FirebaseInstanceId.getInstance().getToken(), Toast.LENGTH_SHORT).show();

        JsonObjectRequest solicitud = new JsonObjectRequest(Request.Method.POST, IP_TOKEN, new JSONObject(hashMapToken), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Preferences.savePreferenceBoolean(Login.this, RBsesion.isChecked(), Preferences.PREFERENCES_ESTADO_BUTTON_SESION);
                Preferences.savePreferenceString(Login.this, nombresinespacios, Preferences.PREFERENCE_USUARIO_LOGIN);
                try {
                    Toast.makeText(Login.this,"Se ha iniciado sesión: " + response.getString("resultado"),Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {}
                Intent i = new Intent(getApplicationContext(), menu_lateral.class);
                startActivity(i);
                finish();
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Toast.makeText(Login.this,"El token no se pudo subir a la BD",Toast.LENGTH_SHORT).show();
                progressLogin.setVisibility(View.INVISIBLE);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(solicitud);
    }

    public void iniciarRegistro(View view) {
        startActivity(new Intent(getApplicationContext(), Register.class));
    }

    private void ejecutarservicio(String URL, String nombres, String tipo, String correo){
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
                parametros.put("idlogin", "NULL");
                parametros.put("nombres", nombres);
                parametros.put("tipo", tipo);
                parametros.put("correo", correo);
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

}
