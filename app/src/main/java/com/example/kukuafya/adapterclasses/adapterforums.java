package com.example.kukuafya.adapterclasses;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kukuafya.R;
import com.example.kukuafya.models.Forum;
import com.example.kukuafya.postlist;

import java.util.List;

public class adapterforums extends RecyclerView.Adapter<adapterforums.ViewHolder> {

    private List<Forum> forumlist;
    private Context context;

    // constructor
    public  adapterforums(List<Forum> forumlist, Context context) {
        this.forumlist = forumlist;
        this.context = context;
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forums_layout, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.TV1.setText(forumlist.get(position).getUser_query());
        holder.TV2.setText(forumlist.get(position).getCategory());

        holder.cvd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bn=new Intent(context, postlist.class);
                bn.putExtra("forum_query",forumlist.get(position).getUser_query());
                bn.putExtra("forum_category",forumlist.get(position).getCategory());
                context.startActivity(bn);
            }
        });

    }

    @Override
    public int getItemCount() {
        return forumlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // creating variables for our text views.
        private TextView TV1, TV2;
        private CardView cvd;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            TV1 = itemView.findViewById(R.id.tvPostContent);
            TV2 = itemView.findViewById(R.id.postcategory);
            cvd = itemView.findViewById(R.id.crdviewforum);
        }
    }

}
