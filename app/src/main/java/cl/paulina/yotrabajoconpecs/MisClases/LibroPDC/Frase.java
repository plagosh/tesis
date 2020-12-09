package cl.paulina.yotrabajoconpecs.MisClases.LibroPDC;

import android.annotation.SuppressLint;
import android.app.Dialog;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import cl.paulina.yotrabajoconpecs.Preferences;
import cl.paulina.yotrabajoconpecs.R;
import cz.msebera.android.httpclient.Header;

public class Frase extends BottomSheetDialogFragment {
    public LinearLayout contentCaja;
    public Fragment fragment;
    private GridView gridView;
    private ArrayList id_frase;
    private ArrayList url;
    private ArrayList id_imagen;
    private ArrayList agregandoFrase;
    public ArrayList glosa;
    private ArrayList nombre_usuario;
    private ArrayList apellido_usuario;
    private ArrayList id_usuario;
    Bundle datos;
    BottomSheetBehavior sheetBehavior;
    public String IDUSUARIO;
    public String USUARIO;

    public Frase(){
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_layout, container, false);
        gridView = view.findViewById(R.id.listviewFrase);
        contentCaja = view.findViewById(R.id.contentCaja);

        id_frase = new ArrayList();
        url = new ArrayList();
        agregandoFrase = new ArrayList();
        id_imagen = new ArrayList();
        glosa = new ArrayList();
        nombre_usuario = new ArrayList();
        apellido_usuario = new ArrayList();
        id_usuario = new ArrayList();
        USUARIO = Preferences.obtenerPreferenceString(getContext(), Preferences.PREFERENCE_USUARIO_LOGIN);
        descargarUsuario();

        return view;
    }

    private void descargarUsuario(){
        nombre_usuario.clear();
        apellido_usuario.clear();
        id_usuario.clear();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("https://yotrabajoconpecs.ddns.net/query_usuario.php", new AsyncHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200){
                    try{
                        JSONArray jsonarray = new JSONArray(new String(responseBody));
                        for(int i = 0; i < jsonarray.length(); i++) {
                            nombre_usuario.add(jsonarray.getJSONObject(i).getString("nombre_usuario"));
                            apellido_usuario.add(jsonarray.getJSONObject(i).getString("apellido_usuario"));
                            id_usuario.add(jsonarray.getJSONObject(i).getString("id_usuario"));
                            String nombre_usuario_conespacios = nombre_usuario.get(i).toString() + " " + apellido_usuario.get(i).toString();
                            String nombre_usuario_sinespacios = nombre_usuario_conespacios.replace(" ", "");
                            if (USUARIO.equals(nombre_usuario_sinespacios)) {
                                IDUSUARIO = id_usuario.get(i).toString();
                                descargarDatos("https://yotrabajoconpecs.ddns.net/query_frase.php?idusuario=" + IDUSUARIO);
                            }
                        }
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getContext(), "Conexión fallida usuario", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void descargarDatos(String URL){
        id_frase.clear();
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(URL, new AsyncHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200){
                    progressDialog.dismiss();
                    try{
                        JSONArray jsonarray = new JSONArray(new String(responseBody));
                        for(int i = 0; i < jsonarray.length(); i++){
                            id_frase.add(jsonarray.getJSONObject(i).getString("url"));
                        }gridView.setAdapter(new CustomAdapter(getContext()));
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getContext(), "Conexión fallida frase", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class CustomAdapter extends BaseAdapter {
        Context ctx;
        LayoutInflater layoutInflater;
        LinearLayout TvImagenButton;

        @RequiresApi(api = Build.VERSION_CODES.M)
        public CustomAdapter(Context applicationContext){
            this.ctx = applicationContext;
            layoutInflater = (LayoutInflater)this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount(){
            return id_frase.size();
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
            ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.list_view_frase, null);
            TvImagenButton = (LinearLayout) viewGroup.findViewById(R.id.tv_imagen_frase);

            String frase = id_frase.get(position).toString();
            String split[] = frase.split(" ");
            for(int i = 0; i < split.length; i++) {
                ImageButton imagen = new ImageButton(getContext());
                imagen.setBackgroundResource(R.drawable.boton_rectangulo);
                imagen.setLayoutParams(new LinearLayout.LayoutParams(130, 130));
                imagen.setScaleType(ImageButton.ScaleType.FIT_CENTER);
                Picasso.get().load("https://yotrabajoconpecs.ddns.net/" + split[i]).into(imagen);
                TvImagenButton.addView(imagen);
            }
            glosa.clear();
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            AsyncHttpClient client = new AsyncHttpClient();
            Toast.makeText(getContext(), IDUSUARIO, Toast.LENGTH_SHORT).show();
            client.get("https://yotrabajoconpecs.ddns.net/query_frase2.php?idusuario=" + IDUSUARIO, new AsyncHttpResponseHandler() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    if(statusCode == 200){
                        progressDialog.dismiss();
                        try{
                            JSONArray jsonarray = new JSONArray(new String(responseBody));
                            for(int i = 0; i < jsonarray.length(); i++){
                                glosa.add(jsonarray.getJSONObject(i).getString("glosa"));
                            }
                        }catch(JSONException e){
                            e.printStackTrace();
                        }
                    }
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(getContext(), "Conexión fallida frase 2", Toast.LENGTH_SHORT).show();
                }
            });
            TvImagenButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragmento = new LibroPDCFragment();
                    datos = new Bundle();
                    datos.putString("url_frase", id_frase.get(position).toString());
                    datos.putString("glosa_frase", glosa.get(position).toString());
                    fragmento.setArguments(datos);
                    cambiarFragmento(fragmento);
                }
            });
            return viewGroup;
        }
    }

    public void cambiarFragmento(Fragment fragment){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
