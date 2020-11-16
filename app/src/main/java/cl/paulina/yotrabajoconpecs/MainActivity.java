package cl.paulina.yotrabajoconpecs;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import cl.paulina.yotrabajoconpecs.ServicioAPI.PreferenciasCompartidas;
import cl.paulina.yotrabajoconpecs.ui.busqueda_pictograma_empleador.Mensajeria;
import cl.paulina.yotrabajoconpecs.ui.busqueda_pictograma_pdc.busquedaPictogramapdcFragment;
import cl.paulina.yotrabajoconpecs.ui.libro.libroFragment;
import cl.paulina.yotrabajoconpecs.ui.principal.panelPrincipalFragment;
import cz.msebera.android.httpclient.Header;

public class MainActivity extends Fragment {

    ImageView btnPanelPrincipal;
    TextView nombre;
    Bundle datos;
    ArrayList nombres, correo;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.sesion, container, false);
        btnPanelPrincipal = vista.findViewById(R.id.to_pdc);
        nombre = vista.findViewById(R.id.nombre);
        nombres = new ArrayList();
        correo = new ArrayList();
        nombres.clear();
        correo.clear();
        datos = new Bundle();

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("https://yotrabajoconpecs.ddns.net/query_loginreciclado.php", new AsyncHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200){
                    progressDialog.dismiss();
                    try{
                        JSONArray jsonarray = new JSONArray(new String(responseBody));
                        for(int i = 0; i < jsonarray.length(); i++){
                            nombres.add(jsonarray.getJSONObject(i).getString("nombres"));
                            correo.add(jsonarray.getJSONObject(i).getString("correo"));
                            //Toast.makeText(getContext(),"recibiendo: " + recibiendo.get(i).toString(), Toast.LENGTH_SHORT).show();
                        }
                        String nom = nombres.get(nombres.size()-1).toString();
                        nombre.setText(nom);
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

        btnPanelPrincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new panelPrincipalFragment();
                fragment.setTargetFragment(MainActivity.this,1);
                cambiarFragmento(fragment);
            }
        });

        return vista;
    }

    public void cambiarFragmento(Fragment fragment){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}