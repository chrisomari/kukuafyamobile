package com.example.kukuafya.adapterclasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kukuafya.R;
import com.example.kukuafya.models.forumPost;

import java.util.List;

public class adapterpost  extends  RecyclerView.Adapter<adapterpost.ViewHolder> {

    private List<forumPost> postlist;
    private Context context;

    // constructor
    public  adapterpost(List<forumPost> postlist, Context context) {
        this.postlist = postlist;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.posts, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.TV3.setText(postlist.get(position).getSenderName());
        holder.TV4.setText(postlist.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return postlist.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        // creating variables for our text views.
        private TextView TV3, TV4;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            TV3 = itemView.findViewById(R.id.replysendername);
            TV4 = itemView.findViewById(R.id.tvPostreply);
        }
    }






}
