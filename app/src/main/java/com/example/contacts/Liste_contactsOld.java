package com.example.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contacts.Model.Contact;
import com.example.contacts.adapters.MyAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;

public class Liste_contactsOld extends AppCompatActivity implements View.OnClickListener {

    FloatingActionButton fab_add;
    RecyclerView contactsRecycler;
    EditText barreRecherche;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    LinkedList<Contact> contacts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_contacts);

        contactsRecycler=(RecyclerView) findViewById(R.id.list_contacts);
        barreRecherche=(EditText) findViewById(R.id.hint);
        fab_add=(FloatingActionButton) findViewById(R.id.fab_add);
        fab_add.setOnClickListener(this);
        db = FirebaseFirestore.getInstance();
        mAuth= FirebaseAuth.getInstance();

    //
    }

    @Override
    protected void onResume() {
        super.onResume();
        getContacts();

    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.fab_add){
            Intent myintent= new Intent(this, Ajouter_contact.class);
            startActivity(myintent);
        }
    }


    void getContacts(){
        contacts= new LinkedList<Contact>();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        DocumentReference docRef = db.collection("user").document(currentUser.getEmail());
        docRef.collection("contact").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Contact c= new Contact(document.get("nom").toString(),document.get("prenom").toString(),document.get("service").toString(),document.get("email").toString(),document.get("url").toString());
                                contacts.add(c);


                            }


                            // use this setting to improve performance if you know that changes
// in content do not change the layout size of the RecyclerView
                            contactsRecycler.setHasFixedSize(true);

                            LinearLayoutManager layoutManager = new LinearLayoutManager(Liste_contactsOld.this);
                            contactsRecycler.setLayoutManager(layoutManager);

                            MyAdapter myAdapter = new MyAdapter(contacts, Liste_contactsOld.this);

                            contactsRecycler.setAdapter(myAdapter);

                        } else {
                            Log.d("not ok", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }



}



