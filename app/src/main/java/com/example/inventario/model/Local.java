package com.example.inventario.model;

import com.example.inventario.config.Firebase;
import com.google.firebase.database.DatabaseReference;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Local implements Serializable {
    private String id;
    private String nome;
    private String tipo;
    private int tipoId;
    private String descricao;

    public Local() {
        DatabaseReference reference = Firebase.getFirebaseDatabase()
                .child("local");
        setId(reference.push().getKey());
    }
    public void salvar(){
        DatabaseReference databaseReference = Firebase.getFirebaseDatabase();
        DatabaseReference workstation = databaseReference.child("local").child(getId());
        workstation.setValue(this);
    }

    public void atualizar(String id1){
        DatabaseReference databaseReference = Firebase.getFirebaseDatabase();
        DatabaseReference localRef = databaseReference
                .child("local")
                .child(id1);
        Map<String, Object> valoresLocal = converterParaMap();
        localRef.updateChildren( valoresLocal );
    }
    public Map<String, Object> converterParaMap(){

        HashMap<String, Object> localMap = new HashMap<>();
        localMap.put("nome", getNome() );
        localMap.put("tipo", getTipo() );
        localMap.put("descricao", getDescricao() );
        localMap.put("tipoId", getTipoId() );

        return localMap;
    }

    public void excluir(String id2){
        DatabaseReference workstationRef = Firebase.getFirebaseDatabase()
                .child("local")
                .child(id2);
        workstationRef.removeValue();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getTipoId() {
        return tipoId;
    }

    public void setTipoId(int tipoId) {
        this.tipoId = tipoId;
    }
}
