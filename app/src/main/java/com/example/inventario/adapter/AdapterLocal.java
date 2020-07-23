package com.example.inventario.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.inventario.R;
import com.example.inventario.model.Local;
import java.util.List;

public class AdapterLocal extends RecyclerView.Adapter<AdapterLocal.MyViewHolder> {
    private List<Local> localList;
    private Context context;

    public AdapterLocal(List<Local> listLocal) {
        this.localList= listLocal;
        this.context = context;
    }
    @NonNull
    @Override
    public AdapterLocal.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_local,parent,false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterLocal.MyViewHolder holder, int position) {
        final Local local = localList.get(position);
        holder.nome.setText(local.getNome());
        holder.tipo.setText(local.getTipo());

        if(local.getTipo().equals("Sala de servidores")){
            holder.imageLocal.setImageResource(R.drawable.serverrooom);
        }else if(local.getTipo().equals("Linha de Produção")){
            holder.imageLocal.setImageResource(R.drawable.production);
        }else{
            holder.imageLocal.setImageResource(R.drawable.escritorio);
        }
    }

    @Override
    public int getItemCount() {
        return localList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nome, tipo;
        ImageView imageLocal;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageLocal = itemView.findViewById(R.id.image_local);
            nome = itemView.findViewById(R.id.tvNome);
            tipo = itemView.findViewById(R.id.tvTipo);
        }
    }
}
