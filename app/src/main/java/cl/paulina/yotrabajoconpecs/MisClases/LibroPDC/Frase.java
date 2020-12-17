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
import android.widget.Adapter;
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
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.tabs.TabLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

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
    private ArrayList fraseArray;
    private ArrayList categoria_frase;
    public String IDUSUARIO;
    public String USUARIO;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    /*
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_layout, container, false);
        gridView = view.findViewById(R.id.listviewFrase);
        contentCaja = view.findViewById(R.id.contentCaja);
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewLayout);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(new AdapterFrase(getActivity().getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0){
                    Toast.makeText(getContext(), "estoy en la frase consulta", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        id_frase = new ArrayList();
        url = new ArrayList();
        agregandoFrase = new ArrayList();
        id_imagen = new ArrayList();
        glosa = new ArrayList();
        nombre_usuario = new ArrayList();
        apellido_usuario = new ArrayList();
        id_usuario = new ArrayList();
        fraseArray = new ArrayList();
        USUARIO = Preferences.obtenerPreferenceString(getContext(), Preferences.PREFERENCE_USUARIO_LOGIN);

        //descargarUsuario();
        return view;
    }*/

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_layout, null);
        dialog.setContentView(view);
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
        fraseArray = new ArrayList();
        categoria_frase = new ArrayList();
        USUARIO = Preferences.obtenerPreferenceString(getContext(), Preferences.PREFERENCE_USUARIO_LOGIN);

        descargarUsuario();

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) view.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    String state = "";

                    switch (newState) {
                        case BottomSheetBehavior.STATE_DRAGGING: {
                            state = "DRAGGING";
                            break;
                        }
                        case BottomSheetBehavior.STATE_SETTLING: {
                            state = "SETTLING";
                            break;
                        }
                        case BottomSheetBehavior.STATE_EXPANDED: {
                            state = "EXPANDED";
                            break;
                        }
                        case BottomSheetBehavior.STATE_COLLAPSED: {
                            state = "COLLAPSED";
                            break;
                        }
                        case BottomSheetBehavior.STATE_HIDDEN: {
                            dismiss();
                            state = "HIDDEN";
                            break;
                        }
                    }

                    //Toast.makeText(getContext(), "Bottom Sheet State Changed to: " + state, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                }
            });
        }
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
        categoria_frase.clear();
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
                            categoria_frase.add(jsonarray.getJSONObject(i).getString("categoria"));
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

    private void AgregarFrecuenciaFrase(String URL){
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
            String categoria = categoria_frase.get(position).toString();
            String split[] = frase.split(" ");
            String splitcat[] = categoria.split(" ");
            for(int i = 0; i < split.length; i++) {
                ImageButton imagen = new ImageButton(getContext());
                if(splitcat[i].equals("3")){
                    imagen.setBackgroundResource(R.drawable.boton_rectangulo_verbo);
                }else if(splitcat[i].equals("2")){
                    imagen.setBackgroundResource(R.drawable.boton_rectangulo_sustantivo);
                }else if(splitcat[i].equals("4")){
                    imagen.setBackgroundResource(R.drawable.boton_rectangulo_adjetivo);
                }else{
                    imagen.setBackgroundResource(R.drawable.boton_rectangulo);
                }
                imagen.setLayoutParams(new LinearLayout.LayoutParams(130, 130));
                imagen.setScaleType(ImageButton.ScaleType.FIT_CENTER);
                Picasso.get().load("https://yotrabajoconpecs.ddns.net/" + split[i]).into(imagen);
                TvImagenButton.addView(imagen);
            }
            glosa.clear();
            fraseArray.clear();
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            AsyncHttpClient client = new AsyncHttpClient();
            //Toast.makeText(getContext(), IDUSUARIO, Toast.LENGTH_SHORT).show();
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
                                fraseArray.add(jsonarray.getJSONObject(i).getString("id_frase"));
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
                    MandarMensaje("marce36", "Maximiliano Ramirez Henriquez", "1", glosa.get(position).toString());
                    AgregarFrecuenciaFrase("https://yotrabajoconpecs.ddns.net/modificar_frase.php?frase=" + fraseArray.get(position).toString());
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

    private void MandarMensaje(String EMISOR, String NOMBRE, String RECEPTOR, String MENSAJE_ENVIAR) {
        //Toast.makeText(Login.this, "entre a SubirToken", Toast.LENGTH_SHORT).show();
        HashMap<String, String> hashMapToken = new HashMap<>();
        hashMapToken.put("emisor", EMISOR);
        hashMapToken.put("nombrecompleto", NOMBRE);
        hashMapToken.put("receptor", RECEPTOR);
        hashMapToken.put("mensaje", MENSAJE_ENVIAR);
        //Toast.makeText(getContext(), EMISOR + NOMBRE + RECEPTOR + MENSAJE_ENVIAR + "" + IP_MENSAJE, Toast.LENGTH_SHORT).show();

        JsonObjectRequest solicitud = new JsonObjectRequest(Request.Method.POST, "https://yotrabajoconpecs.ddns.net/Enviar_Mensajes.php", new JSONObject(hashMapToken), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Toast.makeText(getContext(), response.getString("resultado"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Ocurrió un error", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(solicitud);
    }
}
