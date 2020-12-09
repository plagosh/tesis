package cl.paulina.yotrabajoconpecs.MisClases.LibroPDC;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cl.paulina.yotrabajoconpecs.R;
import cl.paulina.yotrabajoconpecs.MisClases.LibroEmpleador.MensajeDeTexto;
import cz.msebera.android.httpclient.Header;

public class LibroPDCFragment extends Fragment {
    public static final String MENSAJE = "MENSAJE";
    private BroadcastReceiver bR;
    Bundle recibir_frase;
    private RecyclerView rv;
    private List<MensajeDeTexto> mensajedetexto;
    private MessageAdapterFragment adapter;
    private Button bTEnviarMensaje, bTEnviarFrase;
    private String MENSAJE_ENVIAR = "";
    private String EMISOR;
    private String NOMBRE;
    private String RECEPTOR;
    public int sapo2 = 0;
    public int sapo3 = 0;
    public String categoria_verbo;
    public String ultimaCategoria;
    public String nDatos, nDatitos, nDatotes, nURL, nURLita, nURLota, id;
    public ArrayList nombre_login, correo_login, pictos, url, urlita, urlota, categoria, categories, nombre, nombrecito, nombresote, botonuno, botondos, pos, menssage, urlQueryCero, urls, celular;
    private static final String IP_MENSAJE = "https://yotrabajoconpecs.ddns.net/Enviar_Mensajes.php";
    ImageButton imagen1, imagen2, arriba1, arriba2, abajo1, abajo2;
    Bundle datos;
    private ArrayList descargar_mensaje;
    private ArrayList descargar_tipo;
    private ArrayList descargar_hora;
    private ArrayList descargar_url;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.pdc_mensajeria, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Chat");
        rv = vista.findViewById(R.id.recyclerview);
        imagen1 = vista.findViewById(R.id.ImagenButtonUno);
        imagen2 = vista.findViewById(R.id.ImagenButtonDos);
        arriba1 = vista.findViewById(R.id.ArribaButtonUno);
        arriba2 = vista.findViewById(R.id.ArribaButtonDos);
        abajo1 = vista.findViewById(R.id.AbajoButtonUno);
        abajo2 = vista.findViewById(R.id.AbajoButton2);
        bTEnviarFrase = vista.findViewById(R.id.bTEnviarFrase);
        datos = new Bundle();
        mensajedetexto = new ArrayList<>();
        nombre_login = new ArrayList();
        correo_login = new ArrayList();
        pictos = new ArrayList();
        nombre = new ArrayList();
        nombrecito = new ArrayList();
        nombresote = new ArrayList();
        pictos = new ArrayList();
        url = new ArrayList();
        urlita = new ArrayList();
        urlota = new ArrayList();
        categoria = new ArrayList();
        categories = new ArrayList();
        pos = new ArrayList();
        botonuno = new ArrayList();
        botondos = new ArrayList();
        celular = new ArrayList();
        botonuno.clear();
        botondos.clear();
        botonuno.add(0);
        botondos.add(0);
        pos.add(0);
        menssage = new ArrayList();
        urlQueryCero = new ArrayList();
        urls = new ArrayList();
        descargar_mensaje = new ArrayList();
        descargar_tipo = new ArrayList();
        descargar_hora = new ArrayList();
        descargar_url = new ArrayList();

        recibir_frase = getArguments();
        Bundle recibir_verbo = getArguments();
        Bundle recibir_glosa = getArguments();
        Bundle recibir_sust = getArguments();
        Bundle recibir_adj = getArguments();

