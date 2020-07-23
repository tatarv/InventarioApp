package com.example.inventario.fragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventario.R;
import com.example.inventario.adapter.AdapterLocal;
import com.example.inventario.adapter.RecyclerItemClickListener;
import com.example.inventario.config.Firebase;
import com.example.inventario.model.Local;
import com.example.inventario.ui.FormLocalActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LocalFragment extends Fragment {
    private View view;
    private Toolbar toolbar;
    private RecyclerView rvListarLocal;
    private List<Local> listLocal = new ArrayList<>();
    private AdapterLocal adapterLocal;
    private DatabaseReference databaseReference;
    private ProgressBar progressBar;
    private TextView tvProcessando, tvListaVazia,tvQtd;

    public LocalFragment() {
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_cadastrar:
                chamarFormNovoLocal();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_local, container, false);
        inicializarComponentes();
        configurarToolbar();
        configurarAdapterRecyclerView();
        recuperarLista();
        return view;
    }
    private void configurarToolbar() {
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("");
        setHasOptionsMenu(true);
    }

    private void chamarFormNovoLocal() {
        Intent intenTelaCadastro = new Intent(getContext(), FormLocalActivity.class);
        startActivity(intenTelaCadastro);
    }

    private void inicializarComponentes() {
        toolbar = view.findViewById(R.id.toolbarFragmentLocal);
        tvQtd = view.findViewById(R.id.textViewQtdLocal);
        rvListarLocal = view.findViewById(R.id.rvListarLocal);
        progressBar = view.findViewById(R.id.progressBarLocal);
        tvProcessando = view.findViewById(R.id.tvProcessandoLocal);
        tvListaVazia = view.findViewById(R.id.tvListaVaziaLocal);
        tvListaVazia.setVisibility(View.GONE);
    }

    private void configurarAdapterRecyclerView() {
        rvListarLocal.setLayoutManager(new LinearLayoutManager(getContext()));
        rvListarLocal.setHasFixedSize(true);
        adapterLocal = new AdapterLocal(listLocal);
        rvListarLocal.setAdapter(adapterLocal);

        configurarAcoesRecyclerView();
    }
    private void recuperarLista() {
        databaseReference = Firebase.getFirebaseDatabase()
                .child("local");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listLocal.clear();
                if (dataSnapshot.getValue()!=null) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        listLocal.add(dataSnapshot1.getValue(Local.class));
                    }
                    Collections.reverse(listLocal);
                    adapterLocal.notifyDataSetChanged();
                    tvQtd.setText(""+listLocal.size());
                    progressBar.setVisibility(View.GONE);
                    tvProcessando.setVisibility(View.GONE);
                    tvListaVazia.setVisibility(View.GONE);
                }else{
                    progressBar.setVisibility(View.GONE);
                    tvProcessando.setVisibility(View.GONE);
                    tvListaVazia.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    private void configurarAcoesRecyclerView() {
        rvListarLocal.addOnItemTouchListener(new RecyclerItemClickListener(
                getContext(),
                rvListarLocal,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Local localSelecionado = listLocal.get(position);
                        Intent i = new Intent(getContext(), FormLocalActivity.class);
                        i.putExtra("localSelecionado", localSelecionado);
                        startActivity(i);
                    }
                    @Override
                    public boolean onLongItemClick(View view, int position) {
                        final Local localSelecionado = listLocal.get(position);
                        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext(),R.style.RoundedDialog);
                        dialog.setTitle("Deletar Local");
                        dialog.setMessage("Deseja Deletar\t"+localSelecionado.getNome()+"?");
                        dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String id = localSelecionado.getId();
                                localSelecionado.excluir(id);
                                exibirMensagem("Dados deletados com sucesso!");
                            }
                        });
                        dialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                exibirMensagem("Operação cancelada!");
                            }
                        });
                        dialog.show();
                        return true;
                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    }
                }
        ));
    }
    private void exibirMensagem(String mensagem){
        Toast.makeText(getContext(), mensagem, Toast.LENGTH_SHORT).show();
    }
}