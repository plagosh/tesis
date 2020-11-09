package cl.paulina.yotrabajoconpecs.ui.busqueda_pictograma_pdc;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
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
    public LinearLayout contenido, contentCaja, contentFrase;
    private ArrayList id_frase, url;
    public String id = "1";

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_layout, null);
        dialog.setContentView(view);

        gridView = view.findViewById(R.id.listviewFrase);
        contentCaja = view.findViewById(R.id.contentCaja);
        contentFrase = view.findViewById(R.id.contentFrase);

        id_frase = new ArrayList();
        url = new ArrayList();

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

            ImageButton imagen = new ImageButton(getContext());
            contenido = new LinearLayout(getContext());
            contenido.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            contenido.setOrientation(LinearLayout.HORIZONTAL);
            contenido.setPadding(5,5,5,5);
            imagen.setBackgroundResource(R.drawable.boton_rectangulo);
            imagen.setLayoutParams(new LinearLayout.LayoutParams(400, 400));
            imagen.setScaleType(ImageButton.ScaleType.FIT_CENTER);
            Picasso.get().load("https://yotrabajoconpecs.ddns.net/" + url.get(position).toString()).into(imagen);
            contenido.addView(imagen);
            TvImagenButton.addView(contenido);
            /*
            for(int i = 0; i < url.size(); i++) {
                String segundaFrase = id_frase.get(position).toString();
                if(segundaFrase.equals(id)) {
                    ImageButton imagen = new ImageButton(getContext());
                    contenido = new LinearLayout(getContext());
                    contenido.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    contenido.setOrientation(LinearLayout.HORIZONTAL);
                    contenido.setPadding(5,5,5,5);
                    imagen.setBackgroundResource(R.drawable.boton_rectangulo);
                    imagen.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
                    imagen.setScaleType(ImageButton.ScaleType.FIT_CENTER);
                    Picasso.get().load("https://yotrabajoconpecs.ddns.net/" + url.get(i).toString()).into(imagen);
                    contenido.addView(imagen);
                }
            }TvImagenButton.addView(contenido);*/

            //TvImagenButton.removeView(contenido);
            int ultimoIntId = Integer.parseInt(id);
            ultimoIntId++;
            id = ultimoIntId + "";

            TvImagenButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            return viewGroup;
        }
    }
}
