package cl.paulina.yotrabajoconpecs.ui.panel;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import cl.paulina.yotrabajoconpecs.R;
import cz.msebera.android.httpclient.Header;

public class AddActivity extends Fragment {
    private EditText fechadesde, horadesde, fechahasta, horahasta;
    private Button guardar;
    Spinner spinnerTarea;
    Spinner spinnerPDC;
    ArrayList idtarea, nombretarea, idcalendario, llenadodias, idUsuario, nombreUsuario, apellidoUsuario, idCalendario;
    public String tareaSeleccionada;
    CheckBox lunes, martes, miercoles, jueves, viernes, sabado, domingo;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.activity_add, container, false);
        fechadesde = vista.findViewById(R.id.fechadesde);
        horadesde = vista.findViewById(R.id.horadesde);
        fechahasta = vista.findViewById(R.id.fechahasta);
        horahasta = vista.findViewById(R.id.horahasta);
        guardar = vista.findViewById(R.id.btnAddActivity);
        spinnerTarea = vista.findViewById(R.id.spinnerTarea);
        spinnerPDC = vista.findViewById(R.id.spinnerPDC);
        lunes = vista.findViewById(R.id.lunes);
        martes = vista.findViewById(R.id.martes);
        miercoles = vista.findViewById(R.id.miercoles);
        jueves = vista.findViewById(R.id.jueves);
        viernes = vista.findViewById(R.id.viernes);
        sabado = vista.findViewById(R.id.sabado);
        domingo = vista.findViewById(R.id.domingo);

        idUsuario = new ArrayList();
        nombreUsuario = new ArrayList();
        apellidoUsuario = new ArrayList();
        idtarea = new ArrayList();
        nombretarea = new ArrayList();
        idcalendario = new ArrayList();
        llenadodias = new ArrayList();
        idCalendario = new ArrayList();

        descargarPDC();
        descargarTareas();

        Bundle bundle = getArguments();
        int dia = 0, mes = 0, anio = 0;
        dia = bundle.getInt("key_dia");
        mes = bundle.getInt("key_mes");
        anio = bundle.getInt("key_anio");
        fechadesde.setText(anio + "-" + mes + "-" + dia);
        fechahasta.setText(anio + "-" + mes + "-" + dia);

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llenadodias.clear();
                validar();
                for(int i = 0; i < llenadodias.size(); i++){
                    guardarCalendario("https://yotrabajoconpecs.ddns.net/saveCalendario.php", fechadesde.getText().toString(), fechahasta.getText().toString(), horadesde.getText().toString(), horahasta.getText().toString(), llenadodias.get(i).toString());
                    guardarTareaCalendario("https://yotrabajoconpecs.ddns.net/saveTareaCalendario.php", tareaSeleccionada);
                }
                fechadesde.setText("");
                fechahasta.setText("");
                horadesde.setText("");
                horahasta.setText("");
            }
        });

        return vista;
    }

    private void guardarCalendario(String URL, String fechainicio, String fechatermino, String horainicio, String horatermino, String dia){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) { }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String,String>();
                parametros.put("fecha_inicio", fechainicio);
                parametros.put("fecha_termino", fechatermino);
                parametros.put("hora_inicio", horainicio);
                parametros.put("hora_termino", horatermino);
                parametros.put("dia", dia);
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void guardarTareaCalendario(String URL, String tarea){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getContext(), "Se ha asignado la tarea con éxito", Toast.LENGTH_SHORT).show();
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
                parametros.put("tarea_id_tarea", tarea);
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void descargarTareas(){
        idtarea.clear();
        nombretarea.clear();
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("https://yotrabajoconpecs.ddns.net/query_tarea.php", new AsyncHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200){
                    progressDialog.dismiss();
                    try{
                        JSONArray jsonarray = new JSONArray(new String(responseBody));
                        for(int i = 0; i < jsonarray.length(); i++){
                            idtarea.add(jsonarray.getJSONObject(i).getString("id_tarea"));
                            nombretarea.add(jsonarray.getJSONObject(i).getString("nombre_tarea"));
                        }
                        ArrayAdapter adp = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, nombretarea);
                        spinnerTarea.setAdapter(adp);
                        spinnerTarea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                String elemento = (String) spinnerTarea.getAdapter().getItem(position);
                                tareaSeleccionada = idtarea.get(position).toString();
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

    private void validar() {
        if (lunes.isChecked() == true) {
            llenadodias.add("lunes");
        }
        if (martes.isChecked() == true) {
            llenadodias.add("martes");
        }
        if (miercoles.isChecked() == true) {
            llenadodias.add("miercoles");
        }
        if (jueves.isChecked() == true) {
            llenadodias.add("jueves");
        }
        if (viernes.isChecked() == true) {
            llenadodias.add("viernes");
        }
        if (sabado.isChecked() == true) {
            llenadodias.add("sabado");
        }
        if (domingo.isChecked() == true) {
            llenadodias.add("lunes");
        }
        if ((lunes.isChecked() == false) && (martes.isChecked() == false) && (miercoles.isChecked() == false) && (jueves.isChecked() == false) && (viernes.isChecked() == false) && (sabado.isChecked() == false) && (domingo.isChecked() == false)){
            Toast.makeText(getContext(), "No se han seleccionado dias", Toast.LENGTH_SHORT).show();
        }
    }
}
