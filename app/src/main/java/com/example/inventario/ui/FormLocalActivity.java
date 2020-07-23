package com.example.inventario.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventario.R;
import com.example.inventario.model.Local;

public class FormLocalActivity extends AppCompatActivity {
    private EditText editNome, editDescricao;
    private Spinner spinnerTipo;
    private Toolbar toolbar;
    private TextView titleToolbar;
    private Local local;
    private Local localSelecionado;
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
        setContentView(R.layout.activity_form_local);
        inicializarComponentes();
        carregarSpinnerTipo();
        dados = getIntent();
        carregaLocal();
    }
    private void configurarToolbar(String nome) {
        setSupportActionBar(toolbar);
        titleToolbar.setText(nome);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private void inicializarComponentes(){
        toolbar = findViewById(R.id.toolbarFragmentFormLocal);
        titleToolbar = findViewById(R.id.tvToolbarTitleFormLocal);
        editNome = findViewById(R.id.etNome);
        editDescricao = findViewById(R.id.etDescricao);
        spinnerTipo = findViewById(R.id.spinnerTipo);
    }
    private void carregarSpinnerTipo(){
        ArrayAdapter<CharSequence> adapterTipo = ArrayAdapter.createFromResource(this,
                R.array.spinner_tipos, android.R.layout.simple_spinner_item);
        adapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(adapterTipo);
    }
    private Local instanciarLocal(){
        String campoNome,campoDescricao, campoTipo;
        int tipoId;

        campoNome = editNome.getText().toString();
        campoDescricao = editDescricao.getText().toString();
        campoTipo = spinnerTipo.getSelectedItem().toString();
        tipoId = spinnerTipo.getSelectedItemPosition();

        Local local = new Local();
        local.setNome(campoNome);
        local.setDescricao(campoDescricao);
        local.setTipo(campoTipo);
        local.setTipoId(tipoId);

        return local;
    }
    private void validarCampos(){
        local= instanciarLocal();

        if ((!local.getNome().isEmpty()&& !local.getDescricao().isEmpty() && !local.getTipo().equals("Selecione um tipo")
                && !dados.hasExtra("localSelecionado"))){
            salvarLocal();
            exibirMensagem("Dados salvos com sucesso!");
        } else if (!local.getNome().isEmpty() && !local.getDescricao().isEmpty() && !local.getTipo().equals("Selecione um tipo")  &&
                dados.hasExtra("localSelecionado")){
            String id = localSelecionado.getId();
            editarLocal(id);
            exibirMensagem("Dados editados com sucesso!");
        }else{
            exibirMensagem("Por favor preencha os campos obrigatórios");
        }
    }
    private void exibirMensagem(String mensagem){
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
    }
    private void salvarLocal(){
        local.salvar();
        finish();
    }
    private void editarLocal(String id){
        local.atualizar(id);
        finish();
    }
    private void carregaLocal() {
        if (dados.hasExtra("localSelecionado")) {
            configurarToolbar("Editar Local");
            recuperarDados();
        } else {
           configurarToolbar("Cadastrar Local");
        }
    }
    private void recuperarDados(){
        Bundle bundle = getIntent().getExtras();
        localSelecionado = (Local) bundle.getSerializable("localSelecionado");
        int tipoId = localSelecionado.getTipoId();
        editNome.setText(localSelecionado.getNome());
        editDescricao.setText(localSelecionado.getDescricao());
        spinnerTipo.setSelection(tipoId);
    }
}