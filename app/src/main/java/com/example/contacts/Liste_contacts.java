package com.example.contacts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contacts.DAO.ContactDao;
import com.example.contacts.Model.Contact;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.widget.SearchView;

import com.example.contacts.adapters.MyAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.LinkedList;

public class Liste_contacts extends AppCompatActivity implements View.OnClickListener {

    FloatingActionButton fab_add;
    RecyclerView contactsRecycler;
    EditText barreRecherche;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    MyAdapter myAdapter;
    LinkedList<Contact> contacts;
     ProgressDialog mProgressDialog;
    ContactDao cda;
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

        barreRecherche.addTextChangedListener(new TextWatcher() {

              @Override
              public void afterTextChanged(Editable s) {

              }

              @Override
              public void beforeTextChanged(CharSequence s, int start, int count, int after) {

              }

              @Override
              public void onTextChanged(CharSequence s, int start, int before, int count) {
                  if (s.length() == 0){
                      myAdapter = new MyAdapter(contacts, Liste_contacts.this);
                      contactsRecycler.setAdapter(myAdapter);
                      }
                  if (s.length() != 0) {
                      LinkedList<Contact> contactsCopy=new LinkedList<Contact>();
                      System.out.println(s);
                      for (Contact contact : contacts) {
                          final String nom = contact.getNomContact().toLowerCase();
                          final String prenom = contact.getPrenomContact().toLowerCase();
                          final String service = contact.getServiceContact().toLowerCase();
                          if (nom.contains(s.toString().toLowerCase()) || prenom.contains(s.toString().toLowerCase()) || service.contains(s.toString().toLowerCase())) {
                              contactsCopy.add(contact);

                          }
                      }
                      myAdapter = new MyAdapter(contactsCopy, Liste_contacts.this);
                      contactsRecycler.setAdapter(myAdapter);

                  }
              }
          }
        );

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

    @SuppressLint("StaticFieldLeak")
    void getContacts(){

        new AsyncTask() {
            //exécuter des tâches avant le démarrage du thread
            //on a encore le droit d’accéder au thread principal du Gui
            protected void onPreExecute(){
                contacts= new LinkedList<Contact>();
                cda= new ContactDao();
                showProgressDialog();
            }

            //La tâches principale du thread
            //on a pas droit d‘accéder au composantes du thread principal du  GUI
            protected Object doInBackground(Object[] objects) {
                contacts.addAll(cda.getAllContacts());
            return null;}

            ////exécuter des taches pendant la réalisation de la tâche principale du thread
            //on a encore le droit d’accéder au thread principal du Gui
            protected void onProgressUpdate(Integer... progress) {

            }
            //exécuter des taches après la terminaison du thread courant
            ////on a encore le droit d’accéder au thread principal du Gui
            protected void onPostExecute(Object result) {
                contactsRecycler.setHasFixedSize(true);
                LinearLayoutManager layoutManager = new LinearLayoutManager(Liste_contacts.this);
                contactsRecycler.setLayoutManager(layoutManager);
                myAdapter = new MyAdapter(contacts, Liste_contacts.this);
                contactsRecycler.setAdapter(myAdapter);
                hideProgressDialog();
            }
        }.execute();

    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading.......");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }



}



