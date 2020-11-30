package cl.paulina.yotrabajoconpecs.Stack;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import cl.paulina.yotrabajoconpecs.R;

public class StackAdapter extends RecyclerView.Adapter<StackAdapter.HolderStack>{
    private List<StackAtributos> atributosList;
    private Context context;

    public StackAdapter(List<StackAtributos> atributosList, Context context){
        this.atributosList = atributosList;
        this.context = context;
    }

    @Override
    public StackAdapter.HolderStack onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_stack, parent, false);
        return new StackAdapter.HolderStack(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StackAdapter.HolderStack holder, int position) {
        holder.imageView.setImageResource(atributosList.get(position).getFotoDePerfil());
        holder.nombre.setText(atributosList.get(position).getNombre());
        holder.tarea.setText(atributosList.get(position).getTarea());
        holder.hora.setText(atributosList.get(position).getHora());
    }

    @Override
    public int getItemCount() {
        return atributosList.size();
    }

    static class HolderStack extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView imageView;
        TextView nombre, tarea, hora;

        public HolderStack(View itemView){
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardViewStack);
            imageView = (ImageView) itemView.findViewById(R.id.fotoDePerfilPDC);
            nombre = (TextView) itemView.findViewById(R.id.nombreStack);
            tarea = (TextView) itemView.findViewById(R.id.nombreTarea);
            hora = (TextView) itemView.findViewById(R.id.horaTarea);
        }
    }

    public void cambiarFragmento(Fragment fragment){
        FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
