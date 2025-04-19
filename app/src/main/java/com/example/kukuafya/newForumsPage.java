package com.example.kukuafya;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kukuafya.models.Forum;
import com.example.kukuafya.models.forumPost;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class newForumsPage extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference, dbref;
    private Forum forum;
    private forumPost Forumpost;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_forums_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText et1,et2;
        et1=findViewById(R.id.postquestionET);
        et2=findViewById(R.id.CategoryET);
        Button btn=findViewById(R.id.sendPost);



        forum = new Forum();
        Forumpost =new forumPost();
        btn.setOnClickListener(v -> {
            String name = "chris";
            String question = et1.getText().toString();
            String Category = et2.getText().toString();

            if (name.isEmpty() && question.isEmpty() && Category.isEmpty()) {
                Toast.makeText(newForumsPage.this, "Please add some data.", Toast.LENGTH_SHORT).show();
            } else {
                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference("Forums");
                addDataToFirebase(name, question,Category);

            }
        });





    }


    private void addDataToFirebase(String name, String question, String Category) {
        forum.setUser_query(question);
        forum.setCategory(Category);

        Forumpost.setForum_name(question);
        Forumpost.setMessage("hello too");
        Forumpost.setSenderName("Eugene ");

        dbref=firebaseDatabase.getReference("Posts");

        // Use push() to create a unique key for each person
        databaseReference.child(forum.getUser_query()).setValue(forum)
                .addOnSuccessListener(aVoid ->
                        Toast.makeText(newForumsPage.this, "Data added successfully!", Toast.LENGTH_SHORT).show()
                )
                .addOnFailureListener(error ->
                        Toast.makeText(newForumsPage.this, "Failed to add data: " + error.getMessage(), Toast.LENGTH_SHORT).show()
                );

        dbref.child(Forumpost.getSenderName()).setValue(Forumpost).addOnSuccessListener(aVoid ->
                Toast.makeText(this, "added", Toast.LENGTH_SHORT).show()
        ).addOnFailureListener(aVoid ->
                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
        );

    }


}