package cl.paulina.yotrabajoconpecs.ui.busqueda_pictograma_pdc;
// aqui estuve yo 2 asdasdasdasdadasdasdas
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import cl.paulina.yotrabajoconpecs.R;
import cz.msebera.android.httpclient.Header;

public class examplebuttonsheetdialog extends BottomSheetDialogFragment {
    private GridView gridView;
    public LinearLayout contentCaja;
    private ArrayList id_frase, url, id_imagen, agregandoFrase;
    public Fragment fragment;
    Bundle datos;
    public int id = 1;

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_layout, null);
        dialog.setContentView(view);

        gridView = view.findViewById(R.id.listviewFrase);
        contentCaja = view.findViewById(R.id.contentCaja);

        id_frase = new ArrayList();
        url = new ArrayList();
        agregandoFrase = new ArrayList();
        id_imagen = new ArrayList();
        datos = new Bundle();

        descargarDatos();

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
                }
            });
        }
    }

    private void descargarDatos(){
        url.clear();
        id_frase.clear();
        id_imagen.clear();
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("https://yotrabajoconpecs.ddns.net/query7.php", new AsyncHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200){
                    progressDialog.dismiss();
                    try{
                        JSONArray jsonarray = new JSONArray(new String(responseBody));
                        for(int i = 0; i < jsonarray.length(); i++){
                            id_frase.add(jsonarray.getJSONObject(i).getString("frase_detallefrase"));
                            url.add(jsonarray.getJSONObject(i).getString("url"));
                            id_imagen.add(jsonarray.getJSONObject(i).getString("imagen_id_imagen"));
                        }gridView.setAdapter(new CustomAdapter(getContext()));
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Context context = getContext();
                CharSequence text = "Conexión fallida";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });
    }

    private class CustomAdapter extends BaseAdapter {
        Context ctx;
        LayoutInflater layoutInflater;
        LinearLayout TvImagenButton;

        @RequiresApi(api = Build.VERSION_CODES.M)
        public CustomAdapter(Context applicationContext){
            this.ctx = applicationContext;
            layoutInflater = (LayoutInflater)this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount(){
            return id_frase.size();
        }

        @Override
        public Object getItem(int position){
            return position;
        }

        @Override
        public long getItemId(int position){
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.list_view_frase, null);
            TvImagenButton = (LinearLayout) viewGroup.findViewById(R.id.tv_imagen_frase);
            agregandoFrase.clear();
            String postitionGrid = id_frase.get(position).toString();
            int ultimo = id_frase.size();
            String anterior = id_frase.get(position-1).toString();
            String actual = id_frase.get(position).toString();
            int anteriornum = Integer.parseInt(anterior);
            if(anteriornum != ultimo){
                if(!anterior.equals(actual)){
                    for(int i = 0; i < url.size(); i++) {
                        String recorrido = id_frase.get(i).toString();
                        if(recorrido.equals(postitionGrid)) {
                            agregandoFrase.add(id_imagen.get(i).toString());
                            ImageButton imagen = new ImageButton(getContext());
                            imagen.setBackgroundResource(R.drawable.boton_rectangulo);
                            imagen.setLayoutParams(new LinearLayout.LayoutParams(130, 130));
                            imagen.setScaleType(ImageButton.ScaleType.FIT_CENTER);
                            Picasso.get().load("https://yotrabajoconpecs.ddns.net/" + url.get(i).toString()).into(imagen);
                            TvImagenButton.addView(imagen);
                        }
                    }
                }
            }else{
                ImageButton imagen = new ImageButton(getContext());
                imagen.setBackgroundResource(R.drawable.boton_rectangulo);
                imagen.setLayoutParams(new LinearLayout.LayoutParams(130, 130));
                imagen.setScaleType(ImageButton.ScaleType.FIT_CENTER);
                Picasso.get().load("https://yotrabajoconpecs.ddns.net/" + url.get(position).toString()).into(imagen);
                TvImagenButton.addView(imagen);
            }
            id++;
            TvImagenButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    datos.putString("url_", id_imagen.get(position).toString());
                    Toast.makeText(getContext(), id_imagen.get(position).toString(), Toast.LENGTH_SHORT).show();
                    for(int j = 0; j < agregandoFrase.size(); j++){
                        datos.putString("url_" + j, agregandoFrase.get(j).toString());
                    }
                    //fragment.setArguments(datos);
                }
            });
            return viewGroup;
        }
    }
}
