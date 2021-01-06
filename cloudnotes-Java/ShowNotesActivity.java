package com.example.cloudnotes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class ShowNotesActivity extends AppCompatActivity {

    class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{
        // catching the passed data to shoeing in recycler view
        List<String> titlesList;
        List<String> contentList;
        List<String> noteIdList;
        public Adapter(List<String> titlesList,List<String>contentList, List<String>noteIdList){
            this.titlesList=titlesList;
            this.contentList=contentList;
            this.noteIdList=noteIdList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Where the design present of each notes
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.note_view_design,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            // set data in widgets which are initialized in  ViewHolder
            holder.noteTitle.setText(titlesList.get(position));
            holder.noteContent.setText(contentList.get(position));
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(v.getContext(), UniNoteActivity.class);
                    intent.putExtra("title",titlesList.get(position));
                    intent.putExtra("content",contentList.get(position));
                    intent.putExtra("noteID",noteIdList.get(position));
                    v.getContext().startActivity(intent);
                }
            });

            ImageView imageView=holder.view.findViewById(R.id.delete_button);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(imageView.getContext());
                    builder.setTitle("Delete this Note?");
                    builder.setMessage("It will delete your note permanently");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                            String id= noteIdList.get(position);
                            Toast.makeText(ShowNotesActivity.this, "Please Wait...", Toast.LENGTH_SHORT).show();
                            DocumentReference documentReference=firebaseFirestore.collection("Notes").document(firebaseUser.getUid()).collection("UserNotes").document(id);
                            documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(ShowNotesActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                                    Intent mIntent = getIntent();
                                    startActivity(mIntent);
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ShowNotesActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                    Intent mIntent = getIntent();
                                    startActivity(mIntent);
                                    finish();
                                }
                            });
                        }
                    });
                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent mIntent = getIntent();
                            startActivity(mIntent);
                            finish();
                        }
                    });
                    AlertDialog alert= builder.create();
                    alert.show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return titlesList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            // where to show data in design...
            TextView noteTitle,noteContent;
            View view;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                noteTitle=itemView.findViewById(R.id.title_view);
                noteContent=itemView.findViewById(R.id.content_view);
//            noteContent.setMovementMethod(new ScrollingMovementMethod());
                view=itemView;// this is used for clicking handel
            }
        }
    }
    ProgressBar progressBar;
    Toolbar toolbar;
    FloatingActionButton floatingActionButton;
    RecyclerView recyclerView;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_notes);
        progressBar=findViewById(R.id.progressBar);
        recyclerView=findViewById(R.id.recycler_view);
        floatingActionButton=findViewById(R.id.addNote_floating_button);
        List<String> titles=new ArrayList<>();
        List<String> contents=new ArrayList<>();
        List<String> noteIDs=new ArrayList<>();
        // Notes/SEVSN1FrcDOBH4oMgEqaWPXoOfv2/UserNotes/ItLsWYDIQIJGRiSqhw2Y
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Notes").document(firebaseUser.getUid()).collection("UserNotes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(DocumentSnapshot snapshot: task.getResult()){
                    AddNoteActivity addNoteActivity=new AddNoteActivity();
                    String title=addNoteActivity.AES_DECRYPTION(snapshot.getString("Title"));
                    String content=addNoteActivity.AES_DECRYPTION(snapshot.getString("Content"));
                    String noteID=snapshot.getId();
                    titles.add(title);
                    contents.add(content);
                    noteIDs.add(noteID);
                }
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.INVISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ShowNotesActivity.this, "FAILED_ "+e.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        adapter=new Adapter(titles,contents,noteIDs);// Constructor of Adapter call called and passing two lists
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)); // 2 grid design
        recyclerView.setAdapter(adapter);// adapter object passed to recycler view...
        adapter.notifyDataSetChanged();

        toolbar=findViewById(R.id.showNotesToolbar);
        toolbar.setTitle("  CLOUD NOTES");
        String un=firebaseUser.getEmail();
        if(un==null){
            toolbar.setSubtitle("Anonymous Account");
        }
        else{
            toolbar.setSubtitle(un);
        }
        setSupportActionBar(toolbar);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShowNotesActivity.this,AddNoteActivity.class));
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.options_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.logout){
            if(firebaseUser.getEmail()==null){
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setTitle("Confirmation Box: ");
                builder.setMessage("ARE YOU SURE ? This is an Anonymous account. If you logout, then your notes will be no longer saved in Cloud Storage. You can Merge from here to save all of your notes in newly created account. We merge your Anonymous Account with you emailID..");
                builder.setPositiveButton("LogOut", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        firebaseUser=firebaseAuth.getCurrentUser();
                        firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(ShowNotesActivity.this, "Done", Toast.LENGTH_SHORT).show();
                            }
                        });
                        firebaseAuth.signOut();
                        startActivity(new Intent(ShowNotesActivity.this, MainActivity.class));
                        finish();
                    }
                });
                builder.setNegativeButton("Merge To Sync", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(ShowNotesActivity.this,RegisterActivity.class);
                        intent.putExtra("FlagOfAnonymousAccountMerge",true);
                        startActivity(intent);
                        finish();
                    }
                });
                AlertDialog alert= builder.create();
                alert.show();
            }
            else {
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setTitle("Confirmation Box: ");
                builder.setMessage("Do you want to LOGOUT");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        firebaseAuth.signOut();
                        startActivity(new Intent(ShowNotesActivity.this, MainActivity.class));
                        finish();
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss();
                    }
                });
                AlertDialog alert= builder.create();
                alert.show();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}