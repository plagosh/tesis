package cl.paulina.yotrabajoconpecs.Stack;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.List;
import java.util.Stack;

import cl.paulina.yotrabajoconpecs.Amigos.AmigosAdapter;
import cl.paulina.yotrabajoconpecs.Amigos.AmigosAtributos;
import cl.paulina.yotrabajoconpecs.R;
import cz.msebera.android.httpclient.Header;

public class ActivityStack extends Fragment {
    private TextView tarea, hora;
    private CheckBox checkear;
    private List<StackAtributos> atributosList;
    private StackAdapter adapter;
    private static final String URL_GET_ALL_STACK = "https://yotrabajoconpecs.ddns.net/query_lista_tarea.php";
    private ArrayList id_tarea, tv_tarea, tv_hora;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.activity_stack, container, false);
        atributosList = new ArrayList<>();
        id_tarea = new ArrayList();
        tv_tarea = new ArrayList();
        tv_hora = new ArrayList();
        RecyclerView rv = vista.findViewById(R.id.stackRecyclerView);
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(lm);

        adapter = new StackAdapter(atributosList, getContext());
        rv.setAdapter(adapter);
        tarea = vista.findViewById(R.id.nombreTarea);
        hora = vista.findViewById(R.id.horaTarea);
        checkear = vista.findViewById(R.id.checkeado);
        SolicitudJSON();
        return vista;
    }

    public void agregarStack(int fotoDePerfil, String nombre, String tarea, String hora){
        StackAtributos stackAtributos = new StackAtributos();
        stackAtributos.setFotoDePerfil(fotoDePerfil);
        stackAtributos.setNombre(nombre);
        stackAtributos.setTarea(tarea);
        stackAtributos.setHora(hora);
        atributosList.add(stackAtributos);
        adapter.notifyDataSetChanged();
    }

    private void SolicitudJSON(){
        id_tarea.clear();
        tv_tarea.clear();
        tv_hora.clear();
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(URL_GET_ALL_STACK, new AsyncHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200){
                    progressDialog.dismiss();
                    try{
                        JSONArray jsonarray = new JSONArray(new String(responseBody));
                        for(int i = 0; i < jsonarray.length(); i++){
                            id_tarea.add(jsonarray.getJSONObject(i).getString("nombre_tarea"));
                            tv_tarea.add(jsonarray.getJSONObject(i).getString("quien_envia"));
                            tv_hora.add(jsonarray.getJSONObject(i).getString("fecha"));
                            String mensaje = tv_tarea.get(i).toString() + " ha terminado la tarea:";
                            agregarStack(R.drawable.ic_baseline_supervised_user_circle_24, mensaje, id_tarea.get(i).toString(), tv_hora.get(i).toString());
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
    }
}
