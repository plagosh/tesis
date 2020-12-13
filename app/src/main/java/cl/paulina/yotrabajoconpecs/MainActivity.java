package cl.paulina.yotrabajoconpecs;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import cl.paulina.yotrabajoconpecs.MisClases.Principal.PanelPrincipalFragment;
import cz.msebera.android.httpclient.Header;

public class MainActivity extends Fragment {

    ImageView btnPanelPrincipal, imagen;
    Button cambiarimagen;
    TextView nombre;
    Bundle datos;
    public Bitmap bitmap;
    ArrayList nombres, correo;
    public String correoUsuario;
    public String mapabit;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.sesion, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Inicio");
        btnPanelPrincipal = vista.findViewById(R.id.to_pdc);
        nombre = vista.findViewById(R.id.nombre);
        cambiarimagen = vista.findViewById(R.id.cambiar_imagen);
        imagen = vista.findViewById(R.id.to_pdc);
        nombres = new ArrayList();
        correo = new ArrayList();
        nombres.clear();
        correo.clear();
        datos = new Bundle();

        cambiarimagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarImagen();
            }
        });

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
                        correoUsuario = correo.get(correo.size()-1).toString();
                        Picasso.get().load("https://yotrabajoconpecs.ddns.net/uploads/" + correoUsuario + ".jpg").into(imagen);
                        nombre.setText(nom);
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getContext(), "Conexión fallida", Toast.LENGTH_SHORT);
            }
        });

        btnPanelPrincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new PanelPrincipalFragment();
                fragment.setTargetFragment(MainActivity.this,1);
                cambiarFragmento(fragment);
            }
        });

        return vista;
    }

    private void cargarImagen(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent, "Seleccione la aplicación"), 10);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            Uri path = data.getData();
            try{
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), path);
                imagen.setImageBitmap(bitmap);
                mapabit = getStringImagen(bitmap);
                //Toast.makeText(getContext(), mapabit.length() + "", Toast.LENGTH_LONG).show();
                ModificarFotoUsuario("https://yotrabajoconpecs.ddns.net/modificar_usuarioImagen.php", mapabit, correoUsuario);
            }catch (IOException e){
                e.printStackTrace();
            }

        }
    }

    public void cambiarFragmento(Fragment fragment){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void ModificarFotoUsuario(String URL, String imagen, String usuario){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getContext(), "Se ha cambiado la foto con éxito", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<String, String>();
                params.put("foto", imagen);
                params.put("correo", usuario);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    public String getStringImagen(Bitmap bitmap){
        ByteArrayOutputStream byteArrayInputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayInputStream);
        byte[] imageBytes = byteArrayInputStream.toByteArray();
        String encodedImage = com.loopj.android.http.Base64.encodeToString(imageBytes, com.loopj.android.http.Base64.DEFAULT);
        return encodedImage;
    }
}
