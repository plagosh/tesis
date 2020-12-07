package cl.paulina.yotrabajoconpecs.ui.libro;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import cl.paulina.yotrabajoconpecs.R;
import cz.msebera.android.httpclient.Header;

public class frase_general extends Fragment {
    private GridView gridView;
    public LinearLayout contentCaja;
    private ArrayList id_frase;
    private ArrayList url;
    private ArrayList id_imagen;
    private ArrayList agregandoFrase;
    public ArrayList glosa;
    public Fragment fragment;
    Bundle datos;
    BottomSheetBehavior sheetBehavior;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_layout, container, false);

        gridView = view.findViewById(R.id.listviewFrase);
        contentCaja = view.findViewById(R.id.contentCaja);

        id_frase = new ArrayList();
        url = new ArrayList();
        agregandoFrase = new ArrayList();
        id_imagen = new ArrayList();
        glosa = new ArrayList();

        descargarDatos();

        return view;
    }

    private void descargarDatos(){
        id_frase.clear();
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("https://yotrabajoconpecs.ddns.net/query_frase.php", new AsyncHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200){
                    progressDialog.dismiss();
                    try{
                        JSONArray jsonarray = new JSONArray(new String(responseBody));
                        for(int i = 0; i < jsonarray.length(); i++){
                            id_frase.add(jsonarray.getJSONObject(i).getString("url"));
                        }gridView.setAdapter(new frase_general.CustomAdapter(getContext()));
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
            String split[] = frase.split(" ");
            for(int i = 0; i < split.length; i++) {
                ImageButton imagen = new ImageButton(getContext());
                imagen.setBackgroundResource(R.drawable.boton_rectangulo);
                imagen.setLayoutParams(new LinearLayout.LayoutParams(130, 130));
                imagen.setScaleType(ImageButton.ScaleType.FIT_CENTER);
                Picasso.get().load("https://yotrabajoconpecs.ddns.net/" + split[i]).into(imagen);
                TvImagenButton.addView(imagen);
            }
            glosa.clear();
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            AsyncHttpClient client = new AsyncHttpClient();
            client.get("https://yotrabajoconpecs.ddns.net/query_frase2.php", new AsyncHttpResponseHandler() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    if(statusCode == 200){
                        progressDialog.dismiss();
                        try{
                            JSONArray jsonarray = new JSONArray(new String(responseBody));
                            for(int i = 0; i < jsonarray.length(); i++){
                                glosa.add(jsonarray.getJSONObject(i).getString("glosa"));
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
            TvImagenButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragmento = new libroPDC();
                    datos = new Bundle();
                    datos.putString("url_frase", id_frase.get(position).toString());
                    datos.putString("glosa_frase", glosa.get(position).toString());
                    fragmento.setArguments(datos);
                    cambiarFragmento(fragmento);
                    sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
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
}
