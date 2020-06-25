package cl.paulina.yotrabajoconpecs.ui.panel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import cl.paulina.yotrabajoconpecs.R;
import cl.paulina.yotrabajoconpecs.ui.libro.libroFragment;
import cl.paulina.yotrabajoconpecs.ui.lista_pictos;

public class panelFragment extends Fragment {
    Button botonLista;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.pdc_2, container, false);
        botonLista = vista.findViewById(R.id.lista);

        botonLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new lista_pictos();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return vista;
    }


}
