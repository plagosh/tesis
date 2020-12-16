package cl.paulina.yotrabajoconpecs.MisClases.LibroPDC;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class AdapterFrase extends FragmentPagerAdapter {

    public AdapterFrase(@NonNull FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return new frase_consulta();
        }else if(position == 1){
            return new frase_general();
        }else if(position == 2){
            return new frase_sugerencia();
        }return null;
    }

    @Override
    public int getCount() {
        return 3;
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
