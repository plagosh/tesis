package cl.paulina.yotrabajoconpecs.MisClases.LibroPDC;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.tabs.TabLayout;

import cl.paulina.yotrabajoconpecs.R;

public class Frase extends BottomSheetDialogFragment {
    public LinearLayout contentCaja;
    public Fragment fragment;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog (Dialog dialog,int style) {
        //super.setupDialog(dialog, style);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.frase, null);
        dialog.setContentView(view);
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewLayout);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(new AdapterFrase(getActivity().getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0){
                    dialog.setTitle("Frase general");
                }else if(position == 1){
                    dialog.setTitle("Frase consulta");
                }else if(position == 2){
                    dialog.setTitle("Frase sugerencia");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        contentCaja = view.findViewById(R.id.contentCaja);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) view.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    String state = "";

                    switch (newState) {
                        case BottomSheetBehavior.STATE_DRAGGING: {
                            state = "DRAGGING";
                            break;
                        }
                        case BottomSheetBehavior.STATE_SETTLING: {
                            state = "SETTLING";
                            break;
                        }
                        case BottomSheetBehavior.STATE_EXPANDED: {
                            state = "EXPANDED";
                            break;
                        }
                        case BottomSheetBehavior.STATE_COLLAPSED: {
                            state = "COLLAPSED";
                            break;
                        }
                        case BottomSheetBehavior.STATE_HIDDEN: {
                            dismiss();
                            state = "HIDDEN";
                            break;
                        }
                    }
                    //Toast.makeText(getContext(), "Bottom Sheet State Changed to: " + state, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                    String state = "";
                }
            });
        }
    }
}
