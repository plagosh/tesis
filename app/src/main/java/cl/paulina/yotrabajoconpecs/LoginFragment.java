package cl.paulina.yotrabajoconpecs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class LoginFragment extends Fragment{
    EditText emailLogin,passwordLogin;
    ProgressBar progressLogin;
    public LinearLayout layout;
    private ArrayList correo, clave, nombre, apellidos, tipousuario;
    Bundle datos;
    Button login;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.login, container, false);
        emailLogin = vista.findViewById(R.id.userLogin);
        passwordLogin = vista.findViewById(R.id.passwordLogin);
        progressLogin = vista.findViewById(R.id.progressLogin);
        login = vista.findViewById(R.id.button_login);
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
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
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
                Context context = getContext();
                CharSequence text = "Conexión fallida";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    String emailArray = correo.get(i).toString();
                    String tipo = tipousuario.get(i).toString();
                    String nombres = nombre.get(i).toString() + " " + apellidos.get(i).toString();

                    if (emailArray.equals(email)) {
                        datos.putString("nombres", nombres);
                        datos.putString("tipo_usuario", tipo);
                        Log.e("Agregar", "datos enviados");
                        Toast.makeText(getContext(), "pase el nombre: " + nombres + "junto con el tipo " + tipo, Toast.LENGTH_SHORT).show();
                        if (tipo == "1") {
                            Toast.makeText(getContext(), "Sesión Iniciada", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getContext(), menu_lateral.class));
                        } else {
                            Toast.makeText(getContext(), "Sesión Iniciada", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getContext(), menu_lateral.class));
                        }
                    }else{
                        progressLogin.setVisibility(View.INVISIBLE);
                        Toast.makeText(getContext(), "El correo no esta registrado", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return vista;
    }
}
