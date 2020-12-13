package cl.paulina.yotrabajoconpecs.MisClases.LibroPDC;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cl.paulina.yotrabajoconpecs.R;
import cz.msebera.android.httpclient.Header;

public class ListaVerboSustFragment extends Fragment{

    private GridView gridView;
    private ArrayList id_imagen, id_arasaac, nombre_imagen, categoria_imagen, url;
    public Fragment fragment;
    private ImageButton tvimagen;
    Bundle datos;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.pdc_1_2, container, false);
        datos = new Bundle();
        gridView = vista.findViewById(R.id.listview);
        tvimagen = vista.findViewById(R.id.tv_imagen);
        id_imagen = new ArrayList();
        id_arasaac = new ArrayList();
        nombre_imagen = new ArrayList();
        categoria_imagen = new ArrayList();
        url = new ArrayList();
        descargarDatos();
        return vista;
    }

    private void descargarDatos(){
        id_imagen.clear();
        id_arasaac.clear();
        nombre_imagen.clear();
        categoria_imagen.clear();
        url.clear();
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("https://yotrabajoconpecs.ddns.net/query.php", new AsyncHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200){
                    progressDialog.dismiss();
                    try{
                        JSONArray jsonarray = new JSONArray(new String(responseBody));
                        for(int i = 0; i < jsonarray.length(); i++){
                            categoria_imagen.add(jsonarray.getJSONObject(i).getString("categoria_imagen"));
                            id_imagen.add(jsonarray.getJSONObject(i).getString("id_imagen"));
                            nombre_imagen.add(jsonarray.getJSONObject(i).getString("nombre_imagen"));
                            url.add(jsonarray.getJSONObject(i).getString("url"));
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
        TextView tvnombre;
        @RequiresApi(api = Build.VERSION_CODES.M)
        public CustomAdapter(Context applicationContext){
            this.ctx = applicationContext;
            layoutInflater = (LayoutInflater)this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount(){
            return nombre_imagen.size();
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
            ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.list_view_item, null);
            tvimagen = (ImageButton) viewGroup.findViewById(R.id.tv_imagen);
            tvnombre = (TextView) viewGroup.findViewById(R.id.tv_nombre);

            Object var = url.get(position).toString();
            Picasso.get().load("https://yotrabajoconpecs.ddns.net/" + var).into(tvimagen);
            tvimagen.setLayoutParams(new LinearLayout.LayoutParams(200, 200));
            tvimagen.setScaleType(ImageButton.ScaleType.FIT_CENTER);
            if(categoria_imagen.get(position).toString().equals("3")){
                tvimagen.setBackgroundResource(R.drawable.boton_rectangulo_verbo);
            }else{
                tvimagen.setBackgroundResource(R.drawable.boton_rectangulo_sustantivo);
            }
            tvnombre.setText(nombre_imagen.get(position).toString());
            tvimagen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment = new LibroPDCFragment();
                    datos.putString("url_verbo", url.get(position).toString());
                    datos.putString("categoria_imagen_verbo", categoria_imagen.get(position).toString());
                    datos.putString("nombre_imagen_verbo", nombre_imagen.get(position).toString());
                    fragment.setArguments(datos);
                    cambiarFragmento(fragment);
                    String consulta2 = "INSERT INTO `automata` (`idautomata`, `fecha`, `categoria`) VALUES (NULL, current_timestamp(), '"+categoria_imagen.get(position).toString()+"');";
                    ejecutarservicio("https://yotrabajoconpecs.ddns.net/save.php", consulta2);
                }
            });
            return viewGroup;
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        OnBackPressedCallback callback = new OnBackPressedCallback (true){
            @Override
            public void handleOnBackPressed() {
                cambiarFragmento(fragment);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this,callback);
    }
    public void cambiarFragmento(Fragment fragment){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void ejecutarservicio(String URL, String dato){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getContext(), "OPERACION EXITOSA", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String,String>();
                parametros.put("id","");
                parametros.put("fecha","");
                parametros.put("categoria",dato);
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

}