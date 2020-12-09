package cl.paulina.yotrabajoconpecs.MisClases.LibroEmpleador;

import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cl.paulina.yotrabajoconpecs.R;

public class MensajesAdapter extends RecyclerView.Adapter<MensajesAdapter.MensajesViewHolder> {
    private List<MensajeDeTexto> mensajedetexto;
    private Context context;
    public MensajesAdapter(List<MensajeDeTexto> mensajedetexto, Context context) {
        this.mensajedetexto = mensajedetexto;
        this.context = context;
    }

    @Override
    public MensajesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_mensajes,parent,false);
        return new MensajesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MensajesViewHolder holder, int position) {
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

        holder.TvMensaje.setText(mensajedetexto.get(position).getMensaje());
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
        LinearLayout mensajeBG;
        TextView TvMensaje, TvHora;
        MensajesViewHolder(View itemView){
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cvMensajes);
            mensajeBG = (LinearLayout) itemView.findViewById(R.id.mensajeBG);
            TvMensaje = (TextView) itemView.findViewById(R.id.msTexto);
            TvHora = (TextView) itemView.findViewById(R.id.msHora);
        }
    }
}
