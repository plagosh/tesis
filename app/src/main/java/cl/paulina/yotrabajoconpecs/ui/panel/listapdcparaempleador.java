package cl.paulina.yotrabajoconpecs.ui.panel;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import cl.paulina.yotrabajoconpecs.MainActivity;
import cl.paulina.yotrabajoconpecs.R;
import cl.paulina.yotrabajoconpecs.ui.principal.panelPrincipalFragment;
import cz.msebera.android.httpclient.Header;

public class listapdcparaempleador extends Fragment {
    public GridView gridView;
    public ArrayList tipo, id_usuario, correo, clave, nombre_usuario, apellido_usuario, tipousuario_tipo, jefatura, pictosId, pictosNombre, pictosApellido, pictosCorreo;
    public Fragment fragment;
    public ImageButton tvimagen;
    Bundle datos;
    Bundle datosRecibidos;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.empleador_2, container, false);
        datosRecibidos = getArguments();
        fragment = getTargetFragment();
        datos = new Bundle();
        gridView = vista.findViewById(R.id.listview3);
        tvimagen = vista.findViewById(R.id.tv_imagen);
        tipo = new ArrayList();
        id_usuario = new ArrayList();
        correo = new ArrayList();
        clave = new ArrayList();
        nombre_usuario = new ArrayList();
        apellido_usuario = new ArrayList();
        tipousuario_tipo = new ArrayList();
        jefatura = new ArrayList();
        pictosNombre = new ArrayList();
        pictosApellido = new ArrayList();
        pictosCorreo = new ArrayList();
        pictosId = new ArrayList();

        descargarDatos();

        return vista;
    }

    private void descargarDatos(){
        id_usuario.clear();
        correo.clear();
        clave.clear();
        nombre_usuario.clear();
        apellido_usuario.clear();
        tipousuario_tipo.clear();
        jefatura.clear();

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Cargando datos...");
        progressDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("https://yotrabajoconpecs.ddns.net/usuario.php", new AsyncHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    if(statusCode == 200){
                    progressDialog.dismiss();
                    try{
                        JSONArray jsonarray = new JSONArray(new String(responseBody));
                        for(int i = 0; i < jsonarray.length(); i++){
                            id_usuario.add(jsonarray.getJSONObject(i).getString("id_usuario"));
                            correo.add(jsonarray.getJSONObject(i).getString("correo"));
                            clave.add(jsonarray.getJSONObject(i).getString("clave"));
                            nombre_usuario.add(jsonarray.getJSONObject(i).getString("nombre_usuario"));
                            apellido_usuario.add(jsonarray.getJSONObject(i).getString("apellido_usuario"));
                            tipousuario_tipo.add(jsonarray.getJSONObject(i).getString("tipousuario_tipo"));
                            jefatura.add(jsonarray.getJSONObject(i).getString("jefatura"));
                        }
                        for(int j = 0; j < id_usuario.size(); j++){
                            String buscandodos = tipousuario_tipo.get(j).toString();
                            String dos = "2";
                            if(buscandodos.equals(dos)){
                                pictosId.add(id_usuario.get(j).toString());
                                pictosNombre.add(nombre_usuario.get(j).toString());
                                pictosApellido.add(apellido_usuario.get(j).toString());
                                pictosCorreo.add(correo.get(j).toString());
                            }
                        }
                        gridView.setAdapter(new listapdcparaempleador.CustomAdapter(getContext()));
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

    private class CustomAdapter extends BaseAdapter {
        Context ctx;
        LayoutInflater layoutInflater;
        TextView tvnombre, tvapellido, tvcorreo;

        @RequiresApi(api = Build.VERSION_CODES.M)
        public CustomAdapter(Context applicationContext){
            this.ctx = applicationContext;
            layoutInflater = (LayoutInflater)this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount(){
            return pictosId.size();
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
            ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.list_item, null);
            tvimagen = (ImageButton) viewGroup.findViewById(R.id.tv_imagen);
            tvnombre = (TextView) viewGroup.findViewById(R.id.tv_nombre);
            tvapellido = (TextView) viewGroup.findViewById(R.id.tv_apellido);
            tvcorreo = (TextView) viewGroup.findViewById(R.id.tv_correo);

            Picasso.get().load("https://yotrabajoconpecs.ddns.net/repo/img/2617.png").into(tvimagen);
            tvimagen.setLayoutParams(new LinearLayout.LayoutParams(500, 500));
            tvimagen.setPadding(10, 10, 10, 10);
            tvimagen.setScaleType(ImageButton.ScaleType.CENTER_CROP);
            tvimagen.setBackgroundColor(0xFFFFFF);

            tvnombre.setText(pictosNombre.get(position).toString());
            tvapellido.setText(pictosApellido.get(position).toString());
            tvcorreo.setText(pictosCorreo.get(position).toString());

            tvimagen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment = new panelEmpleador();
                    fragment.setTargetFragment(listapdcparaempleador.this,1);
                    Log.e("Agregar", "agregado");
                    datos.putString("url", pictosId.get(position).toString());
                    Log.e("Agregar", "datos enviados");
                    fragment.setArguments(datos);
                    cambiarFragmento(fragment);
                    Toast.makeText(getContext(), "pase el url: " + pictosId.get(position).toString(), Toast.LENGTH_SHORT).show();
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
