package cl.paulina.yotrabajoconpecs.ui.panel;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import cl.paulina.yotrabajoconpecs.R;
import cz.msebera.android.httpclient.Header;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ViewEventsActivity extends Fragment implements View.OnLongClickListener{
    private GridView listView;
    public ArrayList idCalendario, fechaInicio, fechaTermino, horaInicio, horaTermino, diaCalendario, nombreTarea, idUsuario, nombreUsuario, apellidoUsuario;
    public String fecha;
    private Spinner spinnerPDC;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.activity_view_events, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Ver Tareas");
        listView = vista.findViewById(R.id.listaeventos);
        spinnerPDC = vista.findViewById(R.id.spinnerPDC);

        idCalendario = new ArrayList();
        fechaInicio = new ArrayList();
        fechaTermino = new ArrayList();
        horaInicio = new ArrayList();
        horaTermino = new ArrayList();
        diaCalendario = new ArrayList();
        nombreTarea = new ArrayList();
        idUsuario = new ArrayList();
        nombreUsuario = new ArrayList();
        apellidoUsuario = new ArrayList();

        Bundle bundle = getArguments();
        int dia, mes, anio;
        dia = mes = anio = 0;
        dia = bundle.getInt("key_dia");
        mes = bundle.getInt("key_mes");
        anio = bundle.getInt("key_año");
        fecha = anio + "-" + mes + "-" + dia;
        listView.setOnLongClickListener(this);
        descargarPDC();
        descargarDatosCalendario("https://yotrabajoconpecs.ddns.net/queryVerTarea.php?fecha=" + fecha);
        return vista;
    }

    @Override
    public boolean onLongClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        CharSequence []items = new CharSequence[2];
        items[0] = "Eliminar tarea";
        items[1] = "Cancelar";
        builder.setTitle("Eliminar tarea").setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0){
                    //eliminar evento
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        return false;
    }

    private void descargarPDC(){
        idUsuario.clear();
        nombreUsuario.clear();
        apellidoUsuario.clear();
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("https://yotrabajoconpecs.ddns.net/query_pdc.php", new AsyncHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200){
                    progressDialog.dismiss();
                    try{
                        JSONArray jsonarray = new JSONArray(new String(responseBody));
                        for(int i = 0; i < jsonarray.length(); i++){
                            idUsuario.add(jsonarray.getJSONObject(i).getString("id_usuario"));
                            nombreUsuario.add(jsonarray.getJSONObject(i).getString("nombre_usuario"));
                            apellidoUsuario.add(jsonarray.getJSONObject(i).getString("apellido_usuario"));
                        }
                        ArrayList nombreCompleto = new ArrayList();
                        for(int j = 0; j < nombreUsuario.size(); j++){
                            nombreCompleto.add(nombreUsuario.get(j).toString() + " " + apellidoUsuario.get(j).toString());
                        }
                        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, nombreCompleto);
                        spinnerPDC.setAdapter(adapter);
                        spinnerPDC.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                String nombreUsuario = (String) spinnerPDC.getAdapter().getItem(position);
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                Toast.makeText(getContext(), "No ha seleccionado una opción", Toast.LENGTH_SHORT).show();
                            }
                        });
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

    private void descargarDatosCalendario(String URL){
        idCalendario.clear();
        fechaInicio.clear();
        fechaTermino.clear();
        horaInicio.clear();
        horaTermino.clear();
        diaCalendario.clear();
        nombreTarea.clear();
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
                            idCalendario.add(jsonarray.getJSONObject(i).getString("id_calendario"));
                            fechaInicio.add(jsonarray.getJSONObject(i).getString("fecha_inicio"));
                            fechaTermino.add(jsonarray.getJSONObject(i).getString("fecha_termino"));
                            horaInicio.add(jsonarray.getJSONObject(i).getString("hora_inicio"));
                            horaTermino.add(jsonarray.getJSONObject(i).getString("hora_termino"));
                            diaCalendario.add(jsonarray.getJSONObject(i).getString("dia"));
                            nombreTarea.add(jsonarray.getJSONObject(i).getString("nombre_tarea"));
                        }listView.setAdapter(new CustomAdapter(getContext()));
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

    public class CustomAdapter extends BaseAdapter {
        Context ctx;
        LayoutInflater layoutInflater;
        private TextView tvtarea, tvfechainicio, tvfechatermino, tvhorainicio, tvhoratermino, tvdia;

        @RequiresApi(api = Build.VERSION_CODES.M)
        public CustomAdapter(Context applicationContext) {
            this.ctx = applicationContext;
            layoutInflater = (LayoutInflater) this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return idCalendario.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.list, null);
            tvtarea = (TextView) viewGroup.findViewById(R.id.tv_tarea);
            tvfechainicio = (TextView) viewGroup.findViewById(R.id.tv_fechainicio);
            tvfechatermino = (TextView) viewGroup.findViewById(R.id.tv_fechatermino);
            tvhorainicio = (TextView) viewGroup.findViewById(R.id.tv_horainicio);
            tvhoratermino = (TextView) viewGroup.findViewById(R.id.tv_horatermino);
            tvdia = (TextView) viewGroup.findViewById(R.id.tv_dia);

            /*Date fechaDate = ParseFecha(fecha);
            Date fechaInicioDate = ParseFecha(fechaInicio.get(position).toString());
            Date fechaTerminoDate = ParseFecha(fechaTermino.get(position).toString());
            Toast.makeText(getContext(), fechaDate.toString(), Toast.LENGTH_SHORT).show();*/
            tvtarea.setText(nombreTarea.get(position).toString());
            tvfechainicio.setText(fechaInicio.get(position).toString());
            tvfechatermino.setText(fechaTermino.get(position).toString());
            tvhorainicio.setText(horaInicio.get(position).toString());
            tvhoratermino.setText(horaTermino.get(position).toString());
            tvdia.setText(diaCalendario.get(position).toString());

            return viewGroup;
        }
    }

    public static Date ParseFecha(String fecha) {
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaDate = null;
        try {
            fechaDate = formato.parse(fecha);
        }catch (ParseException ex) {
            System.out.println(ex);
        }
        return fechaDate;
    }

}
