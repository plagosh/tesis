package cl.paulina.yotrabajoconpecs.ui.panel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import cl.paulina.yotrabajoconpecs.R;

public class agregarPDC extends Fragment {
    EditText firstNameRegister, lastNameRegister,emailRegister,passwordRegister;
    ProgressBar progressRegister;
    Button agregar;
    TextView volver;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.register, container, false);
        firstNameRegister = vista.findViewById(R.id.firstNameRegister);
        lastNameRegister = vista.findViewById(R.id.lastNameRegister);
        emailRegister = vista.findViewById(R.id.emailRegister);
        passwordRegister = vista.findViewById(R.id.passwordRegister);
        progressRegister = vista.findViewById(R.id.progressRegister);
        agregar = vista.findViewById(R.id.button_register);
        volver = vista.findViewById(R.id.txtr);

        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new listapdcparaempleador();
                fragment.setTargetFragment(agregarPDC.this,1);
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
