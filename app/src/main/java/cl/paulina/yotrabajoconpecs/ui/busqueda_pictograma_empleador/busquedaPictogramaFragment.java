package cl.paulina.yotrabajoconpecs.ui.busqueda_pictograma_empleador;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import cl.paulina.yotrabajoconpecs.R;
import cl.paulina.yotrabajoconpecs.ServicioAPI.ArasaacAPI;
import cl.paulina.yotrabajoconpecs.ServicioAPI.PreferenciasCompartidas;
import cl.paulina.yotrabajoconpecs.modelo.Pictograma;
import cl.paulina.yotrabajoconpecs.ui.libro.libroFragment;
import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class busquedaPictogramaFragment extends Fragment {
    Button btnbuscar;
    EditText texto;
    public FloatingActionMenu actionMenu;
    public FloatingActionButton fabSend;
    public FloatingActionButton fabDelete;
    public Fragment fragment;
    public LinearLayout caja, layout;
    public ArrayList id_imagen, nombre_imagen, url, categoria_imagen, pictos, pictos2;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.empleador_1_1, container, false);
        btnbuscar = vista.findViewById(R.id.btnBusquedaPictograma);
        texto = vista.findViewById(R.id.etBusquedaPictograma);
        actionMenu = vista.findViewById(R.id.fab_tirafrase);
        actionMenu.setClosedOnTouchOutside(true);
        fabSend = vista.findViewById(R.id.fab_tirafrase_enviar);
        fabDelete = vista.findViewById(R.id.fab_tirafrase_eliminar);
        fragment = getTargetFragment();

        id_imagen = new ArrayList();
        nombre_imagen = new ArrayList();
        url = new ArrayList();
        categoria_imagen = new ArrayList();
        pictos = new ArrayList();
        pictos2 = new ArrayList();

        layout = vista.findViewById(R.id.cajaBotonImagen);
        caja = new LinearLayout(getContext());

        descargarDatos();

        btnbuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = texto.getText().toString();
                layout.removeAllViews();
                caja.removeAllViews();
                if (searchText.isEmpty()){
                    texto.setError("Escriba antes de buscar...");
                }else {
                    String[] palabrasSeparadas = searchText.split(" ");
                    for(int i = 0; i < palabrasSeparadas.length; i++){
                        String buscarseparadas = palabrasSeparadas[i];
                        //Toast.makeText(getContext(),buscarseparadas,Toast.LENGTH_SHORT).show();
                        for(int j = 0; j < nombre_imagen.size(); j++){
                            String nomimagen = nombre_imagen.get(j).toString();
                            if(buscarseparadas.equals(nomimagen)){
                                pictos.add(url.get(j).toString());
                                pictos2.add(nombre_imagen.get(j).toString());
                                //Toast.makeText(getContext(),url.get(j).toString(),Toast.LENGTH_SHORT).show();
                            }else{
                                //Toast.makeText(getContext(),"Palabra " + palabrasSeparadas[i] + " no encontrada.",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    for(int i = 0; i < pictos.size(); i++){
                        Toast.makeText(getContext(), pictos2.get(i).toString(), Toast.LENGTH_SHORT).show();
                        ImageButton imagen = new ImageButton(getContext());
                        TextView palabra = new TextView(getContext());
                        LinearLayout contenido = new LinearLayout(getContext());
                        contenido.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        contenido.setOrientation(LinearLayout.HORIZONTAL);
                        contenido.setPadding(30,20,20,20);
                        imagen.setBackgroundResource(R.drawable.boton_rectangulo);
                        imagen.setLayoutParams(new LinearLayout.LayoutParams(400, 400));
                        imagen.setScaleType(ImageButton.ScaleType.FIT_CENTER);
                        palabra.setLayoutParams(new LinearLayout.LayoutParams(400, 400));
                        palabra.setGravity(Gravity.CENTER);
                        palabra.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25.f);
                        Picasso.get().load("https://yotrabajoconpecs.ddns.net/" + pictos.get(i).toString()).into(imagen);
                        //Toast.makeText(getContext(), "imagen: " + pictos2.get(i).toString() + " id: " + i, Toast.LENGTH_SHORT).show();
                        palabra.setText(pictos2.get(i).toString());
                        contenido.addView(imagen);
                        contenido.addView(palabra);
                        caja.addView(contenido);
                        caja.setOrientation(LinearLayout.VERTICAL);
                        caja.setPadding(30,20,20,20);
                    }
                    layout.addView(caja);
                    pictos.clear();
                    pictos2.clear();
                }
            }
        });

        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.removeAllViews();
                caja.removeAllViews();
            }
        });

        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Mensaje enviado", Toast.LENGTH_SHORT).show();
            }
        });

        return vista;
    }

    private void descargarDatos() {
        id_imagen.clear();
        nombre_imagen.clear();
        url.clear();
        categoria_imagen.clear();
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("https://yotrabajoconpecs.ddns.net/query.php", new AsyncHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    progressDialog.dismiss();
                    try {
                        JSONArray jsonarray = new JSONArray(new String(responseBody));
                        for (int i = 0; i < jsonarray.length(); i++) {
                            id_imagen.add(jsonarray.getJSONObject(i).getString("id_imagen"));
                            nombre_imagen.add(jsonarray.getJSONObject(i).getString("nombre_imagen"));
                            url.add(jsonarray.getJSONObject(i).getString("url"));
                            categoria_imagen.add(jsonarray.getJSONObject(i).getString("categoria_imagen"));
                        }
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
}