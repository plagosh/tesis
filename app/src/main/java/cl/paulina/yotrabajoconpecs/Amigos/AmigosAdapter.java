package cl.paulina.yotrabajoconpecs.Amigos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cl.paulina.yotrabajoconpecs.R;
import cl.paulina.yotrabajoconpecs.ui.busqueda_pictograma_empleador.Mensajeria;


public class AmigosAdapter extends RecyclerView.Adapter<AmigosAdapter.HolderAmigos>{
    private List<AmigosAtributos> atributosList;
    private Context context;
    Bundle datos = new Bundle();

    public AmigosAdapter(List<AmigosAtributos> atributosList, Context context){
        this.atributosList = atributosList;
        this.context = context;
    }

    @Override
    public HolderAmigos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_amigos, parent, false);
        return new HolderAmigos(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderAmigos holder, int position) {
        holder.imageView.setImageResource(atributosList.get(position).getFotoDePerfil());
        holder.nombre.setText(atributosList.get(position).getNombre());
        holder.mensaje.setText(atributosList.get(position).getUltimoMensaje());
        holder.hora.setText(atributosList.get(position).getHora());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new Mensajeria();
                datos.putString("key_receptor", atributosList.get(position).getId());
                fragment.setArguments(datos);
                cambiarFragmento(fragment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return atributosList.size();
    }

    static class HolderAmigos extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView imageView;
        TextView nombre, mensaje, hora;

        public HolderAmigos(View itemView){
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardViewAmigos);
            imageView = (ImageView) itemView.findViewById(R.id.fotoDePerfilAmigos);
            nombre = (TextView) itemView.findViewById(R.id.nombreUsuarioAmigo);
            mensaje = (TextView) itemView.findViewById(R.id.mensajeAmigos);
            hora = (TextView) itemView.findViewById(R.id.horaAmigos);
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
