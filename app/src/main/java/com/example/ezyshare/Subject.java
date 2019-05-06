package com.example.ezyshare;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
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

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Subject extends AppCompatActivity {
    String x,m_Text;
    TextView add;
    Uri fileuri;
    Uri downloadUri;
    ArrayList<datatype2> str_notes;
    ArrayList<datatype2> str_question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        FirebaseApp.initializeApp(getApplicationContext());
        str_question=new ArrayList();
        str_notes=new ArrayList();
        Intent intent= getIntent();
        x=intent.getStringExtra("subject");
        Toast.makeText(Subject.this,"SUBJECT: "+x,Toast.LENGTH_LONG).show();
        add=findViewById(R.id.add);

        init();
    }

    private void init() {
        final DatabaseReference otherUsers = FirebaseDatabase.getInstance().getReference(x);

        otherUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap subject = (HashMap) dataSnapshot.getValue();

                 Set<String> keys = subject.keySet();

                for (final String key : keys) {

                    if (key.equals("Notes")) {

                        otherUsers.child(key).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                HashMap hm = (HashMap) dataSnapshot.getValue();

                                Set<String> notes_today=hm.keySet();
                                for (String key1 : notes_today){
                                    otherUsers.child(key).child(key1).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            HashMap notes_final=(HashMap) dataSnapshot.getValue();

                                            String date=notes_final.get("date").toString();
                                            String file=notes_final.get("file").toString();
                                            String name=notes_final.get("name").toString();

                                            //if(!date.equals("")&&!file.equals("")&&!name.equals(""))
                                               datatype2 dt=new datatype2(name,file,date);
                                            str_notes.add(dt);
                                            Log.d("MY DATA MAH LIFE",date+file+name);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                    else {

                        otherUsers.child(key).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                HashMap hm = (HashMap) dataSnapshot.getValue();

                                Set<String> notes_today=hm.keySet();
                                for (String key1 : notes_today){
                                    otherUsers.child(key).child(key1).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            HashMap notes_final=(HashMap) dataSnapshot.getValue();

                                            String date=notes_final.get("date").toString();
                                            String file=notes_final.get("file").toString();
                                            String name=notes_final.get("name").toString();
                                            datatype2 dt=new datatype2(name,file,date);
                                            str_question.add(dt);
                                            Log.d("MY DATA MAH LIFE",date+file+name);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        /*DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference();
        ref1.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot
                        collectAllSubject(dataSnapshot);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });*/
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
                                            if(Type.getText().toString().equals("Question"))
                                                upload("Question",Name.getText().toString());
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

    /*private void collectAllSubject(DataSnapshot dataSnapshot) {
        String date="",file="",name="";
        for(DataSnapshot ds : dataSnapshot.getChildren()) {

            for(DataSnapshot ds1 : ds.getChildren()){
                for(DataSnapshot ds2: ds1.getChildren()){
                    if(ds1.getKey().equals("Notes")){
                        DataType temp= (DataType) ds2.getValue();
                        file=temp.file;
                        name=temp.name;
                        date=temp.date;
                    }
                    else{

                    }
                }
            }

            Log.d("TAG", name + " / " + file + "/" + date);
        }
    }*/

    private void upload(final String Type, final String name) {

        Log.d("IIIIIIII","Testingg");
        Toast.makeText(getApplicationContext(), "UPLOADING FILE", Toast.LENGTH_SHORT).show();
        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageRef = storage.getReference();
        final StorageReference fileRef = storageRef.child(x).child(Type+"/"+name+".pdf");
        fileRef.putFile(fileuri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return fileRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    downloadUri = task.getResult();
                    String link=downloadUri.toString();
                    DataType dt=new DataType(name,link);
                    //along with updations to realtime database
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference().child(x);

                    myRef.child(Type).push().setValue(dt);
                    
                    //str.add(dt);

                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.
                        }
                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Log.w("Error", "Failed to read value.", error.toException());
                        }
                    });
                    Log.d("Sexy",downloadUri.toString());
                } else {

                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        fileuri = data.getData();
        Log.d("IIIIIIIIIIIIIIIIIIIIII",fileuri.toString());
        super.onActivityResult(requestCode, resultCode, data);
    }

}
