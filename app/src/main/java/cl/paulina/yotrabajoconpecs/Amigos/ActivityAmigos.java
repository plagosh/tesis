package cl.paulina.yotrabajoconpecs.Amigos;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cl.paulina.yotrabajoconpecs.R;

public class ActivityAmigos extends Fragment {
    public static final String MENSAJE = "MENSAJE";
    private BroadcastReceiver bR;
    private List<AmigosAtributos> atributosList;
    private AmigosAdapter adapter;
    public String mensaje, emisor, horaParametros[];
    private static final String URL_GET_ALL_USUARIOS = "https://yotrabajoconpecs.ddns.net/Amigos_GETALL.php";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.activity_amigos, container, false);

        atributosList = new ArrayList<>();

        RecyclerView rv = vista.findViewById(R.id.amigosRecyclerView);
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(lm);

        adapter = new AmigosAdapter(atributosList, getContext());
        rv.setAdapter(adapter);

        SolicitudJSON();

        return vista;
    }

    public void agregarAmigos(int fotoDePerfil, String nombre, String ultimoMensaje, String hora, String id){
        AmigosAtributos amigosAtributos = new AmigosAtributos();
        amigosAtributos.setFotoDePerfil(fotoDePerfil);
        amigosAtributos.setNombre(nombre);
        amigosAtributos.setUltimoMensaje(ultimoMensaje);
        amigosAtributos.setHora(hora);
        atributosList.add(amigosAtributos);
        amigosAtributos.setId(id);
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
                        String nombreCompleto = js.getString("nombre_usuario") + " " + js.getString("apellido_usuario");
                        agregarAmigos(R.drawable.ic_baseline_supervised_user_circle_24, nombreCompleto, "último mensaje", "00:00", js.getString("correo"));
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
}
