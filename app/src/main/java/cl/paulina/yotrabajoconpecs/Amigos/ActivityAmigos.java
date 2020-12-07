package cl.paulina.yotrabajoconpecs.Amigos;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
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
import java.util.HashMap;
import java.util.List;

import cl.paulina.yotrabajoconpecs.R;
import cz.msebera.android.httpclient.Header;

public class ActivityAmigos extends Fragment {
    public static final String MENSAJE = "MENSAJE";
    private BroadcastReceiver bR;
    private List<AmigosAtributos> atributosList;
    private AmigosAdapter adapter;
    public String mensaje, emisor, horaParametros[];
    private static final String URL_GET_ALL_USUARIOS = "https://yotrabajoconpecs.ddns.net/Amigos_GETALL.php";
    private ArrayList descargar_mensaje;
    private ArrayList descargar_hora;
    public String nombreCompleto;
    public String correo;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.activity_amigos, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Lista PDC");
        atributosList = new ArrayList<>();

        RecyclerView rv = vista.findViewById(R.id.amigosRecyclerView);
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(lm);

        adapter = new AmigosAdapter(atributosList, getContext());
        rv.setAdapter(adapter);

        descargar_mensaje = new ArrayList();
        descargar_hora = new ArrayList();

        SolicitudJSON();

        return vista;
    }

    public void agregarAmigos(int fotoDePerfil, String nombre, String ultimoMensaje, String hora, String id){
        AmigosAtributos amigosAtributos = new AmigosAtributos();
        amigosAtributos.setFotoDePerfil(fotoDePerfil);
        amigosAtributos.setNombre(nombre);
        amigosAtributos.setUltimoMensaje(ultimoMensaje);
        amigosAtributos.setHora(hora);
        amigosAtributos.setId(id);
        atributosList.add(amigosAtributos);
        adapter.notifyDataSetChanged();
    }

    public void SolicitudJSON(){
        JsonObjectRequest solicitud = new JsonObjectRequest(URL_GET_ALL_USUARIOS, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String responseBody = response.getString("resultado");
                    JSONArray jsonarray = new JSONArray(new String(responseBody));
                    for(int i = 0; i < jsonarray.length(); i++){
                        JSONObject js = jsonarray.getJSONObject(i);
                        nombreCompleto = js.getString("nombre_usuario") + " " + js.getString("apellido_usuario");
                        correo = js.getString("correo");
                        String jefatura = js.getString("jefatura");
                        DescargarMensajes("https://yotrabajoconpecs.ddns.net/query_ultimo_mensaje.php?usuario=" + correo);
                    }
                } catch (JSONException e) {
                    Toast.makeText(getContext(), "Ocurrió un error al descomponer el JSON", Toast.LENGTH_SHORT).show();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Ocurrió un error, por favor contactese con el administrador", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(solicitud);
    }

    private void DescargarMensajes(String URL){
        descargar_mensaje.clear();
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
                            descargar_hora.add(jsonarray.getJSONObject(i).getString("hora_del_mensaje"));
                            String curTime = descargar_hora.get(i).toString().substring(0,5);
                            String mensaje = descargar_mensaje.get(i).toString();
                            agregarAmigos(R.drawable.ic_baseline_supervised_user_circle_24, nombreCompleto, mensaje, curTime, correo);
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
