package cl.paulina.yotrabajoconpecs.ui.busqueda_pictograma_pdc;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import cl.paulina.yotrabajoconpecs.R;
import cl.paulina.yotrabajoconpecs.ui.busqueda_pictograma_empleador.MensajeDeTexto;
import cz.msebera.android.httpclient.Header;

public class messageAdapter extends RecyclerView.Adapter<messageAdapter.MensajesViewHolder> {
    private List<MensajeDeTexto> mensajedetexto;
    private Context context;
    public LinearLayout contenido;
    public String u;
    public ArrayList lista;

    public messageAdapter(List<MensajeDeTexto> mensajedetexto, Context context) {
        this.mensajedetexto = mensajedetexto;
        this.context = context;
    }

    @Override
    public messageAdapter.MensajesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardviewmensajes,parent,false);
        return new MensajesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull messageAdapter.MensajesViewHolder holder, int position) {
        RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) holder.cardView.getLayoutParams();
        FrameLayout.LayoutParams fl = (FrameLayout.LayoutParams) holder.mensajeBG.getLayoutParams();
        if(mensajedetexto.get(position).getTipoMensaje() == 1){
            holder.mensajeBG.setBackgroundResource(R.drawable.in_message_bg);
            rl.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            rl.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            fl.gravity = Gravity.RIGHT;
        }else if(mensajedetexto.get(position).getTipoMensaje() == 2){
            holder.mensajeBG.setBackgroundResource(R.drawable.out_message_bg);
            rl.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            rl.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            fl.gravity = Gravity.LEFT;
        }
        holder.cardView.setLayoutParams(rl);
        holder.mensajeBG.setLayoutParams(fl);

        lista = new ArrayList();

        String id = mensajedetexto.get(position).getId();

        String urlCompleto[] = id.split(" ");
        holder.TvMensaje.removeAllViews();
        for(int i = 0; i < urlCompleto.length; i++){
            lista.add(urlCompleto[i]);
            ImageButton imagen = new ImageButton(context);
            holder.TvMensaje.setGravity(Gravity.CENTER);
            imagen.setBackgroundResource(R.drawable.boton_rectangulo);
            imagen.setLayoutParams(new LinearLayout.LayoutParams(130, 130));
            imagen.setScaleType(ImageButton.ScaleType.FIT_CENTER);
            Picasso.get().load("https://yotrabajoconpecs.ddns.net/" + urlCompleto[i]).into(imagen);
            holder.TvMensaje.addView(imagen);
        }
        holder.TvHora.setText(mensajedetexto.get(position).getHoraDelMensaje());
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) holder.cardView.getBackground().setAlpha(0);
        else holder.cardView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
    }

    @Override
    public int getItemCount() {
        return mensajedetexto.size();
    }

    static class MensajesViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        LinearLayout mensajeBG, TvMensaje;
        TextView TvHora;

        MensajesViewHolder(View itemView){
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cvMensajes);
            mensajeBG = (LinearLayout) itemView.findViewById(R.id.mensajeBG);
            TvMensaje = (LinearLayout) itemView.findViewById(R.id.crearCaja);
            TvHora = (TextView) itemView.findViewById(R.id.msHora);
        }
    }
}
