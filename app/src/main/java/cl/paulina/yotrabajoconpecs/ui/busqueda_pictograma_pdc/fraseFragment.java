package cl.paulina.yotrabajoconpecs.ui.busqueda_pictograma_pdc;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import cl.paulina.yotrabajoconpecs.R;
import cl.paulina.yotrabajoconpecs.ServicioAPI.PreferenciasCompartidas;
import cl.paulina.yotrabajoconpecs.ui.libro.libroFragment;
import cz.msebera.android.httpclient.Header;

import static cl.paulina.yotrabajoconpecs.R.color.colorBlanco;

public class fraseFragment extends Fragment {
    public FloatingActionMenu actionMenu;
    public FloatingActionButton fabSend;
    ArrayList pictos, pictos2;
    public Fragment fragment;
    PreferenciasCompartidas pc;
    AlertDialog.Builder builder;
    ImageButton derecha, izquierda;
    int Sapo = 0;
    Bundle datos;
    public String frase;
    String key = "id_frase";
    public LinearLayout contenido, caja, layout;
    private ArrayList id_frase, url;
    public String ultimo = "1";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.pdc_1_5, container, false);
        actionMenu = vista.findViewById(R.id.fab_tirafrase);
        actionMenu.setClosedOnTouchOutside(true);
        fabSend = vista.findViewById(R.id.fab_tirafrase_enviar);
        fragment = getTargetFragment();
        pictos = new ArrayList();
        pictos2 = new ArrayList();
        id_frase = new ArrayList();
        url = new ArrayList();
        descargarDatos();

        layout = vista.findViewById(R.id.cajaBotonImagen);
        derecha = vista.findViewById(R.id.derecha);
        izquierda = vista.findViewById(R.id.izquierda);
        pc = new PreferenciasCompartidas();
        caja = new LinearLayout(getContext());

        derecha.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                izquierda.setVisibility(View.VISIBLE);
                int last = Integer.parseInt(ultimo);
                last++;
                ultimo = last + "";
                layout.removeAllViews();
                caja.removeAllViews();
                for(int i = 0; i < id_frase.size(); i++) {
                    frase = id_frase.get(i).toString();
                    //Toast.makeText(getContext(), "id_frase: " + frase, Toast.LENGTH_SHORT).show();
                    if(frase.equals(ultimo)) {
                        //Toast.makeText(getContext(), "entre al if id: " + frase, Toast.LENGTH_SHORT).show();
                        ImageButton imagen = new ImageButton(getContext());
                        LinearLayout contenido = new LinearLayout(getContext());
                        contenido.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        contenido.setOrientation(LinearLayout.HORIZONTAL);
                        contenido.setPadding(30,20,20,20);
                        contenido.setGravity(Gravity.CENTER);
                        imagen.setBackgroundResource(R.drawable.boton_rectangulo);
                        imagen.setLayoutParams(new LinearLayout.LayoutParams(800, 800));
                        imagen.setScaleType(ImageButton.ScaleType.FIT_CENTER);
                        Picasso.get().load("https://yotrabajoconpecs.ddns.net/" + url.get(i).toString()).into(imagen);
                        //Toast.makeText(getContext(), "imagen: " + url.get(i).toString() + " id: " + ultimo, Toast.LENGTH_SHORT).show();
                        contenido.addView(imagen);
                        caja.addView(contenido);
                        caja.setOrientation(LinearLayout.VERTICAL);
                        caja.setPadding(30,20,20,20);
                    }
                }
                layout.addView(caja);
            }
        });
        izquierda.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                int last = Integer.parseInt(ultimo);
                last--;
                ultimo = last + "";
                //Toast.makeText(getContext(), "n: " + ultimo, Toast.LENGTH_SHORT).show();
                layout.removeAllViews();
                caja.removeAllViews();
                for(int i = 0; i < id_frase.size(); i++) {
                    frase = id_frase.get(i).toString();
                    if(ultimo.equals(frase)) {
                        pictos2.add(url.get(i).toString());
                    }
                }
                for(int i = 0; i < pictos2.size(); i++) {
                    //Toast.makeText(getContext(), "entre al if: " , Toast.LENGTH_SHORT).show();
                    ImageButton imagen = new ImageButton(getContext());
                    LinearLayout contenido = new LinearLayout(getContext());
                    contenido.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    contenido.setOrientation(LinearLayout.HORIZONTAL);
                    contenido.setPadding(30,20,20,20);
                    imagen.setBackgroundResource(R.drawable.boton_rectangulo);
                    imagen.setLayoutParams(new LinearLayout.LayoutParams(800, 800));
                    imagen.setScaleType(ImageButton.ScaleType.FIT_CENTER);
                    Picasso.get().load("https://yotrabajoconpecs.ddns.net/" + pictos2.get(i).toString()).into(imagen);
                    //Toast.makeText(getContext(), "imagen: " + pictos2.get(i).toString() + " id: " + i, Toast.LENGTH_SHORT).show();
                    contenido.addView(imagen);
                    caja.addView(contenido);
                    caja.setOrientation(LinearLayout.VERTICAL);
                    caja.setPadding(30,20,20,20);
                }
                layout.addView(caja);
                pictos2.clear();
            }
        });
        fabSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Mensaje enviado", Toast.LENGTH_SHORT).show();
            }
        });
        return vista;
    }

    private void descargarDatos(){
        url.clear();
        id_frase.clear();
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("https://yotrabajoconpecs.ddns.net/query7.php", new AsyncHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200){
                    progressDialog.dismiss();
                    try{
                        JSONArray jsonarray = new JSONArray(new String(responseBody));
                        for(int i = 0; i < jsonarray.length(); i++){
                            id_frase.add(jsonarray.getJSONObject(i).getString("frase_detallefrase"));
                            url.add(jsonarray.getJSONObject(i).getString("url"));
                        }
                        ImageButton imagen = new ImageButton(getContext());
                        LinearLayout contenido = new LinearLayout(getContext());
                        contenido.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        contenido.setOrientation(LinearLayout.HORIZONTAL);
                        contenido.setPadding(30,20,20,20);
                        imagen.setBackgroundResource(R.drawable.boton_rectangulo);
                        imagen.setScaleType(ImageButton.ScaleType.FIT_CENTER);
                        imagen.setLayoutParams(new LinearLayout.LayoutParams(800, 800));
                        Picasso.get().load("https://yotrabajoconpecs.ddns.net/" + url.get(0)).into(imagen);
                        contenido.addView(imagen);
                        caja.addView(contenido);
                        layout.addView(caja);
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
