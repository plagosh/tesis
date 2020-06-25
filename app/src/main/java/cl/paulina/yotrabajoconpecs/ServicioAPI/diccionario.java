package cl.paulina.yotrabajoconpecs.ServicioAPI;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import cl.paulina.yotrabajoconpecs.modelo.Pictograma;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class diccionario extends Fragment{
    public List<Pictograma> pr;
    public String urlAPI = "https://api.arasaac.org/api/";
    public Retrofit retrofit;
    public String TAG ="Estado API: ";
    public String locale = "es";

    public void actualizarDiccionario(ArrayList palabras, int i) {
        //https://api.arasaac.org/api/keywords/es
        for( i = 0; i < 101; i++){
            int searchId = i;
            retrofit = new Retrofit.Builder()
                    .baseUrl(urlAPI)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ArasaacAPI service = retrofit.create(ArasaacAPI.class);
            Call<List<Pictograma>> call = service.obtenerListaId(locale, searchId);

            call.enqueue(new Callback<List<Pictograma>>() {
                @Override
                public void onResponse(Call<List<Pictograma>> call, Response<List<Pictograma>> response) {
                    if (response.body() != null) {
                        pr = response.body();
                        String respuesta = pr.get(0).getKeyword();
                        Log.e("GenerarFotoSecuencia ", "" + respuesta);
                        palabras.add(respuesta);
                    }
                }
                @Override
                public void onFailure(Call<List<Pictograma>> call, Throwable t) {
                    Log.e(TAG, "No conectado");
                }
            });
        }


    }
}
