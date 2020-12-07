package cl.paulina.yotrabajoconpecs.ui.panel;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import cl.paulina.yotrabajoconpecs.R;
import cl.paulina.yotrabajoconpecs.ServicioAPI.PreferenciasCompartidas;
import cz.msebera.android.httpclient.Header;

public class desglose extends Fragment {
    ArrayList pictos, pictos2;
    public Fragment fragment;
    PreferenciasCompartidas pc;
    Bundle datos;
    String key = "id_tarea";
    LinearLayout layout;
    ImageButton imagen;
    private ArrayList id_desglose, tarea_id_tarea, url, id_detalle, id_herramienta;
    public String pasando_dato;
    public String HERRAMIENTA;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.pdc_2_1, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Tareas");
        fragment = getTargetFragment();
        datos = new Bundle();
        pictos = new ArrayList();
        pictos2 = new ArrayList();
        id_desglose = new ArrayList();
        tarea_id_tarea = new ArrayList();
        id_detalle = new ArrayList();
        url = new ArrayList();
        id_herramienta = new ArrayList();
        layout = vista.findViewById(R.id.cajaBotonImagen);

        pc = new PreferenciasCompartidas();
        pc.loadData(pictos, getContext(), key);
        Bundle datosRecibido = getArguments();
        String id = datosRecibido.getString("id_tarea");
        descargando("https://yotrabajoconpecs.ddns.net/query2.php?tarea=" + id);

        return vista;
    }

    private void descargando(String URL){
        id_desglose.clear();
        tarea_id_tarea.clear();
        id_detalle.clear();
        url.clear();
        id_herramienta.clear();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(URL, new AsyncHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200){
                    try{
                         JSONArray jsonarray = new JSONArray(new String(responseBody));
                         for(int j = 0; j < jsonarray.length(); j++){
                            id_desglose.add(jsonarray.getJSONObject(j).getString("id_desglose"));
                            tarea_id_tarea.add(jsonarray.getJSONObject(j).getString("tarea_id_tarea"));
                            id_detalle.add(jsonarray.getJSONObject(j).getString("id_detalle"));
                            url.add(jsonarray.getJSONObject(j).getString("url"));
                            id_herramienta.add(jsonarray.getJSONObject(j).getString("id_herramienta"));
                            imagen = new ImageButton(getContext());
                            imagen.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 400));
                            imagen.setBackgroundResource(R.drawable.boton_rectangulo);
                            imagen.setScaleType(ImageButton.ScaleType.FIT_CENTER);
                            imagen.setId(j);
                            layout.setOrientation(LinearLayout.VERTICAL);
                            layout.setPadding(10, 10, 10, 10);
                            Picasso.get().load("https://yotrabajoconpecs.ddns.net/" + url.get(j).toString()).into(imagen);
                            layout.addView(imagen);
                            if(id_herramienta.get(j).toString().equals("null")){

                            }else{
                                pasando_dato = id_desglose.get(j).toString();
                                imagen.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Fragment fragment = new herramientas();
                                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                        transaction.replace(R.id.nav_host_fragment, fragment);
                                        transaction.addToBackStack(null);
                                        transaction.commit();
                                        datos.putString("id_desglose", pasando_dato);
                                        fragment.setArguments(datos);
                                    }
                                });
                            }
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
