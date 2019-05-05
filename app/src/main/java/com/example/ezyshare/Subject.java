package com.example.ezyshare;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

public class Subject extends AppCompatActivity {
    String x,m_Text;
    TextView add;
    Uri fileuri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        FirebaseApp.initializeApp(getApplicationContext());
        Intent intent= getIntent();
        x=intent.getStringExtra("subject");
        Toast.makeText(Subject.this,"SUBJECT: "+x,Toast.LENGTH_LONG).show();
        add=findViewById(R.id.add);

        init();
    }

    private void init() {
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(getApplicationContext());
                View promptsView = li.inflate(R.layout.inputsubject, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        Subject.this);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText Type = promptsView
                        .findViewById(R.id.type);
                final EditText Name = promptsView
                        .findViewById(R.id.name);
                final ImageView Upload = promptsView
                        .findViewById(R.id.upload);
                final TextView done=promptsView.findViewById(R.id.done);

                Upload.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        done.setText("Done");
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("application/pdf");
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        startActivityForResult(intent,1);


                    }
                });

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        if(Type.getText().toString().equals("")||Name.getText().toString().equals("")){
                                            Toast.makeText(getApplicationContext(),"Please Fill the fields",Toast.LENGTH_LONG).show();
                                        }
                                        else{
                                            if(Type.getText().toString().equals("Notes"))
                                                upload("Notes",Name.getText().toString());
                                        }
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();
            }
        });
    }

    private void upload(String Type,String Name) {
        Log.d("IIIIIIII","Testingg");
        Toast.makeText(getApplicationContext(), "UPLOADING FILE", Toast.LENGTH_SHORT).show();
        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageRef = storage.getReference();
        StorageReference fileRef = storageRef.child(x).child(Type+"/"+Name+".pdf");

        fileRef.putFile(fileuri);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        fileuri = data.getData();

        Log.d("IIIIIIIIIIIIIIIIIIIIII",fileuri.toString());
        super.onActivityResult(requestCode, resultCode, data);
    }

}
