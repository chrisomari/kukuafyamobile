package com.example.kukuafya;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kukuafya.adapterclasses.adapterpost;
import com.example.kukuafya.models.forumPost;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class postlist extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_postlist);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        Intent gh=getIntent();
        String forum_question=gh.getStringExtra("forum_query");
        String category=gh.getStringExtra("forum_category");
        TextView gtt=findViewById(R.id.PostContentpostlist);
        TextView enm=findViewById(R.id.postcategorypostlist);
        gtt.setText(forum_question);
        enm.setText(category);
        List<forumPost> fms =new ArrayList<>();
        RecyclerView rv=findViewById(R.id.postlistrv);
        adapterpost adf=new adapterpost(fms,this);

//        forumPost frm=new forumPost("message","sender name","forum name");
//        fms.add(frm);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(postlist.this, RecyclerView.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(adf);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=firebaseDatabase.getReference("Posts");
        databaseReference.orderByChild("forum_name").equalTo(forum_question).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    forumPost frm = dataSnapshot.getValue(forumPost.class);
                    // Toast.makeText(forumlist.this, frm.getUser_query(), Toast.LENGTH_SHORT).show();
                    fms.add(frm);
                }
                adf.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(postlist.this, "Failed to retrieve data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}