        RECEPTOR = "1";
        /*
        if(recibir_verbo != null) {
            if (String.valueOf(recibir_verbo.getString("url_verbo")) != String.valueOf(0)) {
                String url_verbo = recibir_verbo.getString("url_verbo");
                categoria_verbo = recibir_verbo.getString("categoria_imagen_verbo");
                Toast.makeText(getContext(), categoria_verbo, Toast.LENGTH_SHORT).show();
                Picasso.get().load("https://yotrabajoconpecs.ddns.net/" + url_verbo).into(imagen1);
            }
            recibir_verbo.clear();
        }

        if(categoria_verbo != null){
            if(categoria_verbo.equals("3")){
                imagen2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Fragment fragment = new busquedaPictogramapdc2Fragment();
                        cambiarFragmento(fragment);
                    }
                });
            }else{
                imagen2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Fragment fragment = new busquedaPictogramapdc3Fragment();
                        cambiarFragmento(fragment);
                    }
                });
            }
        }

        if(recibir_sust != null) {
            if (String.valueOf(recibir_sust.getString("url_sust")) != String.valueOf(0)) {
                String url_sust = recibir_sust.getString("url_sust");
                Picasso.get().load("https://yotrabajoconpecs.ddns.net/" + url_sust).into(imagen2);
            }
            recibir_sust.clear();
        }

        if(recibir_adj != null) {
            if (String.valueOf(recibir_adj.getString("url_adj")) != String.valueOf(0)) {
                String url_adj = recibir_adj.getString("url_adj");
                Picasso.get().load("https://yotrabajoconpecs.ddns.net/" + url_adj).into(imagen2);
            }
            recibir_adj.clear();
        }
        */

