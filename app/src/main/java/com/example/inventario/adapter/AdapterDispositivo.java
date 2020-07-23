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
import com.example.inventario.model.Dispositivo;
import java.util.List;

public class AdapterDispositivo extends RecyclerView.Adapter<AdapterDispositivo.MyViewHolder>  {
    private List<Dispositivo> dispositivoList;
    private Context context;

    public AdapterDispositivo(List<Dispositivo> listDispositivo) {
        this.dispositivoList = listDispositivo;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterDispositivo.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_dispositivo,parent,false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterDispositivo.MyViewHolder holder, int position) {
        final Dispositivo dispositivo = dispositivoList.get(position);
        holder.hostname.setText(dispositivo.getHostname());
        holder.ip.setText(dispositivo.getIp());
        holder.local.setText(dispositivo.getLocal());

        if(dispositivo.getCategoria().equals("Servidor")){
            holder.imagedispositivo.setImageResource(R.drawable.server);
        } if(dispositivo.getCategoria().equals("Estação de trabalho")){
            holder.imagedispositivo.setImageResource(R.drawable.computer);
        }if(dispositivo.getCategoria().equals("Roteador")){
            holder.imagedispositivo.setImageResource(R.drawable.router);
        }if(dispositivo.getCategoria().equals("Switch")){
            holder.imagedispositivo.setImageResource(R.drawable.switchn);
        }
    }

    @Override
    public int getItemCount() {
        return dispositivoList.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView hostname, local, ip;
        ImageView imagedispositivo;

        public MyViewHolder(View itemView) {
            super(itemView);

            hostname = itemView.findViewById(R.id.tvHostname);
            ip = itemView.findViewById(R.id.tvIp);
            local = itemView.findViewById(R.id.tvLocal);
            imagedispositivo = itemView.findViewById(R.id.image_dispositivo);

        }
    }
}
