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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventario.R;
import com.example.inventario.adapter.AdapterDispositivo;
import com.example.inventario.adapter.RecyclerItemClickListener;
import com.example.inventario.config.Firebase;
import com.example.inventario.model.Dispositivo;
import com.example.inventario.ui.FormDispositivoActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PesquisaFragment extends Fragment {
    private Spinner spinnerFiltro;
    private EditText editPesquisa;
    private View view;
    private Toolbar toolbar;
    private RecyclerView rvListarPesquisas;
    private List<Dispositivo> listDispositivo = new ArrayList<>();
    private AdapterDispositivo adapterDispositivo ;
    private DatabaseReference databaseReference;
    private ProgressBar progressBar;
    private TextView tvProcessando, tvListaVazia;

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_form, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_salvar:
                int selecao = spinnerFiltro.getSelectedItemPosition();
                String texto = editPesquisa.getText().toString();
                configurarAdapterRecyclerView();
           if(selecao == 1){
               pesquisarDispositivo(texto,"hostname");
           }if(selecao == 2){
               pesquisarDispositivo(texto,"ativo");
            }if(selecao == 3){
                pesquisarDispositivo(texto,"ip");
            }if(selecao == 4){
                pesquisarDispositivo(texto,"categoria");
            }if(selecao == 5){
                pesquisarDispositivo(texto,"so");
            }if(selecao == 6){
                pesquisarDispositivo(texto,"local");
            }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_pesquisa, container, false);
        inicializarComponentes();
        configurarToolbar();
        carregarSpinnerFiltros();
        return view;
    }
    private void configurarToolbar() {
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("");
        setHasOptionsMenu(true);
    }
    private void inicializarComponentes() {
        toolbar = view.findViewById(R.id.toolbarFragmentPesquisa);
        spinnerFiltro = view.findViewById(R.id.spinnerFiltro);
        editPesquisa = view.findViewById(R.id.etPesquisa);
        rvListarPesquisas = view.findViewById(R.id.rvListarPesquisa);
        progressBar = view.findViewById(R.id.progressBarPesquisa);
        tvProcessando = view.findViewById(R.id.tvProcessandoPesquisa);
        tvListaVazia = view.findViewById(R.id.tvListaVaziaPesquisa);
        tvListaVazia.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        tvProcessando.setVisibility(View.GONE);
        rvListarPesquisas.setVisibility(View.GONE);
    }
    private void carregarSpinnerFiltros(){
        ArrayAdapter<CharSequence> adapterFiltro = ArrayAdapter.createFromResource(getContext(),
                R.array.spinner_filtros, android.R.layout.simple_spinner_item);
        adapterFiltro.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFiltro.setAdapter(adapterFiltro);
    }
    private void configurarAdapterRecyclerView() {
        rvListarPesquisas.setLayoutManager(new LinearLayoutManager(getContext()));
        rvListarPesquisas.setHasFixedSize(true);
        adapterDispositivo = new AdapterDispositivo(listDispositivo);
        rvListarPesquisas.setAdapter(adapterDispositivo);
        configurarAcoesRecyclerView();
    }
    private void configurarAcoesRecyclerView() {
        rvListarPesquisas.addOnItemTouchListener(new RecyclerItemClickListener(
                getContext(),
                rvListarPesquisas,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Dispositivo dispositivoSelecionado = listDispositivo.get(position);
                        Intent i = new Intent(getContext(), FormDispositivoActivity.class);
                        i.putExtra("dispositivoSelecionado", dispositivoSelecionado);
                        startActivity(i);
                    }
                    @Override
                    public boolean onLongItemClick(View view, int position) {
                        final  Dispositivo dispositivoSelecionado = listDispositivo.get(position);
                        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext(),R.style.RoundedDialog);
                        dialog.setTitle("Deletar Dispositivo");
                        dialog.setMessage("Deseja Deletar\t"+dispositivoSelecionado.getHostname()+"?");
                        dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String id = dispositivoSelecionado.getId();
                                dispositivoSelecionado.excluir(id);
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
                        return false;
                    }
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    }
                }
        ));
    }
    private void pesquisarDispositivo(String texto, String atributo) {
        listDispositivo.clear();
        progressBar.setVisibility(View.VISIBLE);
        tvProcessando.setVisibility(View.VISIBLE);
        tvListaVazia.setVisibility(View.GONE);

        if (texto.length() > 0) {
            databaseReference = Firebase.getFirebaseDatabase()
                    .child("dispositivo");
            Query query = databaseReference.orderByChild(atributo).startAt(texto).endAt(texto + "\uf8ff");

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    listDispositivo.clear();
                    if (dataSnapshot.getValue() != null) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            listDispositivo.add(dataSnapshot1.getValue(Dispositivo.class));
                        }
                        Collections.reverse(listDispositivo);
                        adapterDispositivo.notifyDataSetChanged();
                        rvListarPesquisas.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        tvProcessando.setVisibility(View.GONE);
                        tvListaVazia.setVisibility(View.GONE);
                    } else {
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
    }
    private void exibirMensagem(String mensagem){
        Toast.makeText(getContext(), mensagem, Toast.LENGTH_SHORT).show();
    }
}