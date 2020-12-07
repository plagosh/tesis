package cl.paulina.yotrabajoconpecs.Stack;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import cl.paulina.yotrabajoconpecs.Preferences;
import cl.paulina.yotrabajoconpecs.R;
import cz.msebera.android.httpclient.Header;

public class ActivityStack extends Fragment {
    private GridView gridView;
    private ArrayList id_tarea, tv_tarea, tv_hora, tv_jefatura;
    private ArrayList id_usuario;
    private ArrayList nombre_usuario;
    private ArrayList apellido_usuario;
    private ArrayList id_tarea_lista;
    private ArrayList id;
    private ArrayList correo_pdc;
    private ArrayList nombre_pdc;
    private ArrayList apellido_pdc;
    public String myid = "";
    public String PDC;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.activity_stack, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Tareas PDC terminadas");
        id_tarea = new ArrayList();
        tv_tarea = new ArrayList();
        tv_hora = new ArrayList();
        id_usuario = new ArrayList();
        nombre_usuario = new ArrayList();
        apellido_usuario = new ArrayList();
        tv_jefatura = new ArrayList();
        id_tarea_lista = new ArrayList();
        correo_pdc = new ArrayList();
        nombre_pdc = new ArrayList();
        apellido_pdc = new ArrayList();
        id = new ArrayList();
        gridView = vista.findViewById(R.id.stackRecyclerView);

        descargarUsuario();

        return vista;
    }

    private void descargarPDC(String nombre){
        correo_pdc.clear();
        nombre_usuario.clear();
        apellido_usuario.clear();
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("https://yotrabajoconpecs.ddns.net/query_usuario.php", new AsyncHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200){
                    progressDialog.dismiss();
                    try{
                        JSONArray jsonarray = new JSONArray(new String(responseBody));
                        for(int i = 0; i < jsonarray.length(); i++){
                            nombre_pdc.add(jsonarray.getJSONObject(i).getString("nombre_usuario"));
                            apellido_pdc.add(jsonarray.getJSONObject(i).getString("apellido_usuario"));
                            correo_pdc.add(jsonarray.getJSONObject(i).getString("correo"));
                            String nombre_usuario_conespacios = nombre_pdc.get(i).toString() + " " + apellido_pdc.get(i).toString();
                            String nombre_usuario_sinespacios = nombre_usuario_conespacios.replace(" ", "");
                            if(nombre.equals(nombre_usuario_sinespacios)){
                                PDC = correo_pdc.get(i).toString();
                            }
                        }
                        SolicitudJSON("https://yotrabajoconpecs.ddns.net/query_lista_tarea.php?jefatura=" + myid);
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

    private void SolicitudJSON(String URL_GET_ALL_STACK){
        id_tarea.clear();
        tv_tarea.clear();
        tv_hora.clear();
        tv_jefatura.clear();
        id_tarea_lista.clear();
        id.clear();
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
                            id_tarea_lista.add(jsonarray.getJSONObject(i).getString("id_tarea_lista"));
                            id.add(jsonarray.getJSONObject(i).getString("id_listatarea"));
                        }gridView.setAdapter(new CustomAdapter(getContext()));
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

    private class CustomAdapter extends BaseAdapter {
        Context ctx;
        LayoutInflater layoutInflater;
        CardView cardView;
        ImageView imageView;
        TextView nombre, tarea, hora;
        CheckBox tv_check;

        @RequiresApi(api = Build.VERSION_CODES.M)
        public CustomAdapter(Context applicationContext){
            this.ctx = applicationContext;
            layoutInflater = (LayoutInflater)this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount(){
            return id_tarea.size();
        }

        @Override
        public Object getItem(int position){
            return position;
        }

        @Override
        public long getItemId(int position){
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.card_view_stack, null);
            cardView = (CardView) viewGroup.findViewById(R.id.cardViewStack);
            imageView = (ImageView) viewGroup.findViewById(R.id.fotoDePerfilPDC);
            nombre = (TextView) viewGroup.findViewById(R.id.nombreStack);
            tarea = (TextView) viewGroup.findViewById(R.id.nombreTarea);
            hora = (TextView) viewGroup.findViewById(R.id.horaTarea);
            tv_check = (CheckBox) viewGroup.findViewById(R.id.checkeado);
            imageView.setImageResource(R.drawable.ic_baseline_supervised_user_circle_24);
            String nuevo_texto = tv_tarea.get(position).toString() + " ha terminado la tarea:";
            nombre.setText(nuevo_texto);
            tarea.setText(id_tarea.get(position).toString());
            hora.setText(tv_hora.get(position).toString());
            tv_check.setChecked(false);

            tv_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    descargarPDC(tv_tarea.get(position).toString());
                    //mandar corroboracion
                    validarTareaEmpleador("https://yotrabajoconpecs.ddns.net/validar_tarea_empleador.php?id=" + id.get(position).toString() + "&usuario=" + PDC);
                    //eliminar tarea
                    eliminarTareaLista("https://yotrabajoconpecs.ddns.net/eliminar_ListaTarea.php?id_tarea_lista=" + id_tarea_lista.get(position).toString());
                }
            });
            return viewGroup;
        }


    }

    private void eliminarTareaLista(String URL){
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(URL, new AsyncHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200){
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Se ha eliminado con éxito", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getContext(), "Conexión fallida", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void descargarUsuario(){
        id_usuario.clear();
        nombre_usuario.clear();
        apellido_usuario.clear();
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("https://yotrabajoconpecs.ddns.net/query_usuario.php", new AsyncHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200){
                    progressDialog.dismiss();
                    try{
                        JSONArray jsonarray = new JSONArray(new String(responseBody));
                        for(int i = 0; i < jsonarray.length(); i++){
                            nombre_usuario.add(jsonarray.getJSONObject(i).getString("nombre_usuario"));
                            apellido_usuario.add(jsonarray.getJSONObject(i).getString("apellido_usuario"));
                            id_usuario.add(jsonarray.getJSONObject(i).getString("id_usuario"));
                            String nombre_usuario_conespacios = nombre_usuario.get(i).toString() + " " + apellido_usuario.get(i).toString();
                            String nombre_usuario_sinespacios = nombre_usuario_conespacios.replace(" ", "");
                            String usuario = Preferences.obtenerPreferenceString(getContext(), Preferences.PREFERENCE_USUARIO_LOGIN);
                            if(usuario.equals(nombre_usuario_sinespacios)){
                                myid = id_usuario.get(i).toString();
                            }
                        }
                        SolicitudJSON("https://yotrabajoconpecs.ddns.net/query_lista_tarea.php?jefatura=" + myid);
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

    private void validarTareaEmpleador(String URL){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(URL, new AsyncHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200){
                    //Toast.makeText(getContext(), "Se ha modificado", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getContext(), "Conexión fallida", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
