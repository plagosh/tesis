package cl.paulina.yotrabajoconpecs.ui.libro;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class AdapterFrase extends FragmentPagerAdapter {

    public AdapterFrase(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f = null;
        switch(position) {
            case 0:
                f = new frase_general(); //1
                break;
            case 1:
                f = new frase_consulta(); //2
                break;
            case 2:
                f = new frase_sugerencia();//v1
                break;
        }
        return f;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0){
            return "General";
        }else if(position == 1){
            return "Consultas";
        }else{
            return "Sugerencias";
        }
    }
}
