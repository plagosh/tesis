package cl.paulina.yotrabajoconpecs.ui.busqueda_pictograma_pdc;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
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

import cl.paulina.yotrabajoconpecs.R;
import cl.paulina.yotrabajoconpecs.ui.libro.libroPDC;
import cz.msebera.android.httpclient.Header;

public class busquedaPictogramapdc2Fragment extends Fragment {
    private GridView gridView;
    private ArrayList id_imagen, id_arasaac, nombre_imagen, categoria_imagen, url;

    public Fragment fragment;
    private ImageButton tvimagen;
    Bundle datos;
    Bundle datosRecibidos;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.pdc_1_3, container, false);
        datosRecibidos = getArguments();
        fragment = getTargetFragment();
        datos = new Bundle();
        gridView = vista.findViewById(R.id.listview2);

        tvimagen = vista.findViewById(R.id.tv_imagen);
        id_imagen = new ArrayList();
        id_arasaac = new ArrayList();
        nombre_imagen = new ArrayList();
        categoria_imagen = new ArrayList();
        url = new ArrayList();
        descargarDatos();
        return vista;
    }

    private void descargarDatos(){
        id_imagen.clear();
        id_arasaac.clear();
        nombre_imagen.clear();
        categoria_imagen.clear();
        url.clear();
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
                            categoria_imagen.add(jsonarray.getJSONObject(i).getString("categoria_imagen"));
                            id_imagen.add(jsonarray.getJSONObject(i).getString("id_imagen"));
                            nombre_imagen.add(jsonarray.getJSONObject(i).getString("nombre_imagen"));
                            url.add(jsonarray.getJSONObject(i).getString("url"));
                        }
                        gridView.setAdapter(new busquedaPictogramapdc2Fragment.CustomAdapter(getContext()));
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
        TextView tvnombre;

        @RequiresApi(api = Build.VERSION_CODES.M)
        public CustomAdapter(Context applicationContext){
            this.ctx = applicationContext;
            layoutInflater = (LayoutInflater)this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount(){
            return nombre_imagen.size();
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
            ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.list_view_item, null);
            tvimagen = (ImageButton) viewGroup.findViewById(R.id.tv_imagen);
            tvnombre = (TextView) viewGroup.findViewById(R.id.tv_nombre);

            Object var = url.get(position).toString();
            Picasso.get().load("https://yotrabajoconpecs.ddns.net/" + var).into(tvimagen);
            tvimagen.setLayoutParams(new LinearLayout.LayoutParams(200, 200));
            tvimagen.setScaleType(ImageButton.ScaleType.FIT_CENTER);
            tvimagen.setBackgroundResource(R.drawable.boton_rectangulo);
            tvnombre.setText(nombre_imagen.get(position).toString());
            tvimagen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragmento = new libroPDC();
                    datos.putString("url_sust", url.get(position).toString());
                    datos.putString("nombre_imagen_sust", nombre_imagen.get(position).toString());
                    fragmento.setArguments(datos);
                    cambiarFragmento(fragmento);
                }
            });
            return viewGroup;
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        OnBackPressedCallback callback = new OnBackPressedCallback (true){
            @Override
            public void handleOnBackPressed() {
                cambiarFragmento(fragment);
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
}