        bTEnviarFrase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Frase frase = new Frase();
                frase.show(getParentFragmentManager(), "TAG");
            }
        });

        descargar();

        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(lm);
        adapter = new MessageAdapterFragment(mensajedetexto, getContext());
        rv.setAdapter(adapter);
        bTEnviarMensaje = vista.findViewById(R.id.bTEnviarMensaje);
        if(recibir_frase != null && recibir_glosa != null) {
            if (String.valueOf(recibir_frase.getString("url_frase")) != String.valueOf(0)) {
                if (String.valueOf(recibir_glosa.getString("glosa_frase")) != String.valueOf(0)) {
                    String url_frase = recibir_frase.getString("url_frase");
                    String glosa_frase = recibir_glosa.getString("glosa_frase");
                    Date dt = new Date();
                    int hours = dt.getHours();
                    int minutes = dt.getMinutes();
                    String curTime = hours + ":" + minutes + ", Hoy";
                    MENSAJE_ENVIAR = glosa_frase;
                    NOMBRE = "Maximiliano Ramirez Henriquez";
                    EMISOR = "marce36";
                    MandarMensaje();
                }
            }
            recibir_frase.clear();
        }
        //todos
        descargarDatos();
        //sustantivos
        descargarDatitos();
        //adjetivos
        descargarDatotes();

        arriba1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abajo1.setVisibility(View.VISIBLE);
                arriba2.setVisibility(View.VISIBLE);
                imagen1.setBackgroundResource(R.drawable.boton_rectangulo);
                //Toast.makeText(getContext(), sapo2 + "", Toast.LENGTH_SHORT).show();
                //Toast.makeText(getContext(), url.get(sapo2).toString(), Toast.LENGTH_SHORT).show();
                String url_rescatada = url.get(sapo2).toString();
                Picasso.get().load("https://yotrabajoconpecs.ddns.net/" + url_rescatada).into(imagen1);
                //imagen1.setStroke(8, getResources().getColor(R.color.colorWhite));
                nDatos = nombre.get(sapo2).toString();
                nURL = url.get(sapo2).toString();
                //Toast.makeText(getContext(), nDatos, Toast.LENGTH_SHORT).show();
                imagen1.setScaleType(ImageButton.ScaleType.FIT_CENTER);
                botonuno.add(categoria.get(sapo2));
                //Toast.makeText(getContext(), sapo2 + "", Toast.LENGTH_SHORT).show();
                sapo2++;
                String consulta = "INSERT INTO `automata` (`idautomata`, `fecha`, `categoria`) VALUES (NULL, current_timestamp(), '" + categoria.get(sapo2).toString() + "');";
                ejecutarservicio("https://yotrabajoconpecs.ddns.net/save.php", consulta);
            }
        });
        abajo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sapo2 > 0){
                    sapo2--;
                    imagen1.setBackgroundResource(R.drawable.boton_rectangulo);
                    Picasso.get().load("https://yotrabajoconpecs.ddns.net/" + url.get(sapo2).toString()).into(imagen1);
                    nDatos = nombre.get(sapo2).toString();
                    nURL = url.get(sapo2).toString();
                    //Toast.makeText(getContext(), nDatos, Toast.LENGTH_SHORT).show();
                    imagen1.setScaleType(ImageButton.ScaleType.FIT_CENTER);
                    botonuno.add(categoria.get(sapo2));
                    String consulta = "INSERT INTO `automata` (`idautomata`, `fecha`, `categoria`) VALUES (NULL, current_timestamp(), '" + categoria.get(sapo2).toString() + "');";
                    ejecutarservicio("https://yotrabajoconpecs.ddns.net/save.php", consulta);
                    //Toast.makeText(getContext(), "n: " + sapo2, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), "No hay más pictos", Toast.LENGTH_SHORT).show();
                }
            }
        });
        arriba2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abajo2.setVisibility(View.VISIBLE);
                if(ultimaCategoria == "3") {
                    sapo3++;
                    imagen2.setBackgroundResource(R.drawable.boton_rectangulo);
                    Picasso.get().load("https://yotrabajoconpecs.ddns.net/" + urlita.get(sapo3).toString()).into(imagen2);
                    nDatitos = nombrecito.get(sapo3).toString();
                    nURLita = urlita.get(sapo3).toString();
                    //Toast.makeText(getContext(), nDatitos, Toast.LENGTH_SHORT).show();
                    imagen2.setScaleType(ImageButton.ScaleType.FIT_CENTER);

                    botondos.add(nombrecito.get(sapo3));
                }else{
                    sapo3++;
                    imagen2.setBackgroundResource(R.drawable.boton_rectangulo);
                    Picasso.get().load("https://yotrabajoconpecs.ddns.net/" + urlota.get(sapo3).toString()).into(imagen2);
                    nDatotes = nombresote.get(sapo3).toString();
                    nURLota = urlota.get(sapo3).toString();
                    //Toast.makeText(getContext(), nDatotes, Toast.LENGTH_SHORT).show();
                    imagen2.setScaleType(ImageButton.ScaleType.FIT_CENTER);
                    botondos.add(nombrecito.get(sapo3));
                }
            }
        });
        //automata
        descargarSave();
        abajo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ultimaCategoria == "3") {
                    if(sapo3 > 0){
                        sapo3--;
                        imagen2.setBackgroundResource(R.drawable.boton_rectangulo);
                        Picasso.get().load("https://yotrabajoconpecs.ddns.net/" + urlita.get(sapo3).toString()).into(imagen2);
                        nDatitos = nombrecito.get(sapo3).toString();
                        nURLita = urlita.get(sapo3).toString();
                        //Toast.makeText(getContext(), nDatitos, Toast.LENGTH_SHORT).show();
                        imagen2.setScaleType(ImageButton.ScaleType.FIT_CENTER);
                        botondos.add(nombrecito.get(sapo3));
                    }else{
                        Toast.makeText(getContext(), "No hay más pictos", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    if(sapo3 > 0){
                        imagen2.setBackgroundResource(R.drawable.boton_rectangulo);
                        Picasso.get().load("https://yotrabajoconpecs.ddns.net/" + urlota.get(sapo3).toString()).into(imagen2);
                        nDatotes = nombresote.get(sapo3).toString();
                        nURLota = urlota.get(sapo3).toString();
                        //Toast.makeText(getContext(), nDatotes, Toast.LENGTH_SHORT).show();
                        imagen2.setScaleType(ImageButton.ScaleType.FIT_CENTER);
                        botondos.add(nombresote.get(sapo3));
                        sapo3--;
                    }else{
                        Toast.makeText(getContext(), "No hay más pictos", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        /*
        imagen1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new busquedaPictogramapdcFragment();
                cambiarFragmento(fragment);
            }
        });
        */
        bTEnviarMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sustantivo y 3 es un verbo
                celular.clear();
                String nMensaje;
                String nMURL;
                if(ultimaCategoria == "2"){
                    nMensaje = nDatitos;
                    nMURL = nURLita;
                }else{
                    nMensaje = nDatotes;
                    nMURL = nURLota;
                }

                Date dt = new Date();
                int hours = dt.getHours();
                int minutes = dt.getMinutes();
                String curTime = hours + ":" + minutes;
                //para enviar el mensaje accedemos al método.
                String mensaje = "yo quiero " + nDatos + " " + nMensaje;
                String mensajedos = "repo/img/2617.png " + "repo/img/5441.png " + nURL + " " + nMURL;
                MENSAJE_ENVIAR = mensaje;
                MandarMensaje();
                CreateMensaje(mensajedos, mensaje, curTime, 1);
            }
        });

        bR = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String mensaje = intent.getStringExtra("key_mensaje");
                String url = intent.getStringExtra("key_url");
                String hora = intent.getStringExtra("key_hora");
                String horaParametros[] = hora.split("\\,");
                String emisor = intent.getStringExtra("key_emisor_PHP");
                if(emisor.equals(RECEPTOR)){
                    CreateMensaje(url, mensaje, horaParametros[0], 2);
                }
            }
        };

        return vista;
    }

    private void MandarMensaje() {
        //Toast.makeText(Login.this, "entre a SubirToken", Toast.LENGTH_SHORT).show();
        HashMap<String, String> hashMapToken = new HashMap<>();
        hashMapToken.put("emisor", EMISOR);
        hashMapToken.put("nombrecompleto", NOMBRE);
        hashMapToken.put("receptor", RECEPTOR);
        hashMapToken.put("mensaje", MENSAJE_ENVIAR);
        //Toast.makeText(getContext(), EMISOR + NOMBRE + RECEPTOR + MENSAJE_ENVIAR + "" + IP_MENSAJE, Toast.LENGTH_SHORT).show();

        JsonObjectRequest solicitud = new JsonObjectRequest(Request.Method.POST, IP_MENSAJE, new JSONObject(hashMapToken), new Response.Listener<JSONObject>() {
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

    public void CreateMensaje(String id, String mensaje, String hora, int tipoDeMensaje) {
        MensajeDeTexto mensajeDeTextoAuxiliar = new MensajeDeTexto();
        mensajeDeTextoAuxiliar.setId(id);
        mensajeDeTextoAuxiliar.setMensaje(mensaje);
        mensajeDeTextoAuxiliar.setTipoMensaje(tipoDeMensaje);
        mensajeDeTextoAuxiliar.setHoraDelMensaje(hora);
        mensajedetexto.add(mensajeDeTextoAuxiliar);
        adapter.notifyDataSetChanged();
        setScrollbarChat();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(bR);
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(bR, new IntentFilter(MENSAJE));
    }

    public void setScrollbarChat() {
        rv.scrollToPosition(adapter.getItemCount() - 1);
    }

    private void descargar() {
        nombre_login.clear();
        correo_login.clear();
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("https://yotrabajoconpecs.ddns.net/query_loginreciclado.php", new AsyncHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    progressDialog.dismiss();
                    try {
                        JSONArray jsonarray = new JSONArray(new String(responseBody));
                        for (int i = 0; i < jsonarray.length(); i++) {
                            nombre_login.add(jsonarray.getJSONObject(i).getString("nombres"));
                            correo_login.add(jsonarray.getJSONObject(i).getString("correo"));
                        }
                        EMISOR = correo_login.get(correo_login.size()-1).toString();
                        NOMBRE = nombre_login.get(nombre_login.size()-1).toString();
                        //Toast.makeText(getContext(), EMISOR, Toast.LENGTH_SHORT).show();
                        DescargarMensajes("https://yotrabajoconpecs.ddns.net/query_mensajes.php?usuario=" + EMISOR);
                    } catch (JSONException e) {
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
                Toast.makeText(getContext(), "Conexión fallida", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getContext(), "Conexión fallida", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getContext(), "Conexión fallida", Toast.LENGTH_SHORT).show();
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
                        ultimaCategoria = categories.get(categories.size()-1).toString();
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

    private void DescargarMensajes(String URL){
        descargar_mensaje.clear();
        descargar_tipo.clear();
        descargar_hora.clear();
        descargar_url.clear();
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
                            descargar_mensaje.add(jsonarray.getJSONObject(i).getString("mensaje"));
                            descargar_tipo.add(jsonarray.getJSONObject(i).getString("tipo_mensaje"));
                            descargar_hora.add(jsonarray.getJSONObject(i).getString("hora_del_mensaje"));
                            descargar_url.add(jsonarray.getJSONObject(i).getString("url"));
                            String curTime = descargar_hora.get(i).toString();
                            String mensaje = descargar_mensaje.get(i).toString();
                            String mensajedos = descargar_url.get(i).toString();
                            int tipo = Integer.parseInt(descargar_tipo.get(i).toString());

                            CreateMensaje(mensajedos, mensaje, curTime, tipo);
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

    public void cambiarFragmento(Fragment fragment){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
