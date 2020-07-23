package com.example.inventario.model;

import com.example.inventario.config.Firebase;
import com.google.firebase.database.DatabaseReference;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Dispositivo implements Serializable {
    private String id;
    private String hostname;
    private String ativo;
    private String ip;
    private String categoria;
    private String so;
    private String local;
    private int categoriaId;
    private int soId;
    private int localId;

    public Dispositivo() {
        DatabaseReference reference = Firebase.getFirebaseDatabase()
                .child("dispositivo");
        setId(reference.push().getKey());
    }
    public void salvar(){
        DatabaseReference databaseReference = Firebase.getFirebaseDatabase();
        DatabaseReference workstation = databaseReference.child("dispositivo").child(getId());
        workstation.setValue(this);
    }

    public void atualizar(String id1){
        DatabaseReference databaseReference = Firebase.getFirebaseDatabase();
        DatabaseReference dispositivoRef = databaseReference
                .child("dispositivo")
                .child(id1);
        Map<String, Object> valoresDispositivo = converterParaMap();
        dispositivoRef.updateChildren( valoresDispositivo );
    }
    public Map<String, Object> converterParaMap(){

        HashMap<String, Object> dispositivoMap = new HashMap<>();
        dispositivoMap.put("hostname", getHostname() );
        dispositivoMap.put("ativo", getAtivo() );
        dispositivoMap.put("ip",getIp());
        dispositivoMap.put("categoria", getCategoria() );
        dispositivoMap.put("so", getSo() );
        dispositivoMap.put("local", getLocal() );
        dispositivoMap.put("categoriaId", getCategoriaId() );
        dispositivoMap.put("soId", getSoId() );
        dispositivoMap.put("localId", getLocalId() );
        return dispositivoMap;
    }

    public void excluir(String id2){
        DatabaseReference workstationRef = Firebase.getFirebaseDatabase()
                .child("dispositivo")
                .child(id2);
        workstationRef.removeValue();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getAtivo() {
        return ativo;
    }

    public void setAtivo(String ativo) {
        this.ativo = ativo;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getSo() {
        return so;
    }

    public void setSo(String so) {
        this.so = so;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public int getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(int categoriaId) {
        this.categoriaId = categoriaId;
    }

    public int getSoId() {
        return soId;
    }

    public void setSoId(int soId) {
        this.soId = soId;
    }

    public int getLocalId() {
        return localId;
    }

    public void setLocalId(int localId) {
        this.localId = localId;
    }
}
