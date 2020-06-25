package cl.paulina.yotrabajoconpecs.ui.busqueda_pictograma_empleador;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.github.clans.fab.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import cl.paulina.yotrabajoconpecs.R;
import cl.paulina.yotrabajoconpecs.ServicioAPI.ArasaacAPI;
import cl.paulina.yotrabajoconpecs.ServicioAPI.PreferenciasCompartidas;
import cl.paulina.yotrabajoconpecs.modelo.Pictograma;
import cl.paulina.yotrabajoconpecs.ui.libro.libroFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class busquedaPictogramaFragment extends Fragment {

    public Retrofit retrofit;
    public String TAG ="Estado API: ";
    public String locale = "es";
    public String urlAPI = "https://api.arasaac.org/api/";
    List<Pictograma> pr;
    public Button btnBuscarPicto;
    public FloatingActionButton btnAñadirPicto;
    public EditText etBuscarPicto;
    ProgressBar progressBar;
    Fragment fragment;
    Bundle datos;
    Bundle datosRecibidos;
    String tipo;
    PreferenciasCompartidas pc;
    ImageButton imagen;
    int i = 0;
    public int j;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.empleador_1_1, container, false);
        pc = new PreferenciasCompartidas();
        datosRecibidos = getArguments();
        fragment = getTargetFragment();
        LinearLayout layout = vista.findViewById(R.id.cajaBotonImagen);
        LinearLayout caja = new LinearLayout(getContext());
        btnBuscarPicto = vista.findViewById(R.id.btnBusquedaPictograma);
        etBuscarPicto = vista.findViewById(R.id.etBusquedaPictograma);
        progressBar = vista.findViewById(R.id.progressBarBusqueda);
        btnAñadirPicto = vista.findViewById(R.id.fab_añadir);
        btnAñadirPicto.setVisibility(View.INVISIBLE);
        datos = new Bundle();
        btnBuscarPicto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = etBuscarPicto.getText().toString();
                String[] palabras = searchText.split(" ");
                for(j = 0; j < palabras.length; j++){
                    retrofit = new Retrofit.Builder()
                            .baseUrl(urlAPI)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    ArasaacAPI service = retrofit.create(ArasaacAPI.class);
                    Call<List<Pictograma>> call = service.obtenerListaArasaac(locale, palabras[j]);
                    progressBar.setVisibility(View.VISIBLE);
                    call.enqueue(new Callback<List<Pictograma>>() {
                        @Override
                        public void onResponse(Call<List<Pictograma>> call, Response<List<Pictograma>> response) {
                            if (response.body() != null) {
                                int respuesta = pr.get(0).getId();
                                Log.e("GenerarFotoSecuencia ", "" + respuesta);

                                imagen = new ImageButton(getContext());
                                Picasso.get().load("https://api.arasaac.org/api/pictograms/" + respuesta).into(imagen);
                                imagen.setLayoutParams(new LinearLayout.LayoutParams(500, 500));
                                imagen.setPadding(10, 10, 10, 10);
                                imagen.setId(j);
                                caja.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                caja.addView(imagen);
                                caja.setOrientation(LinearLayout.VERTICAL);
                            } else {
                                etBuscarPicto.setError("Palabra no encontrada en la API");
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                        @Override
                        public void onFailure(Call<List<Pictograma>> call, Throwable t) {
                            progressBar.setVisibility(View.INVISIBLE);
                            btnAñadirPicto.setVisibility(View.INVISIBLE);
                            Log.e(TAG, "No conectado");
                        }
                    });
                }
                layout.addView(caja);
                btnAñadirPicto.setVisibility(View.VISIBLE);
                etBuscarPicto.setText("");
            }
        });

        btnAñadirPicto.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                Fragment fragmento = fragment;
                progressBar.setVisibility(View.VISIBLE);
                Log.e("Agregar","agregado");
                //datos.putAll(caja);
                Log.e("Agregar","datos enviados");
                fragment.setArguments(datos);
                cambiarFragmento(fragmento);
                Toast.makeText(getContext(),"Se ha añadido la Imagen ", Toast.LENGTH_SHORT).show();
            }
        });

        return vista;
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
        if(fragment.getTargetRequestCode() != 1){
            if(fragment.getTargetRequestCode() == 2){
                Log.e("Clima","DatosEnviado");
                datos.putString("tipo","clima");
                fragment.setArguments(datos);
                datosRecibidos.clear();
            }
            if(fragment.getTargetRequestCode() == 3) {
                Log.e("Estacion","DatosEnviado");
                datos.putString("tipo", "estacion");
                fragment.setArguments(datos);
                datosRecibidos.clear();
            }
        }
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}