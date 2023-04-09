package com.example.contacts.DAO;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.contacts.Liste_contacts;
import com.example.contacts.Model.Contact;
import com.example.contacts.adapters.MyAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;

public class ContactDao {


    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    public ContactDao(){
        db = FirebaseFirestore.getInstance();
        mAuth= FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
    }

    //on doit s'assurer que le chargement des donnees est terminé avant de retourner
    // le resultast final
    public LinkedList<Contact> getAllContacts(){
        LinkedList<Contact>contacts= new LinkedList<Contact>();
        final boolean[] terminated = new boolean[1];
        terminated[0] = false;
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
                            terminated[0] = true;
                        } else {
                            Log.d("not ok", "Error getting documents: ", task.getException());
                        }
                    }
                });

        while (  terminated[0] == false)
            System.out.println("...");;
        return contacts;

    }

    // ass other methods of CRUD


}
