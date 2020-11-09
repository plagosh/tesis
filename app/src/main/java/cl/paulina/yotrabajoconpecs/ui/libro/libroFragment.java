package cl.paulina.yotrabajoconpecs.ui.libro;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cl.paulina.yotrabajoconpecs.R;
import cl.paulina.yotrabajoconpecs.ServicioAPI.PreferenciasCompartidas;
import cl.paulina.yotrabajoconpecs.ui.busqueda_pictograma_empleador.busquedaPictogramaFragment;
import cl.paulina.yotrabajoconpecs.ui.busqueda_pictograma_pdc.busquedaPictogramapdc2Fragment;
import cl.paulina.yotrabajoconpecs.ui.busqueda_pictograma_pdc.busquedaPictogramapdc3Fragment;
import cl.paulina.yotrabajoconpecs.ui.busqueda_pictograma_pdc.busquedaPictogramapdcFragment;
import cl.paulina.yotrabajoconpecs.ui.busqueda_pictograma_pdc.fraseFragment;
import cl.paulina.yotrabajoconpecs.ui.principal.panelPrincipalFragment;
import cz.msebera.android.httpclient.Header;

public class libroFragment extends Fragment {
    public FloatingActionMenu actionMenu;
    public FloatingActionButton fabSend, fabDelete;
    ImageButton button1, button2, button3;
    ImageView derecha1, izquierda1, derecha2, izquierda2;
    TextView textouno, textodos;
    ArrayList pictos, pos, pictos2;
    PreferenciasCompartidas pc;
    AlertDialog.Builder builder;
    int Sapo = 0;
    public int sapo2 = 0;
    public int sapo3 = 0;
    private ArrayList url, urlita, urlota, categoria, categories, nombre, nombrecito, nombresote, botonuno, botondos;
    String key = "url";
    String key2 = "nombre_imagen";
    LinearLayout contenido, caja, layout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.pdc_1, container, false);
        actionMenu = vista.findViewById(R.id.fab_tirafrase);
        actionMenu.setClosedOnTouchOutside(true);
        Bundle datosRecibido = getArguments();
        textouno = vista.findViewById(R.id.texto1);
        textodos = vista.findViewById(R.id.texto2);
        button1 = vista.findViewById(R.id.imagebutton1);
        button2 = vista.findViewById(R.id.imagebutton2);
        button3 = vista.findViewById(R.id.frasescompletas);
        izquierda1 = vista.findViewById(R.id.izquierdabutton1);
        izquierda2 = vista.findViewById(R.id.izquierdabutton2);
        derecha1 = vista.findViewById(R.id.derechabutton1);
        derecha2 = vista.findViewById(R.id.derechabutton2);
        fabDelete = vista.findViewById(R.id.fab_tirafrase_eliminar);
        fabSend = vista.findViewById(R.id.fab_tirafrase_enviar);
        nombre = new ArrayList();
        nombrecito = new ArrayList();
        nombresote = new ArrayList();
        pictos = new ArrayList();
        pictos2 = new ArrayList();
        url = new ArrayList();
        urlita = new ArrayList();
        urlota = new ArrayList();
        categoria = new ArrayList();
        pos = new ArrayList();
        botonuno = new ArrayList();
        botondos = new ArrayList();
        botonuno.clear();
        botondos.clear();
        botonuno.add(0);
        botondos.add(0);
        pos.add(0);
        pictos.clear();
        pictos2.clear();
        categories = new ArrayList();
        pc = new PreferenciasCompartidas();
        caja = new LinearLayout(getContext());
        caja.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        caja.setOrientation(LinearLayout.HORIZONTAL);
        caja.setOrientation(LinearLayout.VERTICAL);
        pc.loadData(pictos, getContext(), key);
        pc.loadData(pictos2, getContext(), key2);
        descargarDatos();
        //sustantivos
        descargarDatitos();
        //adjetivos
        descargarDatotes();
        descargarSave();

        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button1.setImageDrawable(null);
                textouno.setText("");
                botonuno.add(0);
                izquierda2.setVisibility(View.INVISIBLE);
                derecha2.setVisibility(View.INVISIBLE);
                button2.setImageDrawable(null);
                textodos.setText("");
                botondos.add(0);
            }
        });
        derecha1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                derecha2.setVisibility(View.VISIBLE);
                izquierda1.setVisibility(View.VISIBLE);
                sapo2++;
                Picasso.get().load("https://yotrabajoconpecs.ddns.net/" + url.get(sapo2)).into(button1);
                textouno.setText(nombre.get(sapo2).toString());
                botonuno.add(categoria.get(sapo2));
                String consulta = "INSERT INTO `automata` (`idautomata`, `fecha`, `categoria`) VALUES (NULL, current_timestamp(), '" + categoria.get(sapo2).toString() + "');";
                ejecutarservicio("https://yotrabajoconpecs.ddns.net/save.php", consulta);
                //Toast.makeText(getContext(), "n: " + sapo2, Toast.LENGTH_SHORT).show();
            }
        });
        izquierda1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sapo2 > 0){
                    sapo2--;
                    Picasso.get().load("https://yotrabajoconpecs.ddns.net/" + url.get(sapo2)).into(button1);
                    textouno.setText(nombre.get(sapo2).toString());
                    botonuno.add(categoria.get(sapo2));
                    String consulta = "INSERT INTO `automata` (`idautomata`, `fecha`, `categoria`) VALUES (NULL, current_timestamp(), '" + categoria.get(sapo2).toString() + "');";
                    ejecutarservicio("https://yotrabajoconpecs.ddns.net/save.php", consulta);
                    //Toast.makeText(getContext(), "n: " + sapo2, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), "No hay más pictos", Toast.LENGTH_SHORT).show();
                }
            }
        });
        derecha2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                izquierda2.setVisibility(View.VISIBLE);
                if(categories.get(categories.size()-1) == "3") {
                    sapo3++;
                    Picasso.get().load("https://yotrabajoconpecs.ddns.net/" + urlita.get(sapo3)).into(button2);
                    textodos.setText(nombrecito.get(sapo3).toString());
                    botondos.add(nombrecito.get(sapo3));
                }else{
                    sapo3++;
                    Picasso.get().load("https://yotrabajoconpecs.ddns.net/" + urlota.get(sapo3)).into(button2);
                    textodos.setText(nombresote.get(sapo3).toString());
                    botondos.add(nombrecito.get(sapo3));
                }
            }
        });
        izquierda2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(categories.get(categories.size()-1) == "3") {
                    if(sapo3 > 0){
                        sapo3--;
                        Picasso.get().load("https://yotrabajoconpecs.ddns.net/" + urlita.get(sapo3)).into(button2);
                        textodos.setText(nombrecito.get(sapo3).toString());
                        botondos.add(nombrecito.get(sapo3));
                    }else{
                        Toast.makeText(getContext(), "No hay más pictos", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    if(sapo3 > 0){
                        Picasso.get().load("https://yotrabajoconpecs.ddns.net/" + urlota.get(sapo3)).into(button2);
                        textodos.setText(nombresote.get(sapo3).toString());
                        botondos.add(nombresote.get(sapo3));
                        sapo3--;
                    }else{
                        Toast.makeText(getContext(), "No hay más pictos", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new busquedaPictogramapdcFragment();
                fragment.setTargetFragment(libroFragment.this,1);
                cambiarFragmento(fragment);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(categories.get(categories.size()-1) == "3"){
                    Fragment fragment = new busquedaPictogramapdc2Fragment();
                    fragment.setTargetFragment(libroFragment.this,1);
                    cambiarFragmento(fragment);
                }else{
                    Fragment fragment = new busquedaPictogramapdc2Fragment();
                    fragment.setTargetFragment(libroFragment.this,1);
                    cambiarFragmento(fragment);
                }

            }
        });
        if(datosRecibido != null) {
            if(String.valueOf(datosRecibido.getString("url")) != String.valueOf(0)){
                if(String.valueOf(datosRecibido.getString("nombre_imagen")) != String.valueOf(0)) {
                    String id = datosRecibido.getString("url");
                    String name = datosRecibido.getString("nombre_imagen");
                    //String cat = datosRecibido.getString("categoria_imagen");
                    //Log.e("DatoRecibido: ", String.valueOf(id));
                    pictos.add(id);
                    pictos2.add(name);
                    Log.e("DatoRecibido: ", "La lista pictos tiene ->" + pictos.size() + " pictos2: " + pictos2.size());
                    pc.saveData(pictos, getContext(), key);
                    pc.saveData(pictos2, getContext(), key2);
                    Sapo = 1;
                }
            }
            datosRecibido.clear();
        }
        /*
        if (pictos.size() > 0 && pictos2.size() > 0) {
            //Log.e("GenerarFotoTamañoLista ", String.valueOf(pictos.size()));
            for (int i = 0; i < pictos.size(); i++) {
                if(pictos.get(i) != "" && pictos2.get(i) != "") {
                    Picasso.get().load("https://yotrabajoconpecs.ddns.net/" + pictos.get(i)).into(button1);
                    textouno.setText(pictos2.get(i).toString());
                }
            }
        }
         */
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new fraseFragment();
                fragment.setTargetFragment(libroFragment.this,1);
                cambiarFragmento(fragment);
            }
        });
        fabSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ultimobotonuno = botonuno.get(botonuno.size()-1).toString();
                String ultimobotondos = botondos.get(botondos.size()-1).toString();
                if(ultimobotonuno == "0"){
                    Toast.makeText(getContext(), "No se puede enviar, elija verbo o sustantivo", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), "Mensaje enviado", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return vista;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        OnBackPressedCallback callback = new OnBackPressedCallback (true){
            @Override
            public void handleOnBackPressed() {
                Fragment fragment = new panelPrincipalFragment();
                if (pictos.size() == 0 || Sapo == 0){
                    cambiarFragmento(fragment);
                }if(Sapo == 1){
                    builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Tienes nuevos pictogramas agregados.").setTitle("Cambios sin guardar.");
                    builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            pc.saveData(pictos,getContext(),key);
                            cambiarFragmento(fragment);
                            Toast.makeText(getContext(), "Pictogramas guardados", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setNegativeButton("No guardar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            pc.deleteData(pictos);
                            pc.saveData(pictos, getContext(), key);
                            cambiarFragmento(fragment);
                            Toast.makeText(getContext(), "Pictogramas borrados", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Log.e("AlertDialog","Acceso al metodo.");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
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

    private void descargarDatos(){
        url.clear();
        categoria.clear();
        nombre.clear();
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
                            url.add(jsonarray.getJSONObject(i).getString("url"));
                            nombre.add(jsonarray.getJSONObject(i).getString("nombre_imagen"));
                            categoria.add(jsonarray.getJSONObject(i).getString("categoria_imagen"));
                        }
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

    private void descargarDatitos(){
        //sustantivos
        urlita.clear();
        nombrecito.clear();
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("https://yotrabajoconpecs.ddns.net/query1.php", new AsyncHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200){
                    progressDialog.dismiss();
                    try{
                        JSONArray jsonarray = new JSONArray(new String(responseBody));
                        for(int i = 0; i < jsonarray.length(); i++){
                            urlita.add(jsonarray.getJSONObject(i).getString("url"));
                            nombrecito.add(jsonarray.getJSONObject(i).getString("nombre_imagen"));
                        }
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

    private void descargarDatotes(){
        //sustantivos
        urlota.clear();
        nombresote.clear();
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("https://yotrabajoconpecs.ddns.net/query5.php", new AsyncHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200){
                    progressDialog.dismiss();
                    try{
                        JSONArray jsonarray = new JSONArray(new String(responseBody));
                        for(int i = 0; i < jsonarray.length(); i++){
                            urlota.add(jsonarray.getJSONObject(i).getString("url"));
                            nombresote.add(jsonarray.getJSONObject(i).getString("nombre_imagen"));
                        }
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

    private void descargarSave(){
        categories.clear();
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("https://yotrabajoconpecs.ddns.net/query6.php", new AsyncHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200){
                    progressDialog.dismiss();
                    try{
                        JSONArray jsonarray = new JSONArray(new String(responseBody));
                        for(int i = 0; i < jsonarray.length(); i++){
                            categories.add(jsonarray.getJSONObject(i).getString("categoria"));
                        }
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

    private void ejecutarservicio(String URL, String dato){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getContext(), "OPERACION EXITOSA", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
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
