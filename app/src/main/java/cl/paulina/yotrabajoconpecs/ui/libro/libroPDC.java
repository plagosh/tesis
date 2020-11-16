package cl.paulina.yotrabajoconpecs.ui.libro;
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
import androidx.fragment.app.Fragment;
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
import cl.paulina.yotrabajoconpecs.ui.busqueda_pictograma_empleador.MensajeDeTexto;
import cl.paulina.yotrabajoconpecs.ui.busqueda_pictograma_pdc.examplebuttonsheetdialog;
import cl.paulina.yotrabajoconpecs.ui.busqueda_pictograma_pdc.messageAdapter;
import cz.msebera.android.httpclient.Header;

public class libroPDC extends Fragment {
    public static final String MENSAJE = "MENSAJE";
    private BroadcastReceiver bR;
    private RecyclerView rv;
    private List<MensajeDeTexto> mensajedetexto;
    private messageAdapter adapter;
    private Button bTEnviarMensaje, bTEnviarFrase;
    private String MENSAJE_ENVIAR = "";
    private String EMISOR;
    private String NOMBRE;
    private String RECEPTOR;
    public String variableUno, variableDos, variableTres, variableCuatro, palabra, dato;
    public int sapo2 = 0;
    public int sapo3 = 0;
    public String ultimaCategoria;
    public String nDatos, nDatitos, nDatotes, nURL, nURLita, nURLota, id;
    public ArrayList nombre_login, correo_login, pictos, url, urlita, urlota, categoria, categories, nombre, nombrecito, nombresote, botonuno, botondos, pos, menssage, urlQueryCero, nombreQueryCero, urls, celular;
    private static final String IP_MENSAJE = "https://yotrabajoconpecs.ddns.net/Enviar_Mensajes.php";
    ImageButton imagen1, imagen2, arriba1, arriba2, abajo1, abajo2;
    Bundle datos;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.pdc_mensajeria, container, false);

        rv = vista.findViewById(R.id.recyclerview);
        //rvbr = vista.findViewById(R.id.recyclerview);
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
        nombreQueryCero = new ArrayList();
        urls = new ArrayList();

        RECEPTOR = "1";

        bTEnviarFrase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                examplebuttonsheetdialog fragment = new examplebuttonsheetdialog();
                fragment.show(getActivity().getSupportFragmentManager(), "TAG");
                /*
                Fragment fragment = new ExampleBottomSheetDialog();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, fragment);
                transaction.addToBackStack(null);
                transaction.commit();*/
            }
        });

        descargar();
        //todos
        descargarDatos();
        //sustantivos
        descargarDatitos();
        //adjetivos
        descargarDatotes();

        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(lm);
        adapter = new messageAdapter(mensajedetexto, getContext());
        rv.setAdapter(adapter);
        bTEnviarMensaje = vista.findViewById(R.id.bTEnviarMensaje);


        arriba1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abajo1.setVisibility(View.VISIBLE);
                arriba2.setVisibility(View.VISIBLE);
                sapo2++;
                Picasso.get().load("https://yotrabajoconpecs.ddns.net/" + url.get(sapo2)).into(imagen1);
                //imagen1.setStroke(8, getResources().getColor(R.color.colorWhite));
                nDatos = nombre.get(sapo2).toString();
                nURL = url.get(sapo2).toString();
                //Toast.makeText(getContext(), nDatos, Toast.LENGTH_SHORT).show();
                imagen1.setScaleType(ImageButton.ScaleType.FIT_CENTER);
                botonuno.add(categoria.get(sapo2));
                String consulta = "INSERT INTO `automata` (`idautomata`, `fecha`, `categoria`) VALUES (NULL, current_timestamp(), '" + categoria.get(sapo2).toString() + "');";
                ejecutarservicio("https://yotrabajoconpecs.ddns.net/save.php", consulta);
            }
        });
        abajo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sapo2 > 0){
                    sapo2--;
                    Picasso.get().load("https://yotrabajoconpecs.ddns.net/" + url.get(sapo2)).into(imagen1);
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
                    Picasso.get().load("https://yotrabajoconpecs.ddns.net/" + urlita.get(sapo3)).into(imagen2);
                    nDatitos = nombrecito.get(sapo3).toString();
                    nURLita = urlita.get(sapo3).toString();
                    //Toast.makeText(getContext(), nDatitos, Toast.LENGTH_SHORT).show();
                    imagen2.setScaleType(ImageButton.ScaleType.FIT_CENTER);
                    botondos.add(nombrecito.get(sapo3));
                }else{
                    sapo3++;
                    Picasso.get().load("https://yotrabajoconpecs.ddns.net/" + urlota.get(sapo3)).into(imagen2);
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
                        Picasso.get().load("https://yotrabajoconpecs.ddns.net/" + urlita.get(sapo3)).into(imagen2);
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
                        Picasso.get().load("https://yotrabajoconpecs.ddns.net/" + urlota.get(sapo3)).into(imagen2);
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


        bTEnviarMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sustantivo y 3 es un verbo
                nombreQueryCero.clear();
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
        /*Toast.makeText(getContext(), EMISOR, Toast.LENGTH_SHORT).show();
        Toast.makeText(getContext(), NOMBRE, Toast.LENGTH_SHORT).show();
        Toast.makeText(getContext(), RECEPTOR, Toast.LENGTH_SHORT).show();
        Toast.makeText(getContext(), MENSAJE_ENVIAR, Toast.LENGTH_SHORT).show();*/
        //Toast.makeText(Login.this, emailemail + FirebaseInstanceId.getInstance().getToken(), Toast.LENGTH_SHORT).show();

        JsonObjectRequest solicitud = new JsonObjectRequest(Request.Method.POST, IP_MENSAJE, new JSONObject(hashMapToken), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Toast.makeText(getContext(), "Se ha iniciado sesión: " + response.getString("resultado"), Toast.LENGTH_SHORT).show();
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

    private void descargarDatosQueryCero(String consulta){
        JsonObjectRequest solicitud = new JsonObjectRequest("https://yotrabajoconpecs.ddns.net/query0.php?consulta=" + consulta, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String responseBody = response.getString("resultado");
                    JSONArray jsonarray = new JSONArray(new String(responseBody));
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject js = jsonarray.getJSONObject(i);
                        dato = js.getString("url");
                    }
                } catch (JSONException e) {
                    Toast.makeText(getContext(), "Ocurrió un error al descomponer el JSON", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Ocurrió un error, por favor contactese con el administrador", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(solicitud);
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

    private void consultarURL(String URL, String url, String palabra){
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
                parametros.put("nombre", palabra);
                parametros.put("url", url);
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}