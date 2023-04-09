package com.example.contacts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.contacts.Model.Contact;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Detail_contact extends AppCompatActivity implements View.OnClickListener {
    Contact contact;
    TextView identification;
    TextView service;
    ImageView photo;

    ImageView call;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_contact);
        contact= (Contact) getIntent().getSerializableExtra("contact");

        identification=(TextView) findViewById(R.id.identification_contact);
        service=(TextView) findViewById(R.id.service_contact);
        photo=(ImageView) findViewById(R.id.photo_contact);
        call=(ImageView) findViewById(R.id.call);


        call.setOnClickListener(this);
        identification.setText(contact.getNomContact()+" "+ contact.getPrenomContact());
        service.setText(contact.getServiceContact());


        // Reference to an image file in Cloud Storage
        StorageReference storageReference =
                FirebaseStorage.getInstance().getReferenceFromUrl(contact.getImg_url());
// Download directly from StorageReference using Glide
// (See MyAppGlideModule for Loader registration)
        Glide.with(this /* context */)
                .load(storageReference)
                .into(photo);


    }


    @Override
    public void onClick(View view) {
       if(view.getId()==R.id.call)
        {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + contact.getTel()));//change the number
            startActivity(callIntent);
        }

    }
}