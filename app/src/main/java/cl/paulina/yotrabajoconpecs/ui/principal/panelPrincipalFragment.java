package cl.paulina.yotrabajoconpecs.ui.principal;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import cl.paulina.yotrabajoconpecs.Amigos.ActivityAmigos;
import cl.paulina.yotrabajoconpecs.R;
import cl.paulina.yotrabajoconpecs.ui.panel.panelEmpleador;
import cl.paulina.yotrabajoconpecs.ui.panel.panelPDC;
import cz.msebera.android.httpclient.Header;

public class panelPrincipalFragment extends Fragment {

    public ImageView btnLibro, btnPanel;
    ArrayList tipo;
    public String actualizar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.pdc_0, container, false);

        btnLibro = vista.findViewById(R.id.libro);
        btnPanel = vista.findViewById(R.id.panel);
        tipo = new ArrayList();
        descargarDatos();

        btnLibro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uno = "1";
                if(actualizar.equals(uno)) {
                    Fragment fragment = new ActivityAmigos();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.nav_host_fragment, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }else{
                    Fragment fragment = new cl.paulina.yotrabajoconpecs.ui.libro.libroPDC();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.nav_host_fragment, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }

            }
        });

        btnPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uno = "1";
                if(actualizar.equals(uno)) {
                    Fragment fragment = new panelEmpleador();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.nav_host_fragment, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }else{
                    Fragment fragment = new panelPDC();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.nav_host_fragment, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });
        return vista;
    }

    private void descargarDatos(){
        tipo.clear();
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("https://yotrabajoconpecs.ddns.net/query_principal.php", new AsyncHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200){
                    progressDialog.dismiss();
                    try{
                        JSONArray jsonarray = new JSONArray(new String(responseBody));
                        for(int i = 0; i < jsonarray.length(); i++){
                            tipo.add(jsonarray.getJSONObject(i).getString("tipo"));
                        }
                        actualizar = tipo.get(tipo.size()-1).toString();
                        //Toast.makeText(getContext(),actualizar,Toast.LENGTH_SHORT).show();
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

}
