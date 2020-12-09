package cl.paulina.yotrabajoconpecs.MisClases.LibroPDC;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;

import cl.paulina.yotrabajoconpecs.R;

public class frase_sugerencia extends Fragment {
    private GridView gridView;
    public LinearLayout contentCaja;
    private ArrayList id_frase;
    private ArrayList url;
    private ArrayList id_imagen;
    private ArrayList agregandoFrase;
    public ArrayList glosa;
    public Fragment fragment;
    Bundle datos;
    BottomSheetBehavior sheetBehavior;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login, container, false);

        return view;
    }
}
