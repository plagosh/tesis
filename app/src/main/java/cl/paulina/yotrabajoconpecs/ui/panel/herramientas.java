package cl.paulina.yotrabajoconpecs.ui.panel;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
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
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Herramientas");
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

        Bundle datosRecibido = getArguments();
        String id = datosRecibido.getString("id_desglose");
        descargarDatos("https://yotrabajoconpecs.ddns.net/query4.php?desgloce=" + id);

        return vista;
    }

    private void descargarDatos(String URL){
        id_herramienta.clear();
        desglose_id_desglose.clear();
        url.clear();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(URL, new AsyncHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200){
                    try{
                        JSONArray jsonarray = new JSONArray(new String(responseBody));
                        for(int i = 0; i < jsonarray.length(); i++){
                            //
                            id_herramienta.add(jsonarray.getJSONObject(i).getString("id_herramienta"));
                            desglose_id_desglose.add(jsonarray.getJSONObject(i).getString("desglose_id_desglose"));
                            url.add(jsonarray.getJSONObject(i).getString("url"));

                            ImageButton imagen = new ImageButton(getContext());
                            imagen.setBackgroundResource(R.drawable.boton_rectangulo);
                            imagen.setScaleType(ImageButton.ScaleType.FIT_CENTER);
                            imagen.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,400));
                            imagen.setId(i);
                            Picasso.get().load("https://yotrabajoconpecs.ddns.net/" + url.get(i)).into(imagen);
                            layout.setOrientation(LinearLayout.VERTICAL);
                            layout.setPadding(10, 10, 10, 10);
                            layout.addView(imagen);
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
