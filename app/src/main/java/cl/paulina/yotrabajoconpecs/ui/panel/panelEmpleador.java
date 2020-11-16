package cl.paulina.yotrabajoconpecs.ui.panel;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import cl.paulina.yotrabajoconpecs.R;
import cl.paulina.yotrabajoconpecs.ui.principal.panelPrincipalFragment;

public class panelEmpleador extends Fragment implements CalendarView.OnDateChangeListener {
    public CalendarView calendarView;
    public TextView calendarText;
    Long date;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.empleador_2_1, container, false);
        calendarView = vista.findViewById(R.id.calendarView);
        calendarText = vista.findViewById(R.id.textViewCalendar);
        calendarView.setOnDateChangeListener(this);
        return vista;
    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
        String date = (month + 1) + "/" + dayOfMonth + "/" + year;
        calendarText.setText(date);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        CharSequence []items = new CharSequence[3];
        items[0] = "Agregar Tarea";
        items[1] = "Ver tareas";
        items[2] = "Cancelar";
        final int dia, mes, anio;
        dia = dayOfMonth;
        mes = month + 1;
        anio = year;

        builder.setTitle("Seleccione una tarea").setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0){
                    //agregar evento
                    Fragment fragment = new AddActivity();
                    Bundle bundle = new Bundle();
                    bundle.putInt("key_dia", dia);
                    bundle.putInt("key_mes", mes);
                    bundle.putInt("key_anio", anio);
                    fragment.setArguments(bundle);
                    cambiarFragmento(fragment);
                }else if(which == 1){
                    //ver eventos
                    Fragment fragment = new ViewEventsActivity();
                    Bundle bundle = new Bundle();
                    bundle.putInt("key_dia", dia);
                    bundle.putInt("key_mes", mes);
                    bundle.putInt("key_año", anio);
                    fragment.setArguments(bundle);
                    cambiarFragmento(fragment);
                }else{
                    return;
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void cambiarFragmento(Fragment fragment){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
