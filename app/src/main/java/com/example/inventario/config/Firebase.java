package com.example.inventario.config;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Firebase {
    private static DatabaseReference database;

    public static DatabaseReference getFirebaseDatabase(){
        if ( database == null ){
            database = FirebaseDatabase.getInstance().getReference();
        }
        return database;
    }
}
