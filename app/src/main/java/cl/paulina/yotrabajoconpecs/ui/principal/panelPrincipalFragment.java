package cl.paulina.yotrabajoconpecs.ui.principal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import cl.paulina.yotrabajoconpecs.R;
import cl.paulina.yotrabajoconpecs.ui.libro.libroFragment;
import cl.paulina.yotrabajoconpecs.ui.panel.panelFragment;

public class panelPrincipalFragment extends Fragment {

    public ImageView btnLibro, btnPanel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.pdc_0, container, false);

        btnLibro = vista.findViewById(R.id.libro);
        btnPanel = vista.findViewById(R.id.panel);

        btnLibro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new libroFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        btnPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new panelFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return vista;
    }
}
