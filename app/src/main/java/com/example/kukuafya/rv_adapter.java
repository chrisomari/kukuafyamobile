package com.example.kukuafya;

import static androidx.core.app.ActivityCompat.recreate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class rv_adapter  extends RecyclerView.Adapter<rv_adapter.ViewHolder>{



    List<rem_item> remItems=new ArrayList<>();

    public rv_adapter(List<rem_item> remItems) {
        this.remItems = remItems;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
int i= holder.getAdapterPosition();

        holder.ta.setText(remItems.get(i).getTitle());
        holder.tb.setText(remItems.get(i).getFlock());
        holder.tc.setText(remItems.get(i).getAbout());
        holder.td.setText(remItems.get(i).getDate());



        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             holder.hj.deleteData(remItems.get(i).getTitle(),remItems.get(i).getFlock(),remItems.get(i).getDate(),remItems.get(i).getAbout());
              remItems.remove(i);
              notifyItemRemoved(i);
            }
        });

    }

    @Override
    public int getItemCount() {

        return remItems.size();
    }










    public class ViewHolder extends RecyclerView.ViewHolder {
          private DBhandler hj;
         private TextView ta,tb,tc,td,mk;
         private ImageView del;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ta=itemView.findViewById(R.id.title);
            tb=itemView.findViewById(R.id.flock);
            tc=itemView.findViewById(R.id.desc);
            td=itemView.findViewById(R.id.date);
            //mk=itemView.findViewById(R.id.dbid);
           del=itemView.findViewById(R.id.deleteRecord);
            hj=new DBhandler(itemView.getContext());
        }
    }






}
