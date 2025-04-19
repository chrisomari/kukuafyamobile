package com.example.kukuafya.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kukuafya.Answer;
import com.example.kukuafya.R;
import com.example.kukuafya.db.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.AnswerViewHolder> {

    private final List<Answer> answerList;
    private final Context context;
    private final OnAnswerClickListener listener;
    private final String currentUserId;
    private final String questionUserId;
    private final DatabaseHelper dbHelper;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());

    public interface OnAnswerClickListener {
        void onUpvoteClick(Answer answer);
        void onDownvoteClick(Answer answer);
        void onBookmarkClick(Answer answer, boolean isBookmarked);
        void onAcceptClick(Answer answer);
        void onDeleteClick(Answer answer);
        void onReportClick(Answer answer);
    }

    public AnswerAdapter(Context context, List<Answer> answerList, String currentUserId, String questionUserId, OnAnswerClickListener listener) {
        this.context = context;
        this.answerList = answerList;
        this.listener = listener;
        this.currentUserId = currentUserId;
        this.questionUserId = questionUserId;
        this.dbHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public AnswerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_answer, parent, false);
        return new AnswerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerViewHolder holder, int position) {
        Answer answer = answerList.get(position);

        holder.tvContent.setText(answer.getContent());
        holder.tvUsername.setText(answer.getUserName());
        holder.tvDate.setText(dateFormat.format(answer.getCreatedAt()));
        holder.tvUpvotes.setText(String.valueOf(answer.getUpvotes()));

        // Set accepted status
        if (answer.isAccepted()) {
            holder.ivAccepted.setVisibility(View.VISIBLE);
            holder.btnAccept.setVisibility(View.GONE);
        } else {
            holder.ivAccepted.setVisibility(View.GONE);
            // Only show accept button for question owner
            if (currentUserId.equals(questionUserId)) {
                holder.btnAccept.setVisibility(View.VISIBLE);
            } else {
                holder.btnAccept.setVisibility(View.GONE);
            }
        }

        // Set vote status
        int voteStatus = dbHelper.getUserVoteStatus(currentUserId, answer.getQuestionId(), answer.getId());
        if (voteStatus == 1) {
            holder.btnUpvote.setColorFilter(context.getResources().getColor(R.color.colorAccent));
            holder.btnDownvote.setColorFilter(context.getResources().getColor(R.color.colorGray));
        } else if (voteStatus == -1) {
            holder.btnUpvote.setColorFilter(context.getResources().getColor(R.color.colorGray));
            holder.btnDownvote.setColorFilter(context.getResources().getColor(R.color.colorAccent));
        } else {
            holder.btnUpvote.setColorFilter(context.getResources().getColor(R.color.colorGray));
            holder.btnDownvote.setColorFilter(context.getResources().getColor(R.color.colorGray));
        }

        // Set bookmark status
        boolean isBookmarked = dbHelper.isBookmarked(currentUserId, answer.getQuestionId(), answer.getId());
        if (isBookmarked) {
            holder.btnBookmark.setImageResource(R.drawable.ic_bookmark_filled);
        } else {
            holder.btnBookmark.setImageResource(R.drawable.ic_bookmark_outline);
        }

        // Show delete button only for user's own answers
        if (answer.getUserId().equals(currentUserId)) {
            holder.btnDelete.setVisibility(View.VISIBLE);
        } else {
            holder.btnDelete.setVisibility(View.GONE);
        }

        // Set click listeners
        holder.btnUpvote.setOnClickListener(v -> {
            listener.onUpvoteClick(answer);
            notifyItemChanged(holder.getAdapterPosition());
        });

        holder.btnDownvote.setOnClickListener(v -> {
            listener.onDownvoteClick(answer);
            notifyItemChanged(holder.getAdapterPosition());
        });

        holder.btnBookmark.setOnClickListener(v -> {
            boolean newBookmarkState = !isBookmarked;
            listener.onBookmarkClick(answer, newBookmarkState);
            notifyItemChanged(holder.getAdapterPosition());
        });

        holder.btnAccept.setOnClickListener(v -> {
            listener.onAcceptClick(answer);
            notifyDataSetChanged(); // Refresh all items as accepted status may change for multiple answers
        });

        holder.btnDelete.setOnClickListener(v -> {
            listener.onDeleteClick(answer);
            // Deletion will be handled in the fragment
        });

        holder.btnReport.setOnClickListener(v -> {
            listener.onReportClick(answer);
            notifyItemChanged(holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return answerList.size();
    }

    public void updateAnswers(List<Answer> newAnswers) {
        answerList.clear();
        answerList.addAll(newAnswers);
        notifyDataSetChanged();
    }

    static class AnswerViewHolder extends RecyclerView.ViewHolder {
        TextView tvContent, tvUsername, tvDate, tvUpvotes;
        ImageButton btnUpvote, btnDownvote, btnBookmark, btnAccept, btnDelete, btnReport;
        ImageView ivAccepted;

        public AnswerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tv_answer_content);
            tvUsername = itemView.findViewById(R.id.tv_answer_username);
            tvDate = itemView.findViewById(R.id.tv_answer_date);
            tvUpvotes = itemView.findViewById(R.id.tv_answer_upvotes);
            btnUpvote = itemView.findViewById(R.id.btn_answer_upvote);
            btnDownvote = itemView.findViewById(R.id.btn_answer_downvote);
            btnBookmark = itemView.findViewById(R.id.btn_answer_bookmark);
            btnAccept = itemView.findViewById(R.id.btn_answer_accept);
            btnDelete = itemView.findViewById(R.id.btn_answer_delete);
            btnReport = itemView.findViewById(R.id.btn_answer_report);
            ivAccepted = itemView.findViewById(R.id.iv_answer_accepted);
        }
    }
}
