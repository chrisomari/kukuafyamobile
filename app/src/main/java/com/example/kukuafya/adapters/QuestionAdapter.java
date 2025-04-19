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

import com.example.kukuafya.Question;
import com.example.kukuafya.R;
import com.example.kukuafya.db.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {

    private final List<Question> questionList;
    private final Context context;
    private final OnQuestionClickListener listener;
    private final String currentUserId;
    private final DatabaseHelper dbHelper;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());

    public interface OnQuestionClickListener {
        void onQuestionClick(Question question);
        void onUpvoteClick(Question question);
        void onDownvoteClick(Question question);
        void onBookmarkClick(Question question, boolean isBookmarked);
        void onDeleteClick(Question question);
    }

    public QuestionAdapter(Context context, List<Question> questionList, String currentUserId, OnQuestionClickListener listener) {
        this.context = context;
        this.questionList = questionList;
        this.listener = listener;
        this.currentUserId = currentUserId;
        this.dbHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        Question question = questionList.get(position);

        holder.tvTitle.setText(question.getTitle());
        holder.tvContent.setText(question.getContent());
        holder.tvUsername.setText(question.getUserName());
        holder.tvCategory.setText(question.getCategory());
        holder.tvDate.setText(dateFormat.format(question.getCreatedAt()));
        holder.tvUpvotes.setText(String.valueOf(question.getUpvotes()));

        // Set solved status
        if (question.isSolved()) {
            holder.ivSolved.setVisibility(View.VISIBLE);
        } else {
            holder.ivSolved.setVisibility(View.GONE);
        }

        // Set vote status
        int voteStatus = dbHelper.getUserVoteStatus(currentUserId, question.getId(), -1);
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
        boolean isBookmarked = dbHelper.isBookmarked(currentUserId, question.getId(), -1);
        if (isBookmarked) {
            holder.btnBookmark.setImageResource(R.drawable.ic_bookmark_filled);
        } else {
            holder.btnBookmark.setImageResource(R.drawable.ic_bookmark_outline);
        }

        // Show delete button only for user's own questions
        if (question.getUserId().equals(currentUserId)) {
            holder.btnDelete.setVisibility(View.VISIBLE);
        } else {
            holder.btnDelete.setVisibility(View.GONE);
        }

        // Set click listeners
        holder.itemView.setOnClickListener(v -> listener.onQuestionClick(question));

        holder.btnUpvote.setOnClickListener(v -> {
            listener.onUpvoteClick(question);
            notifyItemChanged(holder.getAdapterPosition());
        });

        holder.btnDownvote.setOnClickListener(v -> {
            listener.onDownvoteClick(question);
            notifyItemChanged(holder.getAdapterPosition());
        });

        holder.btnBookmark.setOnClickListener(v -> {
            boolean newBookmarkState = !isBookmarked;
            listener.onBookmarkClick(question, newBookmarkState);
            notifyItemChanged(holder.getAdapterPosition());
        });

        holder.btnDelete.setOnClickListener(v -> {
            listener.onDeleteClick(question);
            // Deletion will be handled in the fragment
        });
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public void updateQuestions(List<Question> newQuestions) {
        questionList.clear();
        questionList.addAll(newQuestions);
        notifyDataSetChanged();
    }

    static class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvContent, tvUsername, tvCategory, tvDate, tvUpvotes;
        ImageButton btnUpvote, btnDownvote, btnBookmark, btnDelete;
        ImageView ivSolved;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_question_title);
            tvContent = itemView.findViewById(R.id.tv_question_content);
            tvUsername = itemView.findViewById(R.id.tv_question_username);
            tvCategory = itemView.findViewById(R.id.tv_question_category);
            tvDate = itemView.findViewById(R.id.tv_question_date);
            tvUpvotes = itemView.findViewById(R.id.tv_question_upvotes);
            btnUpvote = itemView.findViewById(R.id.btn_question_upvote);
            btnDownvote = itemView.findViewById(R.id.btn_question_downvote);
            btnBookmark = itemView.findViewById(R.id.btn_question_bookmark);
            btnDelete = itemView.findViewById(R.id.btn_question_delete);
            ivSolved = itemView.findViewById(R.id.iv_question_solved);
        }
    }
}
