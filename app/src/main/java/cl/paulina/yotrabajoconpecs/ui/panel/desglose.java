package cl.paulina.yotrabajoconpecs.ui.panel;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import cl.paulina.yotrabajoconpecs.R;
import cl.paulina.yotrabajoconpecs.ServicioAPI.PreferenciasCompartidas;
import cl.paulina.yotrabajoconpecs.ui.libro.libroFragment;
import cz.msebera.android.httpclient.Header;

public class desglose extends Fragment {
    ArrayList pictos, pictos2;
    public Fragment fragment;
    PreferenciasCompartidas pc;
    AlertDialog.Builder builder;
    int Sapo = 0;
    Bundle datos;
    String key = "id_tarea";
    LinearLayout contenido, caja, layout;
    private ArrayList id_desglose, tarea_id_tarea, url, id_detalle;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.pdc_2_1, container, false);
        fragment = getTargetFragment();
        datos = new Bundle();
        pictos = new ArrayList();
        pictos2 = new ArrayList();
        id_desglose = new ArrayList();
        tarea_id_tarea = new ArrayList();
        id_detalle = new ArrayList();
        url = new ArrayList();
        layout = vista.findViewById(R.id.cajaBotonImagen);

        pc = new PreferenciasCompartidas();
        caja = new LinearLayout(getContext());
        caja.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        caja.setOrientation(LinearLayout.HORIZONTAL);
        caja.setOrientation(LinearLayout.VERTICAL);
        pc.loadData(pictos, getContext(), key);
        descargando();

        return vista;
    }

    private void descargando(){
        id_desglose.clear();
        tarea_id_tarea.clear();
        id_detalle.clear();
        url.clear();

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Cargando datos...");
        progressDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("https://yotrabajoconpecs.ddns.net/query2.php", new AsyncHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200){
                    progressDialog.dismiss();
                    Bundle datosRecibido = getArguments();
                    String id = datosRecibido.getString("id_tarea");
                    Toast.makeText(getContext(), "tengo el id_tarea: " + id, Toast.LENGTH_SHORT).show();
                    try{
                         JSONArray jsonarray = new JSONArray(new String(responseBody));

                         for(int j = 0; j < jsonarray.length(); j++){
                            //Toast.makeText(getContext(), "recorriendo for " + j, Toast.LENGTH_SHORT).show();
                            id_desglose.add(jsonarray.getJSONObject(j).getString("id_desglose"));
                            tarea_id_tarea.add(jsonarray.getJSONObject(j).getString("tarea_id_tarea"));
                            id_detalle.add(jsonarray.getJSONObject(j).getString("id_detalle"));
                            url.add(jsonarray.getJSONObject(j).getString("url"));

                            //Toast.makeText(getContext(), "id_desglose: " + id_desglose.get(j).toString(), Toast.LENGTH_SHORT).show();
                            String tarea = tarea_id_tarea.get(j).toString();
                            if(tarea.equals(id)){
                                pictos.add(url.get(j).toString());
                                pictos2.add(id_desglose.get(j).toString());
                            }
                        }
                        if (pictos.size() > 0) {
                            Log.e("GenerarFotoTamañoLista ", String.valueOf(pictos.size()));
                            for (int i = 0; pictos.size()> i; i++) {
                                if(pictos.get(i) != "") {
                                    ImageButton imagen = new ImageButton(getContext());
                                    LinearLayout contenido = new LinearLayout(getContext());
                                    contenido.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    contenido.setOrientation(LinearLayout.HORIZONTAL);
                                    imagen.setLayoutParams(new LinearLayout.LayoutParams(600, 600));
                                    imagen.setPadding(10, 10, 10, 10);
                                    imagen.setId(i);
                                    Picasso.get().load("https://yotrabajoconpecs.ddns.net/" + pictos.get(i)).into(imagen);
                                    contenido.addView(imagen);
                                    caja.addView(contenido);
                                    String pasando_dato = pictos2.get(i).toString();
                                    imagen.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Fragment fragment = new herramientas();
                                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                            transaction.replace(R.id.nav_host_fragment, fragment);
                                            transaction.addToBackStack(null);
                                            transaction.commit();
                                            datos.putString("id_desglose", pasando_dato);
                                            Log.e("Enviar", "desglose enviada");
                                            fragment.setArguments(datos);
                                            Toast.makeText(getContext(), "pase el id_desglose: " + pasando_dato, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                            layout.addView(caja);
                        }
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //progressDialog.dismiss();
                Context context = getContext();
                CharSequence text = "Conexión fallida";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });
    }
}
