package cl.paulina.yotrabajoconpecs.ui.busqueda_pictograma_empleador;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cl.paulina.yotrabajoconpecs.R;
import cz.msebera.android.httpclient.Header;

public class Mensajeria extends Fragment {
    public static final String MENSAJE = "MENSAJE";
    private BroadcastReceiver bR;
    private RecyclerView rv;
    private List<MensajeDeTexto> mensajedetexto;
    private MesajesAdapter adapter;
    private Button bTEnviarMensaje;
    private EditText eTEscribirMensaje;
    private int TEXT_LINES = 1;
    private String MENSAJE_ENVIAR = "";
    private String EMISOR;
    private String NOMBRE;
    private String RECEPTOR;
    public ArrayList nombre_login, correo_login;
    private static final String IP_MENSAJE = "https://yotrabajoconpecs.ddns.net/Enviar_Mensajes.php";
    private ArrayList descargar_mensaje;
    private ArrayList descargar_tipo;
    private ArrayList descargar_hora;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.mensajeria, container, false);
        mensajedetexto = new ArrayList<>();
        rv = vista.findViewById(R.id.recyclerview);

        nombre_login = new ArrayList();
        correo_login = new ArrayList();
        descargar_mensaje = new ArrayList();
        descargar_tipo = new ArrayList();
        descargar_hora = new ArrayList();

        Bundle bundle = getArguments();
        Bundle datosRecibido = getArguments();
        if (datosRecibido != null) {
            RECEPTOR = datosRecibido.getString("key_receptor");
        }

        descargarDatos();

        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(lm);
        adapter = new MesajesAdapter(mensajedetexto, getContext());
        rv.setAdapter(adapter);
        bTEnviarMensaje = vista.findViewById(R.id.bTEnviarMensaje);
        eTEscribirMensaje = vista.findViewById(R.id.eTEscribirMensaje);

        eTEscribirMensaje.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (eTEscribirMensaje.getLayout().getLineCount() != TEXT_LINES) {
                    setScrollbarChat();
                    TEXT_LINES = eTEscribirMensaje.getLayout().getLineCount();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        bTEnviarMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //para enviar el mensaje accedemos al método.
                String mensaje = eTEscribirMensaje.getText().toString().trim();
                if (!mensaje.isEmpty()) {
                    MENSAJE_ENVIAR = mensaje;
                    Date dt = new Date();
                    int hours = dt.getHours();
                    int minutes = dt.getMinutes();
                    String curTime = hours + ":" + minutes + ", Hoy";
                    MandarMensaje();
                    CreateMensaje(mensaje, curTime, 1);
                    eTEscribirMensaje.setText("");
                }
            }
        });

        bR = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String mensaje = intent.getStringExtra("key_mensaje");
                String hora = intent.getStringExtra("key_hora");
                String horaParametros[] = hora.split("\\,");
                String emisor = intent.getStringExtra("key_emisor_PHP");
                if(emisor.equals(RECEPTOR)){
                    CreateMensaje(mensaje, horaParametros[0], 2);
                }
            }
        };

        return vista;
    }

    private void descargarDatos() {
        nombre_login.clear();
        correo_login.clear();
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("https://yotrabajoconpecs.ddns.net/query_loginreciclado.php", new AsyncHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    progressDialog.dismiss();
                    try {
                        JSONArray jsonarray = new JSONArray(new String(responseBody));
                        for (int i = 0; i < jsonarray.length(); i++) {
                            nombre_login.add(jsonarray.getJSONObject(i).getString("nombres"));
                            correo_login.add(jsonarray.getJSONObject(i).getString("correo"));
                        }
                        EMISOR = correo_login.get(correo_login.size()-1).toString();
                        NOMBRE = nombre_login.get(nombre_login.size()-1).toString();
                        DescargarMensajes("https://yotrabajoconpecs.ddns.net/query_mensajes_empleador.php?usuario=" + correo_login.get(correo_login.size()-1).toString());
                    } catch (JSONException e) {
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
    }

    private void MandarMensaje() {
        //Toast.makeText(Login.this, "entre a SubirToken", Toast.LENGTH_SHORT).show();
        HashMap<String, String> hashMapToken = new HashMap<>();
        hashMapToken.put("emisor", EMISOR);
        hashMapToken.put("nombrecompleto", NOMBRE);
        hashMapToken.put("receptor", RECEPTOR);
        hashMapToken.put("mensaje", MENSAJE_ENVIAR);

        //Toast.makeText(getContext(), RECEPTOR, Toast.LENGTH_SHORT).show();
        //Toast.makeText(getContext(), MENSAJE_ENVIAR, Toast.LENGTH_SHORT).show();
        //Toast.makeText(Login.this, emailemail + FirebaseInstanceId.getInstance().getToken(), Toast.LENGTH_SHORT).show();

        JsonObjectRequest solicitud = new JsonObjectRequest(Request.Method.POST, IP_MENSAJE, new JSONObject(hashMapToken), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Toast.makeText(getContext(), response.getString("resultado"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Ocurrió un error aqui", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(solicitud);
    }

    public void CreateMensaje(String mensaje, String hora, int tipoDeMensaje) {
        MensajeDeTexto mensajeDeTextoAuxiliar = new MensajeDeTexto();
        mensajeDeTextoAuxiliar.setId("0");
        mensajeDeTextoAuxiliar.setMensaje(mensaje);
        mensajeDeTextoAuxiliar.setTipoMensaje(tipoDeMensaje);
        mensajeDeTextoAuxiliar.setHoraDelMensaje(hora);
        mensajedetexto.add(mensajeDeTextoAuxiliar);
        adapter.notifyDataSetChanged();
        setScrollbarChat();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(bR);
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(bR, new IntentFilter(MENSAJE));
    }

    public void setScrollbarChat() {
        rv.scrollToPosition(adapter.getItemCount() - 1);
    }

    private void DescargarMensajes(String URL){
        descargar_mensaje.clear();
        descargar_tipo.clear();
        descargar_hora.clear();
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(URL, new AsyncHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200){
                    progressDialog.dismiss();
                    try{
                        JSONArray jsonarray = new JSONArray(new String(responseBody));
                        for(int i = 0; i < jsonarray.length(); i++){
                            descargar_mensaje.add(jsonarray.getJSONObject(i).getString("mensaje"));
                            descargar_tipo.add(jsonarray.getJSONObject(i).getString("tipo_mensaje"));
                            descargar_hora.add(jsonarray.getJSONObject(i).getString("hora_del_mensaje"));
                            String curTime = descargar_hora.get(i).toString();
                            String mensaje = descargar_mensaje.get(i).toString();
                            int tipo = Integer.parseInt(descargar_tipo.get(i).toString());
                            CreateMensaje(mensaje, curTime, tipo);
                        }
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getContext(), "Conexión fallida", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
