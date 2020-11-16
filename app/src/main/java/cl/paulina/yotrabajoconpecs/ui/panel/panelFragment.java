package cl.paulina.yotrabajoconpecs.ui.panel;

import android.app.ProgressDialog;
import android.content.Context;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import cl.paulina.yotrabajoconpecs.R;
import cz.msebera.android.httpclient.Header;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

public class panelFragment extends Fragment {
    TextView tvdia, tvano, reloj;
    Button agregardia, agregarhora;
    public Fragment fragment;
    private ProgressBar pb;
    private int mProgressStatus = 0;
    Bundle datos;
    Bundle datosRecibidos;
    private ArrayList id, fecha_inicio, fecha_termino, hora_inicio, hora_termino, dia, url, id_tarea;
    private ImageButton tvpanel, tvmes;
    private Handler mHandler = new Handler();
    Date date = new Date();
    DateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
    String horan = formatoHora.format(date);
    DateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
    String fechan = formatoFecha.format(date);
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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.pdc_2, container, false);
        datosRecibidos = getArguments();
        fragment = getTargetFragment();
        datos = new Bundle();
        tvpanel = vista.findViewById(R.id.panel);
        tvdia = vista.findViewById(R.id.dia);
        tvano = vista.findViewById(R.id.ano);
        tvmes = vista.findViewById(R.id.mes);
        reloj = vista.findViewById(R.id.reloj);
        reloj.setText(horan);
        agregardia = vista.findViewById(R.id.agregardia);
        agregarhora = vista.findViewById(R.id.agregarhora);
        pb = vista.findViewById(R.id.progreso);
        id = new ArrayList();
        fecha_inicio = new ArrayList();
        fecha_termino = new ArrayList();
        hora_inicio = new ArrayList();
        hora_termino = new ArrayList();
        dia = new ArrayList();
        url = new ArrayList();
        id_tarea = new ArrayList();
        tvdia.setText("" + tv_dia);
        tvano.setText("" + tv_ano);
        int ciclo = 1;

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

        descargarDatos();

        agregardia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ciclodia = 1;
                id.clear();
                fecha_inicio.clear();
                fecha_termino.clear();
                hora_inicio.clear();
                hora_termino.clear();
                dia.clear();
                url.clear();
                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Cargando datos...");
                progressDialog.show();
                AsyncHttpClient client = new AsyncHttpClient();
                client.get("https://yotrabajoconpecs.ddns.net/query3.php", new AsyncHttpResponseHandler() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        if(statusCode == 200){
                            progressDialog.dismiss();
                            try{
                                GregorianCalendar fechaCalendario = new GregorianCalendar();
                                fechaCalendario.setTime(date);
                                int diaSemana = fechaCalendario.get(Calendar.DAY_OF_WEEK) + aumentardia;
                                String Valor_dia = null;
                                if (diaSemana == 1) {
                                    Valor_dia = "domingo";
                                } else if (diaSemana == 2) {
                                    Valor_dia = "lunes";
                                } else if (diaSemana == 3) {
                                    Valor_dia = "martes";
                                } else if (diaSemana == 4) {
                                    Valor_dia = "miercoles";
                                } else if (diaSemana == 5) {
                                    Valor_dia = "jueves";
                                } else if (diaSemana == 6) {
                                    Valor_dia = "viernes";
                                } else if (diaSemana == 7) {
                                    Valor_dia = "sabado";
                                }
                                JSONArray jsonarray = new JSONArray(new String(responseBody));
                                for(int i = 0; i < jsonarray.length(); i++){
                                    id.add(jsonarray.getJSONObject(i).getString("id"));
                                    fecha_inicio.add(jsonarray.getJSONObject(i).getString("fecha_inicio"));
                                    fecha_termino.add(jsonarray.getJSONObject(i).getString("fecha_termino"));
                                    hora_inicio.add(jsonarray.getJSONObject(i).getString("hora_inicio"));
                                    hora_termino.add(jsonarray.getJSONObject(i).getString("hora_termino"));
                                    dia.add(jsonarray.getJSONObject(i).getString("dia"));
                                    url.add(jsonarray.getJSONObject(i).getString("url"));
                                    id_tarea.add(jsonarray.getJSONObject(i).getString("id_tarea"));
                                    String text = dia.get(i).toString();
                                    if(text.equals(Valor_dia) && horan.compareTo(hora_inicio.get(i).toString()) > 0 && horan.compareTo(hora_termino.get(i).toString()) < 0){
                                        String dato = url.get(i).toString();
                                        Picasso.get().load("https://yotrabajoconpecs.ddns.net/" + dato).into(tvpanel);

                                        tvpanel.setLayoutParams(new LinearLayout.LayoutParams(800, 800));
                                        tvpanel.setPadding(200, 10, 10, 10);
                                        tvpanel.setScaleType(ImageButton.ScaleType.FIT_CENTER);
                                        tvpanel.setBackgroundColor(0xFFFFFF);

                                        String pasando_dato = id_tarea.get(i).toString();
                                        tvpanel.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Fragment fragment = new desglose();
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
                                    }
                                }
                            }catch(JSONException e){
                                e.printStackTrace();
                            }
                        }
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        //progressDialog.dismiss();
                        Context context = getContext();
                        CharSequence text = "Conexión fallida";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                });
                if(tv_dia >= 31 && ciclodia < 2) {
                    tv_dia = 1;
                    tvdia.setText("" + tv_dia);
                    tv_mes = tv_mes + 1;
                    ciclodia = ciclodia + 1;
                    Toast.makeText(getContext(), "Ciclo: " + ciclodia, Toast.LENGTH_SHORT).show();
                    switch (tv_mes) {
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
                }else if(tv_dia >= 31 && ciclodia > 1){
                    tv_dia = 1;
                    tvdia.setText("" + tv_dia);
                    tv_mes = tv_mes + 1;
                    ciclodia = ciclodia + 1;
                    Toast.makeText(getContext(), "Ciclo: " + ciclodia, Toast.LENGTH_SHORT).show();
                    switch (tv_mes) {
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
                }else{
                    tv_dia = tv_dia + aumentardia;
                    tvdia.setText("" + tv_dia);
                }
            }
        });

        agregarhora.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View v){
                    String actualizarstring;
                    if(hora >= 1 && hora <= 9){
                        actualizarstring = "0" + hora + "";
                    }else{
                        actualizarstring = hora + "";
                    }
                    String horannueva = actualizarstring + horan.substring(2,8);
                    horan = horannueva;
                    reloj.setText(horan);
                    //Toast.makeText(getContext(), horannueva, Toast.LENGTH_SHORT).show();
                    id.clear();
                    fecha_inicio.clear();
                    fecha_termino.clear();
                    hora_inicio.clear();
                    hora_termino.clear();
                    dia.clear();
                    url.clear();
                    final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage("Cargando datos...");
                    progressDialog.show();
                    AsyncHttpClient client = new AsyncHttpClient();
                    client.get("https://yotrabajoconpecs.ddns.net/query3.php", new AsyncHttpResponseHandler() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            if (statusCode == 200) {
                                progressDialog.dismiss();
                                try {
                                    GregorianCalendar fechaCalendario = new GregorianCalendar();
                                    fechaCalendario.setTime(date);
                                    int diaSemana = fechaCalendario.get(Calendar.DAY_OF_WEEK) + 1;
                                    String Valor_dia = null;
                                    if (diaSemana == 1) {
                                        Valor_dia = "domingo";
                                    } else if (diaSemana == 2) {
                                        Valor_dia = "lunes";
                                    } else if (diaSemana == 3) {
                                        Valor_dia = "martes";
                                    } else if (diaSemana == 4) {
                                        Valor_dia = "miercoles";
                                    } else if (diaSemana == 5) {
                                        Valor_dia = "jueves";
                                    } else if (diaSemana == 6) {
                                        Valor_dia = "viernes";
                                    } else if (diaSemana == 7) {
                                        Valor_dia = "sabado";
                                    }
                                    JSONArray jsonarray = new JSONArray(new String(responseBody));
                                    for (int i = 0; i < jsonarray.length(); i++) {
                                        id.add(jsonarray.getJSONObject(i).getString("id"));
                                        fecha_inicio.add(jsonarray.getJSONObject(i).getString("fecha_inicio"));
                                        fecha_termino.add(jsonarray.getJSONObject(i).getString("fecha_termino"));
                                        hora_inicio.add(jsonarray.getJSONObject(i).getString("hora_inicio"));
                                        hora_termino.add(jsonarray.getJSONObject(i).getString("hora_termino"));
                                        dia.add(jsonarray.getJSONObject(i).getString("dia"));
                                        url.add(jsonarray.getJSONObject(i).getString("url"));
                                        id_tarea.add(jsonarray.getJSONObject(i).getString("id_tarea"));
                                        String text = dia.get(i).toString();
                                        if (text.equals(Valor_dia) && horan.compareTo(hora_inicio.get(i).toString()) > 0 && horan.compareTo(hora_termino.get(i).toString()) < 0) {
                                            String dato = url.get(i).toString();
                                            Picasso.get().load("https://yotrabajoconpecs.ddns.net/" + dato).into(tvpanel);

                                            tvpanel.setLayoutParams(new LinearLayout.LayoutParams(800, 800));
                                            tvpanel.setPadding(200, 10, 10, 10);
                                            tvpanel.setScaleType(ImageButton.ScaleType.FIT_CENTER);
                                            tvpanel.setBackgroundColor(0xFFFFFF);

                                            String pasando_dato = id_tarea.get(i).toString();
                                            tvpanel.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Fragment fragment = new desglose();
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
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            //progressDialog.dismiss();
                            Context context = getContext();
                            CharSequence text = "Conexión fallida";
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }
                    });
                    //Toast.makeText(getContext(), "Hora: " + actualizar, Toast.LENGTH_SHORT).show();
                    if(hora >= 24 && ciclohora < 2) {
                        hora = 1;
                        ciclohora = ciclohora + 1;
                        Toast.makeText(getContext(), "Ciclo: " + ciclohora, Toast.LENGTH_SHORT).show();
                    }else if(hora >= 24 && ciclohora > 1){
                        ciclohora = ciclohora + 1;
                    }else{
                        hora = hora + aumentarhora;
                    }
                }
            });

        return vista;
    }

    private void descargarDatos(){
        id.clear();
        fecha_inicio.clear();
        fecha_termino.clear();
        hora_inicio.clear();
        hora_termino.clear();
        dia.clear();
        url.clear();

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Cargando datos...");
        progressDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("https://yotrabajoconpecs.ddns.net/query3.php", new AsyncHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200){
                    progressDialog.dismiss();
                    try{
                        GregorianCalendar fechaCalendario = new GregorianCalendar();
                        fechaCalendario.setTime(date);
                        int diaSemana = fechaCalendario.get(Calendar.DAY_OF_WEEK);
                        String Valor_dia = null;
                        if (diaSemana == 1) {
                            Valor_dia = "domingo";
                        } else if (diaSemana == 2) {
                            Valor_dia = "lunes";
                        } else if (diaSemana == 3) {
                            Valor_dia = "martes";
                        } else if (diaSemana == 4) {
                            Valor_dia = "miercoles";
                        } else if (diaSemana == 5) {
                            Valor_dia = "jueves";
                        } else if (diaSemana == 6) {
                            Valor_dia = "viernes";
                        } else if (diaSemana == 7) {
                            Valor_dia = "sabado";
                        }

                        JSONArray jsonarray = new JSONArray(new String(responseBody));
                       for(int i = 0; i < jsonarray.length(); i++){
                            id.add(jsonarray.getJSONObject(i).getString("id"));
                            fecha_inicio.add(jsonarray.getJSONObject(i).getString("fecha_inicio"));
                            fecha_termino.add(jsonarray.getJSONObject(i).getString("fecha_termino"));
                            hora_inicio.add(jsonarray.getJSONObject(i).getString("hora_inicio"));
                            hora_termino.add(jsonarray.getJSONObject(i).getString("hora_termino"));
                            dia.add(jsonarray.getJSONObject(i).getString("dia"));
                            url.add(jsonarray.getJSONObject(i).getString("url"));
                            id_tarea.add(jsonarray.getJSONObject(i).getString("id_tarea"));
                            //Toast.makeText(getContext(), dia.get(i).toString(), Toast.LENGTH_SHORT).show();

                            String text = dia.get(i).toString();
                            if(text.equals(Valor_dia) && horan.compareTo(hora_inicio.get(i).toString()) > 0 && horan.compareTo(hora_termino.get(i).toString()) < 0){
                                //tvpanel.setBackgroundResource(R.drawable.diciembre);
                                //Toast.makeText(getContext(),"entre al if",Toast.LENGTH_SHORT).show();
                                String dato = url.get(i).toString();
                                Picasso.get().load("https://yotrabajoconpecs.ddns.net/" + dato).into(tvpanel);

                                tvpanel.setLayoutParams(new LinearLayout.LayoutParams(800, 800));
                                tvpanel.setPadding(200, 10, 10, 10);
                                tvpanel.setScaleType(ImageButton.ScaleType.FIT_CENTER);
                                tvpanel.setBackgroundColor(0xFFFFFF);

                                String pasando_dato = id_tarea.get(i).toString();
                                tvpanel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Fragment fragment = new desglose();
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
                            }
                            //Toast.makeText(getContext(), text, duration).show();
                        }
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //progressDialog.dismiss();
                Context context = getContext();
                CharSequence text = "Conexión fallida";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });
    }
}
