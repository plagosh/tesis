package cl.paulina.yotrabajoconpecs.ui.libro;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import cl.paulina.yotrabajoconpecs.R;
import cl.paulina.yotrabajoconpecs.ServicioAPI.PreferenciasCompartidas;
import cl.paulina.yotrabajoconpecs.ui.busqueda_pictograma_empleador.busquedaPictogramaFragment;
import cl.paulina.yotrabajoconpecs.ui.busqueda_pictograma_pdc.busquedaPictogramapdcFragment;
import cl.paulina.yotrabajoconpecs.ui.principal.panelPrincipalFragment;

public class libroFragment extends Fragment {

    public FloatingActionMenu actionMenu;
    public FloatingActionButton fabAdd, fabDelete;
    ArrayList pictos;
    PreferenciasCompartidas pc;
    AlertDialog.Builder builder;
    int Sapo = 0;
    int clik;
    String key = "url";
    LinearLayout contenido, caja, layout;
    public Button bBorrar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.pdc_1, container, false);
        actionMenu = vista.findViewById(R.id.fab_tirafrase);
        actionMenu.setClosedOnTouchOutside(true);
        layout = vista.findViewById(R.id.cajaBotonImagen);
        Bundle datosRecibido = getArguments();
        fabAdd = vista.findViewById(R.id.fab_tirafrase_añadir);
        fabDelete = vista.findViewById(R.id.fab_tirafrase_eliminar);
        pictos = new ArrayList();

        pc = new PreferenciasCompartidas();
        pictos = new ArrayList();
        caja = new LinearLayout(getContext());
        caja.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        caja.setOrientation(LinearLayout.HORIZONTAL);
        caja.setOrientation(LinearLayout.VERTICAL);
        pc.loadData(pictos, getContext(), key);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new busquedaPictogramapdcFragment();
                fragment.setTargetFragment(libroFragment.this,1);
                cambiarFragmento(fragment);
            }
        });
        if(datosRecibido!= null) {
            if(String.valueOf(datosRecibido.getString("url")) != String.valueOf(0)){
                String id = datosRecibido.getString("url");
                Log.e("DatoRecibido: ", String.valueOf(id));
                pictos.add(id);
                Log.e("DatoRecibido: ", "La lista tiene ->" + pictos.size());
                pc.saveData(pictos, getContext(), key);
                Sapo = 1;
            }
            datosRecibido.clear();
        }
        generarFoto(caja,layout,getContext(),pictos,key,0);
        return vista;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        OnBackPressedCallback callback = new OnBackPressedCallback (true){
            @Override
            public void handleOnBackPressed() {
                Fragment fragment = new panelPrincipalFragment();
                if (pictos.size() == 0 || Sapo == 0){
                    cambiarFragmento(fragment);
                }if(Sapo == 1){
                    builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Tienes nuevos pictogramas agregados.").setTitle("Cambios sin guardar.");
                    builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            pc.saveData(pictos,getContext(),key);
                            cambiarFragmento(fragment);
                            Toast.makeText(getContext(), "Pictogramas guardados", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setNegativeButton("No guardar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            pc.deleteData(pictos);
                            pc.saveData(pictos, getContext(), key);
                            cambiarFragmento(fragment);
                            Toast.makeText(getContext(), "Pictogramas borrados", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Log.e("AlertDialog","Acceso al metodo.");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this,callback);
    }

    public void generarFoto(LinearLayout caja, LinearLayout layout, Context context, ArrayList pictos,String key, int eliminar) {
        Log.e("GenerarFoto: ", "Ingreso a metodo");
        if (pictos.size() > 0) {
            Log.e("GenerarFotoTamañoLista ", String.valueOf(pictos.size()));
            for (int i = 0; pictos.size()> i; i++) {
                if(pictos.get(i) != "") {
                    ImageButton imagen = new ImageButton(context);
                    bBorrar = new Button(context);
                    LinearLayout contenido = new LinearLayout(context);
                    contenido.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    contenido.setOrientation(LinearLayout.HORIZONTAL);
                    bBorrar.setLayoutParams(new LinearLayout.LayoutParams(50,50));
                    bBorrar.setText("X");
                    bBorrar.setId(i);
                    bBorrar.setTag(i);
                    bBorrar.setBackgroundColor(Color.BLUE);
                    bBorrar.setTextColor(Color.WHITE);
                    Log.e("eliminarfueraif ", "" + eliminar);
                    bBorrar.setVisibility(View.INVISIBLE);
                    bBorrar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Fragment fragment = new libroFragment();
                            Toast.makeText(context,"Pictografía borrada",Toast.LENGTH_SHORT).show();
                            pc.deleteOneData(pictos, bBorrar.getId());
                            pc.saveData(pictos, context, key);
                            cambiarFragmento(fragment);
                        }
                    });
                    fabDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bBorrar.setVisibility(View.VISIBLE);
                        }
                    });
                imagen.setLayoutParams(new LinearLayout.LayoutParams(600, 600));
                imagen.setPadding(10, 10, 10, 10);
                imagen.setId(i);
                Picasso.get().load("https://yotrabajoconpecs.ddns.net/" + pictos.get(i)).into(imagen);
                contenido.addView(bBorrar);
                contenido.addView(imagen);
                caja.addView(contenido);
                }
            }
            layout.addView(caja);
        }
    }

    public void cambiarFragmento(Fragment fragment){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
