package cl.paulina.yotrabajoconpecs.MisClases.panel;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
import java.util.Calendar;
import java.util.Date;

import cl.paulina.yotrabajoconpecs.Preferences;
import cl.paulina.yotrabajoconpecs.R;
import cz.msebera.android.httpclient.Header;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class PanelPDCFragment extends Fragment {
    TextView tvdia, tvano, reloj;
    public Fragment fragment;
    private ProgressBar pb;
    private int mProgressStatus = 0;
    private ImageButton checkTarea;
    Bundle datos;
    Bundle datosRecibidos;
    private ArrayList id, fecha_inicio, fecha_termino, hora_inicio, hora_termino, dia, url, id_tarea;
    private ImageButton tvpanel, tvmes;
    private Handler mHandler = new Handler();
    Date date = new Date();
    DateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
    String horan = formatoHora.format(date);
    DateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
    Calendar fecha = Calendar.getInstance();
    int tv_ano = fecha.get(Calendar.YEAR);
    int tv_mes = fecha.get(Calendar.MONTH) + 1;
    int tv_dia = fecha.get(Calendar.DAY_OF_MONTH);
    int hora = fecha.get(Calendar.HOUR_OF_DAY);
    int minuto = fecha.get(Calendar.MINUTE);
    int segundo = fecha.get(Calendar.SECOND);
    int aumentardia = 1;
    private int aumentarhora = 1;
    int ciclohora = 1;
    public String pasando_dato;
    private ArrayList nombre_usuario;
    private ArrayList jefatura_usuario;
    private ArrayList apellido_usuario;
    private ArrayList correo_usuario;
    private ArrayList id_usuario;
    private ArrayList tarea_id;
    private ArrayList realizada;
    private ArrayList realizada_pdc;
    private ArrayList id_desglose;
    public String usuario;
    public String id_jefatura;
    public String nombre_usuario_conespacios;
    private static final String IP_MENSAJE = "https://yotrabajoconpecs.ddns.net/Enviar_Notificacion.php";
    public String EMISOR;
    public String RECEPTOR;
    public String NOMBRE;
    public String dato;
    public String pasando_modificar;
    public String realizado_pdc;
    public String IDUSUARIO;
    public String Valor_dia;
    public String DESGLOSE;
    public String CORREO;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.pdc_2, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Panel");
        datosRecibidos = getArguments();
        fragment = getTargetFragment();
        datos = new Bundle();
        tvpanel = vista.findViewById(R.id.panel);
        tvdia = vista.findViewById(R.id.dia);
        tvano = vista.findViewById(R.id.ano);
        tvmes = vista.findViewById(R.id.mes);
        reloj = vista.findViewById(R.id.reloj);
        checkTarea = vista.findViewById(R.id.checktarea);
        reloj.setText(horan);
        pb = vista.findViewById(R.id.progreso);
        id = new ArrayList();
        fecha_inicio = new ArrayList();
        fecha_termino = new ArrayList();
        hora_inicio = new ArrayList();
        hora_termino = new ArrayList();
        dia = new ArrayList();
        url = new ArrayList();
        id_tarea = new ArrayList();
        nombre_usuario = new ArrayList();
        jefatura_usuario = new ArrayList();
        apellido_usuario = new ArrayList();
        correo_usuario = new ArrayList();
        id_usuario = new ArrayList();
        tarea_id = new ArrayList();
        realizada = new ArrayList();
        realizada_pdc = new ArrayList();
        id_desglose = new ArrayList();
        tvdia.setText("" + tv_dia);
        tvano.setText("" + tv_ano);
        usuario = Preferences.obtenerPreferenceString(getContext(), Preferences.PREFERENCE_USUARIO_LOGIN);
        descargarUsuario();
        switch(tv_mes) {
            case 1:
                tvmes.setImageResource(R.drawable.enero);
                break;
            case 2:
                tvmes.setImageResource(R.drawable.febrero);
                break;
            case 3:
                tvmes.setImageResource(R.drawable.marzo);
                break;
            case 4:
                tvmes.setImageResource(R.drawable.abril);
                break;
            case 5:
                tvmes.setImageResource(R.drawable.mayo);
                break;
            case 6:
                tvmes.setImageResource(R.drawable.junio);
                break;
            case 7:
                tvmes.setImageResource(R.drawable.julio);
                break;
            case 8:
                tvmes.setImageResource(R.drawable.agosto);
                break;
            case 9:
                tvmes.setImageResource(R.drawable.septiembre);
                break;
            case 10:
                tvmes.setImageResource(R.drawable.octubre);
                break;
            case 11:
                tvmes.setImageResource(R.drawable.noviembre);
                break;
            default:
                tvmes.setImageResource(R.drawable.diciembre);
        }
        tvmes.setLayoutParams(new LinearLayout.LayoutParams(300, 300));
        tvmes.setPadding(10, 10, 10, 10);
        tvmes.setScaleType(ImageButton.ScaleType.CENTER_CROP);
        tvmes.setBackgroundColor(0xFFFFFF);

        return vista;
    }

    private void descargarDatos(String URL){
        id.clear();
        fecha_inicio.clear();
        fecha_termino.clear();
        hora_inicio.clear();
        hora_termino.clear();
        dia.clear();
        url.clear();
        tarea_id.clear();
        realizada.clear();
        realizada_pdc.clear();
        id_desglose.clear();
        rellenarTabla("https://yotrabajoconpecs.ddns.net/query3.php?usuario=" + EMISOR + "&idusuario=" + IDUSUARIO);
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Cargando datos...");
        progressDialog.show();
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
                            id.add(jsonarray.getJSONObject(i).getString("id_dia"));
                            fecha_inicio.add(jsonarray.getJSONObject(i).getString("fecha_inicio"));
                            fecha_termino.add(jsonarray.getJSONObject(i).getString("fecha_termino"));
                            hora_inicio.add(jsonarray.getJSONObject(i).getString("hora_inicio"));
                            hora_termino.add(jsonarray.getJSONObject(i).getString("hora_termino"));
                            dia.add(jsonarray.getJSONObject(i).getString("dia"));
                            url.add(jsonarray.getJSONObject(i).getString("url"));
                            id_tarea.add(jsonarray.getJSONObject(i).getString("id_tarea"));
                            realizada.add(jsonarray.getJSONObject(i).getString("realizada"));
                            realizada_pdc.add(jsonarray.getJSONObject(i).getString("realizada_pdc"));
                            id_desglose.add(jsonarray.getJSONObject(i).getString("desglose"));

                            String horainicio = hora_inicio.get(i).toString();
                            String subhorainicio = horainicio.substring(0,2);
                            int subhorainicio1 = Integer.parseInt(subhorainicio);
                            String subminutoinicio = horainicio.substring(4,5);
                            int subminutoinicio1 = Integer.parseInt(subminutoinicio);
                            String subsegundoinicio = horainicio.substring(7,8);
                            int subsegundoinicio1 = Integer.parseInt(subsegundoinicio);

                            String horatermino = hora_termino.get(i).toString();
                            String subhoratermino = horatermino.substring(0,2);
                            int subhoratermino1 = Integer.parseInt(subhoratermino);
                            String subminutotermino = horatermino.substring(4,5);
                            int subminutotermino1 = Integer.parseInt(subminutotermino);
                            String subsegundotermino = horatermino.substring(7,8);
                            int subsegundotermino1 = Integer.parseInt(subsegundotermino);

                            int calculoSegundos = (subhoratermino1 - hora)*60*60 + subminutotermino1*60 + (60 - minuto)*60 + subsegundotermino1 + (60 - segundo);
                            //Toast.makeText(getContext(),"segundos restantes: " + calculoSegundos,Toast.LENGTH_SHORT).show();
                            pb.setMax(calculoSegundos);
                            mProgressStatus = (hora - subhorainicio1)*60*60 + subminutoinicio1*60 + (60 - minuto)*60 + subsegundoinicio1 + (60 - segundo);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    while(mProgressStatus < calculoSegundos){
                                        mProgressStatus++;
                                        android.os.SystemClock.sleep(50);
                                        mHandler.post(new Runnable(){
                                            @Override
                                            public void run() {
                                                pb.setProgress(mProgressStatus);
                                            }
                                        });
                                    }
                                }
                            }).start();
                            //Toast.makeText(getContext(), text, duration).show();
                        }

                        for(int j = 0; j < id.size(); j++){
                            String ultimo = id.get(id.size()-1).toString();
                            if(horan.compareTo(hora_inicio.get(j).toString()) > 0 && horan.compareTo(hora_termino.get(j).toString()) < 0) {
                                if (realizada.get(j).toString().equals("null")) {
                                    dato = url.get(j).toString();
                                    pasando_dato = id_tarea.get(j).toString();
                                    pasando_modificar = id.get(j).toString();
                                    realizado_pdc = realizada_pdc.get(j).toString();
                                    DESGLOSE = id_desglose.get(j).toString();
                                    //Toast.makeText(getContext(), DESGLOSE, Toast.LENGTH_LONG).show();
                                    tvpanel.setBackgroundResource(R.drawable.boton_rectangulo);
                                    tvpanel.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    tvpanel.setScaleType(ImageButton.ScaleType.CENTER_INSIDE);
                                    Picasso.get().load("https://yotrabajoconpecs.ddns.net/" + dato).into(tvpanel);
                                } else {
                                    dato = url.get(j+1).toString();
                                    pasando_dato = id_tarea.get(j+1).toString();
                                    pasando_modificar = id.get(j+1).toString();
                                    realizado_pdc = realizada_pdc.get(j+1).toString();
                                    DESGLOSE = id_desglose.get(j+1).toString();
                                    //Toast.makeText(getContext(), DESGLOSE, Toast.LENGTH_LONG).show();
                                    tvpanel.setBackgroundResource(R.drawable.boton_rectangulo);
                                    tvpanel.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    tvpanel.setScaleType(ImageButton.ScaleType.CENTER_INSIDE);
                                    Picasso.get().load("https://yotrabajoconpecs.ddns.net/" + dato).into(tvpanel);
                                }
                            }

                            if(realizado_pdc == "null"){
                                checkTarea.setVisibility(View.VISIBLE);
                            }
                            if(DESGLOSE != "null") {
                                tvpanel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Fragment fragment = new DesgloseFragment();
                                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                        transaction.replace(R.id.nav_host_fragment, fragment);
                                        transaction.addToBackStack(null);
                                        transaction.commit();
                                        datos.putString("id_tarea", pasando_dato);
                                        Log.e("Enviar", "tarea enviada");
                                        fragment.setArguments(datos);
                                        //Toast.makeText(getContext(), "pase el id_tarea: " + pasando_dato, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
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

    private void descargarUsuario(){
        nombre_usuario.clear();
        jefatura_usuario.clear();
        apellido_usuario.clear();
        id_usuario.clear();
        correo_usuario.clear();
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("https://yotrabajoconpecs.ddns.net/query_usuario.php", new AsyncHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200){
                    progressDialog.dismiss();
                    try{
                        JSONArray jsonarray = new JSONArray(new String(responseBody));
                        for(int i = 0; i < jsonarray.length(); i++) {
                            nombre_usuario.add(jsonarray.getJSONObject(i).getString("nombre_usuario"));
                            apellido_usuario.add(jsonarray.getJSONObject(i).getString("apellido_usuario"));
                            jefatura_usuario.add(jsonarray.getJSONObject(i).getString("jefatura"));
                            correo_usuario.add(jsonarray.getJSONObject(i).getString("correo"));
                            id_usuario.add(jsonarray.getJSONObject(i).getString("id_usuario"));
                            nombre_usuario_conespacios = nombre_usuario.get(i).toString() + " " + apellido_usuario.get(i).toString();
                            String nombre_usuario_sinespacios = nombre_usuario_conespacios.replace(" ", "");
                            if (usuario.equals(nombre_usuario_sinespacios)) {
                                id_jefatura = jefatura_usuario.get(i).toString();
                                EMISOR = correo_usuario.get(i).toString();
                                NOMBRE = nombre_usuario_conespacios;
                                IDUSUARIO = id_usuario.get(i).toString();
                                CORREO = correo_usuario.get(i).toString();
                            }
                        }
                        descargarDatos("https://yotrabajoconpecs.ddns.net/query_TareasDia.php?usuario=" + EMISOR);
                        checkTarea.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                guardarListaTarea("https://yotrabajoconpecs.ddns.net/save_lista_tarea.php", pasando_dato, NOMBRE, id_jefatura, pasando_modificar, CORREO);
                                checkTarea.setVisibility(View.INVISIBLE);
                                RECEPTOR = "1";
                                MandarMensaje();
                                //Toast.makeText(getContext(), pasando_modificar, Toast.LENGTH_SHORT).show();
                                validarTareaPDC("https://yotrabajoconpecs.ddns.net/validar_tarea_pdc.php?id=" + pasando_modificar + "&usuario=" + EMISOR);
                            }
                        });
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

    private void guardarListaTarea(String URL, String tarea, String quien, String jefatura, String id, String correo){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getContext(), "Se ha terminado la tarea con éxito", Toast.LENGTH_SHORT).show();
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
                parametros.put("id_tarea_lista", tarea);
                parametros.put("quien_envia", quien);
                parametros.put("correo", correo);
                parametros.put("id_jefatura", jefatura);
                parametros.put("id_listatarea", id);
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void MandarMensaje() {
        HashMap<String, String> hashMapToken = new HashMap<>();
        hashMapToken.put("emisor", EMISOR);
        hashMapToken.put("nombrecompleto", NOMBRE);
        hashMapToken.put("receptor", RECEPTOR);
        //Toast.makeText(getContext(), EMISOR + NOMBRE + RECEPTOR + "" + IP_MENSAJE, Toast.LENGTH_SHORT).show();

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

    private void validarTareaPDC(String URL){
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

    private void rellenarTabla(String URL) {
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
}
