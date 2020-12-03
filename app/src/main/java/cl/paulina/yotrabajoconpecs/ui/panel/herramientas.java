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

public class herramientas extends Fragment {
    ArrayList pictos;
    public Fragment fragment;
    private PreferenciasCompartidas pc;
    Bundle datos;
    String key = "id_desglose";
    LinearLayout caja, layout;
    private ArrayList id_herramienta, desglose_id_desglose, url;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.pdc_2_1_1, container, false);
        fragment = getTargetFragment();
        datos = new Bundle();
        pictos = new ArrayList();
        id_herramienta = new ArrayList();
        desglose_id_desglose = new ArrayList();
        url = new ArrayList();
        layout = vista.findViewById(R.id.cajaBotonImagen);

        pc = new PreferenciasCompartidas();
        caja = new LinearLayout(getContext());
        caja.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        caja.setOrientation(LinearLayout.HORIZONTAL);
        caja.setOrientation(LinearLayout.VERTICAL);
        pc.loadData(pictos, getContext(), key);
        descargarDatos();


        return vista;
    }

    private void descargarDatos(){
        id_herramienta.clear();
        desglose_id_desglose.clear();
        url.clear();

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Cargando datos...");
        progressDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("https://yotrabajoconpecs.ddns.net/query4.php", new AsyncHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200){
                    progressDialog.dismiss();
                    Bundle datosRecibido = getArguments();
                    String id = datosRecibido.getString("id_desglose");
                    //Toast.makeText(getContext(), "tengo el id_desglose: " + id, Toast.LENGTH_SHORT).show();
                    try{
                        JSONArray jsonarray = new JSONArray(new String(responseBody));
                        for(int i = 0; i < jsonarray.length(); i++){
                            //Toast.makeText(getContext(), "recorriendo for " + i, Toast.LENGTH_SHORT).show();
                            id_herramienta.add(jsonarray.getJSONObject(i).getString("id_herramienta"));
                            desglose_id_desglose.add(jsonarray.getJSONObject(i).getString("desglose_id_desglose"));
                            url.add(jsonarray.getJSONObject(i).getString("url"));

                            String desglose = desglose_id_desglose.get(i).toString();
                            if(desglose.equals(id)){
                                pictos.add(url.get(i).toString());
                            }
                        }
                        if (pictos.size() > 0) {
                            Log.e("GenerarFotoTamañoLista ", String.valueOf(pictos.size()));
                            for (int j = 0; pictos.size()> j; j++) {
                                if(pictos.get(j) != "") {
                                    ImageButton imagen = new ImageButton(getContext());
                                    LinearLayout contenido = new LinearLayout(getContext());
                                    contenido.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    contenido.setOrientation(LinearLayout.HORIZONTAL);
                                    imagen.setLayoutParams(new LinearLayout.LayoutParams(400, 400));
                                    imagen.setPadding(10, 10, 10, 10);
                                    imagen.setId(j);
                                    Picasso.get().load("https://yotrabajoconpecs.ddns.net/" + pictos.get(j)).into(imagen);
                                    contenido.addView(imagen);
                                    caja.addView(contenido);
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
