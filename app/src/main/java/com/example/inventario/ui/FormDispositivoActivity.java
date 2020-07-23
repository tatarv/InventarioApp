package com.example.inventario.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventario.R;
import com.example.inventario.config.Firebase;
import com.example.inventario.model.Dispositivo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FormDispositivoActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView titleToolbar;
    private EditText editHost, editAtivo, editIp;
    private Spinner spinnerCategoria, spinnerSO, spinnerLocal;
    private Dispositivo dispositivoSelecionado;
    private Dispositivo dispositivo;
    private Intent dados;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_form, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }if (item.getItemId() == R.id.menu_salvar) {
            validarCampos();
        }
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_dispositivo);
        inicializarComponentes();
        dados = getIntent();
        carregarSpinnerCategoria();
        carregarSpinnerSo();
        carregarSpinnerLocal();
        carregaDispositivo();
    }
    private void configurarToolbar(String nome) {
        setSupportActionBar(toolbar);
        titleToolbar.setText(nome);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private void inicializarComponentes(){
        toolbar = findViewById(R.id.toolbarFormDispositivo);
        titleToolbar = findViewById(R.id.tvToolbarTitleDispositivo);
        editHost = findViewById(R.id.etHostname);
        editAtivo = findViewById(R.id.etAtivo);
        editIp = findViewById(R.id.etIp);
        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        spinnerSO = findViewById(R.id.spinnerSo);
        spinnerLocal = findViewById(R.id.spinnerLocal);
        editHost.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
    }
    private void carregarSpinnerCategoria(){
        ArrayAdapter<CharSequence> adapterCategoria = ArrayAdapter.createFromResource(this,
                R.array.spinner_categorias, android.R.layout.simple_spinner_item);
        adapterCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(adapterCategoria);
    }
    private void carregarSpinnerSo(){
        ArrayAdapter<CharSequence> adapterSO = ArrayAdapter.createFromResource(this,
                R.array.spinner_sistemas, android.R.layout.simple_spinner_item);
        adapterSO.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSO.setAdapter(adapterSO);
    }
    public void carregarSpinnerLocal(){
        DatabaseReference databaseReference;
        databaseReference = Firebase.getFirebaseDatabase().child("local");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final List<String> locais = new ArrayList<String>();

                for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                    String localName = areaSnapshot.child("nome").getValue(String.class);
                    locais.add(localName);

                    ArrayAdapter<String> localAdapter = new ArrayAdapter<String>(FormDispositivoActivity.this, android.R.layout.simple_spinner_item, locais);
                    localAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerLocal.setAdapter(localAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private Dispositivo instaciarDispositivo(){
        String campoHost, campoAtivo, campoIp, campoCategoria, campoSo, campoLocal;
        int categoriaId, soId, localId;

        campoHost = editHost.getText().toString();
        campoAtivo = editAtivo.getText().toString();
        campoIp = editIp.getText().toString();
        campoCategoria = spinnerCategoria.getSelectedItem().toString();
        campoSo = spinnerSO.getSelectedItem().toString();
        campoLocal = spinnerLocal.getSelectedItem().toString();
        categoriaId = spinnerCategoria.getSelectedItemPosition();
        soId = spinnerSO.getSelectedItemPosition();
        localId = spinnerLocal.getSelectedItemPosition();

       Dispositivo dispositivo = new Dispositivo();
       dispositivo.setHostname(campoHost);
       dispositivo.setAtivo(campoAtivo);
       dispositivo.setIp(campoIp);
       dispositivo.setCategoria(campoCategoria);
       dispositivo.setSo(campoSo);
       dispositivo.setLocal(campoLocal);
       dispositivo.setCategoriaId(categoriaId);
       dispositivo.setSoId(soId);
       dispositivo.setLocalId(localId);

        return dispositivo;
    }
    private void validarCampos(){

       dispositivo = instaciarDispositivo();

       if(!dispositivo.getHostname().isEmpty() && !dispositivo.getAtivo().isEmpty() && !dispositivo.getIp().isEmpty()
       && !dispositivo.getCategoria().equals("Selecione uma categoria") && !dispositivo.getSo().equals("Selecione um sistema operacional")
       && !dispositivo.getLocal().equals("Selecione Um Local") && !dados.hasExtra("dispositivoSelecionado")){
            salvarDispositivo();
           exibirMensagem("Dados salvos com sucesso!");
       }else if(!dispositivo.getHostname().isEmpty() && !dispositivo.getAtivo().isEmpty() && !dispositivo.getIp().isEmpty()
                && !dispositivo.getCategoria().equals("Selecione uma categoria") && !dispositivo.getSo().equals("Selecione um sistema operacional")
                && !dispositivo.getLocal().equals("Selecione Um Local") && dados.hasExtra("dispositivoSelecionado")){
            String id = dispositivoSelecionado.getId();
            editarDispositivo(id);
            exibirMensagem("Dados editados com sucesso!");
        }else{
           exibirMensagem("Por favor preencha os campos obrigatórios");
       }

    }
    private void carregaDispositivo() {

        if (dados.hasExtra("dispositivoSelecionado")) {
            configurarToolbar("Editar Dispositivo");
            recuperarDados();
        } else {
            configurarToolbar("Cadastrar Dispositivo");
        }
    }
    private void recuperarDados(){
        Bundle bundle = getIntent().getExtras();
        dispositivoSelecionado = (Dispositivo) bundle.getSerializable("dispositivoSelecionado");

        int so = dispositivoSelecionado.getSoId();
        int  local = dispositivoSelecionado.getLocalId();
        int categoria = dispositivoSelecionado.getCategoriaId();

        editHost.setText(dispositivoSelecionado.getHostname());
        editAtivo.setText(dispositivoSelecionado.getAtivo());
        editIp.setText(dispositivoSelecionado.getIp());
        spinnerCategoria.setSelection(categoria);
        spinnerSO.setSelection(so);
        spinnerLocal.setSelection(local);
    }
    private void exibirMensagem(String mensagem){
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
    }
    private void salvarDispositivo(){
        dispositivo.salvar();
        finish();
    }
    private void editarDispositivo(String id){
        dispositivo.atualizar(id);
        finish();
    }
}
