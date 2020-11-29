package cl.paulina.yotrabajoconpecs.ui.panel;

import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import cl.paulina.yotrabajoconpecs.Amigos.AmigosAdapter;
import cl.paulina.yotrabajoconpecs.Amigos.AmigosAtributos;
import cl.paulina.yotrabajoconpecs.R;

public class stackTareas extends Fragment {
    private TextView tarea, hora;
    private CheckBox checkear;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.activity_stack, container, false);
        RecyclerView rv = vista.findViewById(R.id.stackRecyclerView);
        tarea = vista.findViewById(R.id.nombreTarea);
        hora = vista.findViewById(R.id.horaTarea);
        checkear = vista.findViewById(R.id.checkeado);
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(lm);
        return vista;
    }
}
