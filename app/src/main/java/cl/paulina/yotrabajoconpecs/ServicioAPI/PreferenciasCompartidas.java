package cl.paulina.yotrabajoconpecs.ServicioAPI;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class PreferenciasCompartidas {

    public void generarFoto(LinearLayout caja, LinearLayout layout, Context context, ArrayList pictos) {
        Log.e("GenerarFoto: ", "Ingreso a metodo");
        if (pictos.size() > 0) {
            Log.e("GenerarFotoTamañoLista ", String.valueOf(pictos.size()));
            for (int i = 0; pictos.size()> i; i++) {
                if(pictos.get(i) != "") {
                    Log.e("GenerarFotoSecuencia ", "" + pictos.get(i));
                    ImageButton imagen = new ImageButton(context);
                    imagen.setLayoutParams(new LinearLayout.LayoutParams(600, 600));
                    imagen.setScaleType(ImageButton.ScaleType.CENTER_CROP);
                    imagen.setBackgroundColor(0xFFFFFF);
                    imagen.setPadding(10, 10, 10, 10);
                    imagen.setId(i);
                    Picasso.get().load("https://api.arasaac.org/api/pictograms/" + pictos.get(i)).into(imagen);
                    caja.addView(imagen);
                }
            }
            layout.addView(caja);
        }
    }
    public void deleteOneData(ArrayList pictos, int id){
        for ( int i = 0;pictos.size() > i; ++i ){
            if(i == id){
                Log.e("borrarElemento"," "+pictos.get(id));
                pictos.remove(i);
            }
        }
    }
    public void saveData(ArrayList pictos, Context context,String key) {
        Log.e("Guardar: ","Accede al metodo de GuardarDatos");
        SharedPreferences.Editor spe = context.getSharedPreferences("data",Context.MODE_PRIVATE).edit();
        StringBuilder sb = new StringBuilder();
        int i;
        for (i = 0; i < pictos.size(); i++) {
            sb.append( ((i == 0) ? "" : ";")  + pictos.get(i));
        }
        spe.putString(key, sb.toString());
        spe.commit();
    }
    public void loadData(ArrayList pictos, Context context,String key) {
        Log.e("Cargar: ","Accede al metodo de CargarDatos");
        SharedPreferences sp = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        String countryList = sp.getString(key,"");
        for (String country : countryList.split(";")) {
            if(country != ""){
                Log.e("Cargar: ", country);
                pictos.add(country);
            }
        }
    }
    public void deleteData(ArrayList pictos){
        if(pictos.size() >= 0){
            pictos.clear();
        }
    }

}